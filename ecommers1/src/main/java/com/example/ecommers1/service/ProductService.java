package com.example.ecommers1.service;

import com.example.ecommers1.model.Product;
import com.example.ecommers1.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    // ✅ Create or Update product
    public Product saveProduct(Product product) {
        return repo.save(product);
    }

    // ✅ Read all products
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    // ✅ Read product by ID
    public Product getProductById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // ✅ Delete product
    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }
}
