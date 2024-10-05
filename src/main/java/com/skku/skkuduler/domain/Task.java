package com.skku.skkuduler.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Task {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private int priority;
    private String createdBy;
    private String description;

}
