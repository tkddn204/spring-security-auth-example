package com.rightpair.service;

import com.rightpair.entity.Users;
import com.rightpair.entity.types.UsersStateType;
import com.rightpair.event.UserUpdateEvent;
import com.rightpair.exception.AlreadySignedUserException;
import com.rightpair.exception.NullReferenceEntityException;
import com.rightpair.exception.UserNotFoundException;
import com.rightpair.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional(readOnly = true)
    public Users getById(Long userId) {
        return usersRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public void create(Users users) {
        if (users == null) {
            throw new NullReferenceEntityException(Users.class);
        }
        if (usersRepository.existsByEmail(users.getEmail())) {
            throw new AlreadySignedUserException();
        }
        Users newUsers = users
                .encryptPassword(passwordEncoder.encode(users.getPassword()))
                .newFaceUser();
        usersRepository.save(newUsers);
    }

    @Transactional
    public void updateNickName(Long userId, Users users) {
        if (users == null) {
            throw new NullReferenceEntityException(Users.class);
        }
        Users user = usersRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setNickname(Optional.ofNullable(users.getNickname()).orElse(user.getNickname()));
        applicationEventPublisher.publishEvent(new UserUpdateEvent(this, user));
    }

    @Transactional
    public void delete(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setState(UsersStateType.DELETED);
    }
}
