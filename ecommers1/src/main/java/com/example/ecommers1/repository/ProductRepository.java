package com.example.ecommers1.repository;

import com.example.ecommers1.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByPriceBetweenOrderByPriceAsc(Double min, Double max);
    List<Product> findByPriceGreaterThanEqualOrderByPriceAsc(Double min);
    List<Product> findAllByOrderByPriceAsc();
}
