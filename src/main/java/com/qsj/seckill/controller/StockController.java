package com.qsj.seckill.controller;

import com.qsj.seckill.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StockController {

    @Autowired
    private StockService stockService;

    @RequestMapping("createorder/{sid}")
    @ResponseBody
    public String createOrder(@PathVariable int sid){
        System.out.println("sid info:"+sid);
        int id = stockService.createOrder(sid);
        return id+"";
    }

}
