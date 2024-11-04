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

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Permission> permissions;

    public enum Role {
        ADMIN,
        USER
    }

    public static User of(Long userId) {
        return User.builder().userId(userId).build();
    }

    public void subscribeDepartment(Long departmentId) {
        subscriptions.add(new Subscription(null, departmentId, this));
    }

    public boolean isSubscribed(Long departmentId){
        return subscriptions.stream().anyMatch(subscription -> subscription.getDepartmentId().equals(departmentId));
    }

    public boolean unsubscribeDepartment(Long departmentId){
        return subscriptions.removeIf(subscription -> subscription.getDepartmentId().equals(departmentId));
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void changeRole(Role role) {
        this.role = role;
    }

}