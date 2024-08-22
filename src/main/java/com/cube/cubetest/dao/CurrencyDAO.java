package com.cube.cubetest.dao;

import com.cube.cubetest.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyDAO extends JpaRepository<Currency,Long> {
    @Query("select c from Currency as c")
     List<Currency> getAll();

    void deleteByCode(String code);
}
