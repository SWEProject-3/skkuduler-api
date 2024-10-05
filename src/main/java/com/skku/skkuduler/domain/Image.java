package com.skku.skkuduler.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    private String src;

    @Column(name = "`order`")
    private int order;

    @ManyToOne
    @JoinColumn(name = "eventId")
    private Event event;

}
