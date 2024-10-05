package com.skku.skkuduler.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deptId;

    private String name;

    @OneToMany(mappedBy = "department")
    private List<Major> majors;
}
