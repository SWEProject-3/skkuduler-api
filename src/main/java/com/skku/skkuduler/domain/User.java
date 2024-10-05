package com.skku.skkuduler.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user")
    private List<Subscribe> subscriptions;

    @OneToMany(mappedBy = "myId")
    private List<Friendship> friendships;

    public enum Role {
        ADMIN,
        USER
    }
    public void changeProfile(String email, String name){
        if(email != null) this.email = email;
        if(name != null) this.name = name;
    }
    public void changePassword(String newPassword) {
        if(newPassword != null) this.password = newPassword;
    }
    public void changeRole(Role role){
        this.role = role;
    }

}
