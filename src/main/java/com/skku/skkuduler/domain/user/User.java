package com.skku.skkuduler.domain.user;

import com.skku.skkuduler.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Permission> permissions;

    public enum Role {
        ADMIN,
        USER
    }
    public static User of(Long userId){
        return User.builder().userId(userId).build();
    }
    public void changeEmail(String email){
        if(email != null) this.email = email;
    }
    public void changeName(String name){
        if(name != null) this.name = name;
    }
    public void changePassword(String newPassword) {
        if(newPassword != null) this.password = newPassword;
    }
    public void changeRole(Role role){
        this.role = role;
    }

}
