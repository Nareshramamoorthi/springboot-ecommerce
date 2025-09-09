package com.example.ecommers1.controller;

import com.example.ecommers1.model.CartItem;
import com.example.ecommers1.model.Product;
import com.example.ecommers1.service.OrderService;
import com.example.ecommers1.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/user/cart")
public class CartController {

    private final ProductService productService;
    private final OrderService orderService;

    public CartController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    /** ------------------ Add to Cart ------------------ **/
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") Integer quantity,
                            HttpSession session,
                            RedirectAttributes ra) {

        Product p = productService.getProductById(productId);
        if (p == null) {
            ra.addFlashAttribute("error", "Product not found");
            return "redirect:/user/home";
        }

        if (quantity == null || quantity < 1) quantity = 1;

        @SuppressWarnings("unchecked")
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute("cart", cart);
        }

        CartItem existing = cart.get(productId);
        int newQty = quantity + (existing != null ? existing.getQuantity() : 0);

        if (newQty > (p.getStock() == null ? 0 : p.getStock())) {
            ra.addFlashAttribute("error", "Only " + (p.getStock() == null ? 0 : p.getStock()) + " left in stock");
            return "redirect:/user/product/" + productId;
        }

        if (existing == null) {
            cart.put(productId, new CartItem(productId, p.getName(), p.getPrice(), quantity, p.getImagelink()));
        } else {
            existing.setQuantity(newQty);
        }

        ra.addFlashAttribute("success", "Added to cart");
        return "redirect:/user/cart/view";
    }

    /** ------------------ View Cart ------------------ **/
    @GetMapping("/view")
    public String viewCart(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new LinkedHashMap<>();

        double total = cart.values().stream().mapToDouble(CartItem::getTotal).sum();
        model.addAttribute("items", cart.values());
        model.addAttribute("total", total);
        return "cart";
    }

    /** ------------------ Remove Item ------------------ **/
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        @SuppressWarnings("unchecked")
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
        if (cart != null) {
            cart.remove(id);
            ra.addFlashAttribute("success", "Removed item");
        }
        return "redirect:/user/cart/view";
    }

    /** ------------------ Checkout ------------------ **/
    @PostMapping("/checkout")
    public String checkout(HttpSession session, RedirectAttributes ra) {
        @SuppressWarnings("unchecked")
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            ra.addFlashAttribute("error", "Your cart is empty");
            return "redirect:/user/cart/view";
        }

        // Build items map productId -> qty
        Map<Long, Integer> items = new HashMap<>();
        for (CartItem it : cart.values()) {
            items.put(it.getProductId(), it.getQuantity());
        }

        try {
            orderService.placeOrder(items); // will reduce stock here
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/user/cart/view";
        }

        session.removeAttribute("cart");
        ra.addFlashAttribute("success", "Order placed successfully");
        return "redirect:/user/cart/success";
    }

    /** ------------------ Success Page ------------------ **/
    @GetMapping("/success")
    public String orderSuccess() {
        return "order-success"; // <-- matches order-success.html
    }
}
