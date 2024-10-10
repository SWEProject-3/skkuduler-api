package com.skku.skkuduler.domain.user;

import com.skku.skkuduler.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "subscription")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;

    private Long departmentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
