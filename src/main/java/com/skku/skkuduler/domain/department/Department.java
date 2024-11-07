package com.skku.skkuduler.domain.department;

import com.skku.skkuduler.domain.BaseEntity;
import com.skku.skkuduler.domain.calender.Calendar;
import jakarta.persistence.*;
import lombok.Getter;

@Entity(name = "department")
@Getter
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    @OneToOne(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private Calendar calendar;

    private String name;
}