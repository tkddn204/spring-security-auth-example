package com.rightpair.service;

import com.rightpair.entity.User;
import com.rightpair.entity.types.UsersStateType;
import com.rightpair.event.UserUpdateEvent;
import com.rightpair.exception.AlreadySignedUserException;
import com.rightpair.exception.NullReferenceEntityException;
import com.rightpair.exception.UserNotFoundException;
import com.rightpair.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional(readOnly = true)
    public User getById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public void create(User user) {
        if (user == null) {
            throw new NullReferenceEntityException(User.class);
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AlreadySignedUserException();
        }
        User newUser = user
                .encryptPassword(passwordEncoder.encode(user.getPassword()))
                .newFaceUser();
        userRepository.save(newUser);
    }

    @Transactional
    public void updateNickName(Long userId, User users) {
        if (users == null) {
            throw new NullReferenceEntityException(User.class);
        }
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setNickname(Optional.ofNullable(users.getNickname()).orElse(user.getNickname()));
        applicationEventPublisher.publishEvent(new UserUpdateEvent(this, user));
    }

    @Transactional
    public void delete(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setState(UsersStateType.DELETED);
    }
}
