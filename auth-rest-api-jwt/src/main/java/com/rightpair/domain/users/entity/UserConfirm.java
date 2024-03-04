package com.rightpair.domain.users.entity;

import com.rightpair.domain.users.entity.types.ConfirmStateType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserConfirm {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @PrimaryKeyJoinColumn
    private User user;

    private String code;

    private ConfirmStateType state = ConfirmStateType.WAIT;

    private LocalDateTime expiredAt;

    private UserConfirm(User user, String code, LocalDateTime expiredAt) {
        this.user = user;
        this.code = code;
        this.expiredAt = expiredAt;
    }

    public static UserConfirm from(User user) {
        return new UserConfirm(user, RandomStringUtils.random(32), LocalDateTime.now().plusMinutes(30));
    }

    public void updateCompleted() {
        this.state = ConfirmStateType.COMPLETE;
    }

    public boolean isCompleted() {
        return this.state.equals(ConfirmStateType.COMPLETE);
    }

    public boolean isExpired() {
        return this.expiredAt.isBefore(LocalDateTime.now()) || this.isCompleted();
    }

    public void refreshCode() {
        this.code = RandomStringUtils.random(32);
    }
}
