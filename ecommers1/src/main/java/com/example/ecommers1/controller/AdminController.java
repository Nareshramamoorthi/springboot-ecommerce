package com.example.ecommers1.controller;

import com.example.ecommers1.model.Product;
import com.example.ecommers1.repository.UserRepository;
import com.example.ecommers1.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final UserRepository userRepository;

    public AdminController(ProductService productService, UserRepository userRepository) {
        this.productService = productService;
        this.userRepository = userRepository;
    }

    // Admin Home page -> shows products + users
    @GetMapping("/home")
    public String adminHome(Model model) {
        model.addAttribute("product", new Product()); // empty product for add form
        model.addAttribute("products", productService.getAllProducts()); // list products
        model.addAttribute("users", userRepository.findAll()); // list users
        return "admin-home";
    }

    // Add new product with image upload
    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = imageFile.getOriginalFilename();
            File dest = new File(uploadDir + fileName);
            imageFile.transferTo(dest);

            product.setImagelink(fileName); // save only filename
        }

        productService.saveProduct(product);
        return "redirect:/admin/home";
    }

    // Edit product form
    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) return "redirect:/admin/home";

        model.addAttribute("product", product); // prefill form
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("users", userRepository.findAll());

        return "admin-home"; // reuse same page
    }

    // Update product with image upload
    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute Product product,
                                @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        product.setProductId(id);

        if (!imageFile.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = imageFile.getOriginalFilename();
            File dest = new File(uploadDir + fileName);
            imageFile.transferTo(dest);

            product.setImagelink(fileName);
        }

        productService.saveProduct(product); // save acts as update
        return "redirect:/admin/home";
    }

    // Delete product
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/home";
    }

    // Delete user
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/home";
    }
}
