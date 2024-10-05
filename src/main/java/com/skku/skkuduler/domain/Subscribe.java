package com.skku.skkuduler.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Subscribe extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscribeId;

    @ManyToOne
    @JoinColumn(name = "eventId")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;


}
