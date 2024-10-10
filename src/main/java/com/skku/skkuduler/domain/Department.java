package com.skku.skkuduler.domain;

import com.skku.skkuduler.domain.user.Subscription;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Department extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deptId;

    private String name;

    @OneToMany(mappedBy = "department")
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "department")
    private List<Major> majors;
}
