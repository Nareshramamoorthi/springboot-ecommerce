package com.example.ecommers1.model;

public class CartItem {
    private Long productId;
    private String name;
    private Double price;
    private Integer quantity;
    private String imagelink;

    public CartItem() {}

    public CartItem(Long productId, String name, Double price, Integer quantity, String imagelink) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imagelink = imagelink;
    }

    // getters / setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getImagelink() { return imagelink; }
    public void setImagelink(String imagelink) { this.imagelink = imagelink; }

    public Double getTotal() {
        return price * quantity;
    }
}
