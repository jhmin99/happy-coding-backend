package com.example.demo.service;

import com.example.demo.dto.response.follow.FollowResponseDto;
import com.example.demo.entity.Following;
import com.example.demo.entity.Users;
import com.example.demo.repository.FollowingRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FollowingServiceImpl implements IFollowingService{

    private final UserRepository userRepository;
    private final FollowingRepository followingRepository;

    public FollowingServiceImpl(UserRepository userRepository, FollowingRepository followingRepository) {
        this.userRepository = userRepository;
        this.followingRepository = followingRepository;
    }

    @Override
    public void following(Long userId, Long followingUserId) {
        Users users = userRepository.findById(userId).get();
        Users followingUser = userRepository.findById(followingUserId).get();

        users.addFollowing(Following.createFollowing(followingUser));
    }

    @Override
    public void unfollowing(Long userId, Long followingUserId) {
        Users users = userRepository.findById(userId).get();
        users.getFollowers().removeIf(following -> Objects.equals(followingUserId, following.getFollow().getId()));
    }

    @Override
    public Page<FollowResponseDto> getAllFollowings(Long userId, Pageable pageable) {
        return followingRepository.findAll(pageable)
                .map(following -> new FollowResponseDto(following.getFollow().getId(), following.getFollow().getName(), following.getFollow().getProfileImages()));
    }
}
