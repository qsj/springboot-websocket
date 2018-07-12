package com.qsj.seckill.repository;

import com.qsj.seckill.domain.Stock;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends PagingAndSortingRepository<Stock,Integer> {
}
