package com.qsj.seckill.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="stock")
@Data
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private int count;
    private int sale;
    @Version
    private int version;
}
