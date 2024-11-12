package com.skku.skkuduler.domain.user;

import com.skku.skkuduler.domain.BaseEntity;
import com.skku.skkuduler.domain.calender.Calendar;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "user")
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"email","is_available"})
)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Calendar calendar;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Permission> permissions;

    @Column(updatable = false, insertable = false)
    private boolean isAvailable;

    public enum Role {
        ADMIN,
        USER
    }

    public static User of(Long userId) {
        return User.builder().userId(userId).build();
    }

    public void changeCalender(Calendar calendar){
        if(calendar != null) this.calendar = calendar;
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