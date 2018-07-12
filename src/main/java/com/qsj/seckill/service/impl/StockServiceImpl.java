package com.qsj.seckill.service.impl;

import com.qsj.seckill.domain.Stock;
import com.qsj.seckill.domain.StockOrder;
import com.qsj.seckill.repository.StockOrderRepository;
import com.qsj.seckill.repository.StockRepository;
import com.qsj.seckill.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockOrderRepository stockOrderRepository;
    @Override
    public int createOrder(int sid) {
        Stock stock = checkStock(sid);
        stock = saleStock(stock);
        StockOrder stockOrder = createSaleOrder(stock);
        return stockOrder.getId();
    }

    private Stock checkStock(int sid) {
        Stock stock = stockRepository.findOne(sid);
        if(stock!=null&&stock.getSale()==stock.getCount()){
            throw new RuntimeException("库存不足");
        }
        return stock;
    }

    private Stock saleStock(Stock stock){
        stock.setSale(stock.getSale()+1);
        return stockRepository.save(stock);
    }

    private StockOrder createSaleOrder(Stock stock){
        StockOrder stockOrder = new StockOrder();
        stockOrder.setSid(stock.getId());
        stockOrder.setName(stock.getName());
        return stockOrderRepository.save(stockOrder);
    }
}
