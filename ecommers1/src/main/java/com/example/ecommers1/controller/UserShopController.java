package com.example.ecommers1.controller;

import com.example.ecommers1.model.Product;
import com.example.ecommers1.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserShopController {

    private final ProductRepository productRepository;

    public UserShopController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // /user/home?min=0&max=1000  OR /user/home?min=5000 (for 5000+)
    @GetMapping("/home")
    public String home(@RequestParam(required = false) Double min,
                       @RequestParam(required = false) Double max,
                       Model model) {

        List<Product> products;

        if (min != null && max != null) {
            // Filter by price range and sort ascending
            products = productRepository.findByPriceBetweenOrderByPriceAsc(min, max);
        } else if (min != null) {
            // Filter for price >= min and sort ascending
            products = productRepository.findByPriceGreaterThanEqualOrderByPriceAsc(min);
        } else {
            // No filter, show all sorted by price
            products = productRepository.findAllByOrderByPriceAsc();
        }

        model.addAttribute("products", products);
        model.addAttribute("min", min);
        model.addAttribute("max", max);
        return "user-home";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return "redirect:/user/home";
        model.addAttribute("product", product);
        return "product-detail";
    }


}
