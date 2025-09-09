package com.example.ecommers1.service;

import com.example.ecommers1.model.Product;
import com.example.ecommers1.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class OrderService {

    private final ProductRepository productRepository;

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Place order: check stock and decrement. transactional to avoid partial updates.
    @Transactional
    public void placeOrder(Map<Long, Integer> items) {
        for (Map.Entry<Long, Integer> e : items.entrySet()) {
            Long productId = e.getKey();
            Integer qty = e.getValue();

            Product p = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

            if (p.getStock() == null || p.getStock() < qty) {
                throw new RuntimeException("Insufficient stock for: " + p.getName());
            }

            p.setStock(p.getStock() - qty);
            productRepository.save(p);
        }
        // optional: create and save an Order entity here (not required for stock decrement)
    }
}
