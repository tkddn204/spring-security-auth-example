package com.rightpair.entity;

import com.rightpair.entity.types.UsersStateType;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import static com.rightpair.entity.types.RolesType.ASSOCIATE_USER;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)

    private UsersStateType state;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private final Set<UsersRole> roles = new HashSet<>();

    @Builder
    public Users(String nickname, String email, String password) {
        this(nickname, email, password, new Roles(ASSOCIATE_USER));
    }

    @Builder
    public Users(String nickname, String email, String password, Roles roles) {
        this.roles.add(new UsersRole(this, roles));
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
