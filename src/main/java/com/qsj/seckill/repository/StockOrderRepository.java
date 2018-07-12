package com.qsj.seckill.repository;

import com.qsj.seckill.domain.StockOrder;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockOrderRepository extends PagingAndSortingRepository<StockOrder,Integer> {
}
