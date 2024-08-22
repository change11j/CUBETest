package com.cube.cubetest.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data

public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;
    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String chineseName;
    @Column(nullable = false)
    private Double rate;

}
