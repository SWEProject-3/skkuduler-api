package com.skku.skkuduler.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeEntity{
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
    public void updateProfile(String email, String name){
        if(email != null) this.email = email;
        if(name != null) this.name = name;
    }
    public void changePassword(String newPassword) {
        if(newPassword != null) this.password = newPassword;

    }

}
