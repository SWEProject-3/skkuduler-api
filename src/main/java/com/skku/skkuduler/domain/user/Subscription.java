package com.skku.skkuduler.domain.user;

import com.skku.skkuduler.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "subscription")
@Table(uniqueConstraints = @UniqueConstraint (columnNames = {"user_id","department_id"}))
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;

    @Column(nullable = false)
    private Long departmentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}