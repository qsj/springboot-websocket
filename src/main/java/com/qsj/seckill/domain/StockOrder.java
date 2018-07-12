package com.qsj.seckill.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="stock_order")
@Data
public class StockOrder {
    @Id
    private int id;
    private int sid;
    private String name;
    private Date createTime;
}
