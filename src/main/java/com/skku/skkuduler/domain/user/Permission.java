package com.skku.skkuduler.domain.user;

import com.skku.skkuduler.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "permission")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Permission{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    private Long departmentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}