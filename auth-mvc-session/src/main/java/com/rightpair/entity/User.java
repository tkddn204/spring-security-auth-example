package com.rightpair.entity;

import com.rightpair.entity.types.UsersStateType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import static com.rightpair.entity.types.RoleType.ASSOCIATE_USER;

@Entity
@Getter
@Setter
@ToString
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "닉네임을 입력해야 합니다.")
    @Column(nullable = false)
    private String nickname;

    @NotEmpty(message = "이메일을 입력해야 합니다.")
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @ToString.Exclude
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UsersStateType state = UsersStateType.ACTIVE;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private final Set<UsersRole> userRoles = new HashSet<>();

    public User(String nickname, String email, String password) {
        this(nickname, email, password, new Roles(ASSOCIATE_USER));
    }

    public User(String nickname, String email, String password, Roles userRoles) {
        this.userRoles.add(new UsersRole(this, userRoles));
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public static User empty() {
        return new User();
    }

    public User encryptPassword(String encryptedPassword) {
        this.password = encryptedPassword;
        return this;
    }

    public User newFaceUser() {
        this.userRoles.add(new UsersRole(this, new Roles(ASSOCIATE_USER)));
        return this;
    }
}
