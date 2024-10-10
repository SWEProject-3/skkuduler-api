package com.skku.skkuduler.domain.event;

import com.skku.skkuduler.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "image")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    private String src;

    @Column(name = "`order`")
    private int order;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

}
