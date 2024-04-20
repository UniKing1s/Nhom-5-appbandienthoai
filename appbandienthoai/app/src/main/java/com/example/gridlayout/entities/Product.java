package com.example.gridlayout.entities;

public class Product {
    private int id;
    private int masp;
    private String name;
    private double price;
    private String type;
    private int quantity;
    private int sale;
    private String decribtion;
    private String status;

    private String img;

    public Product() {
    }

    public Product(int id, int masp, String name, double price, String type, int quantity, int sale, String decribtion, String status, String img) {
        this.id = id;
        this.masp = masp;
        this.name = name;
        this.price = price;
        this.type = type;
        this.quantity = quantity;
        this.sale = sale;
        this.decribtion = decribtion;
        this.status = status;
        this.img = img;
    }

    public int getMasp() {
        return masp;
    }

    public void setMasp(int masp) {
        this.masp = masp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public String getDecribtion() {
        return decribtion;
    }

    public void setDecribtion(String decribtion) {
        this.decribtion = decribtion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    @Override
//    public String toString() {
//        return "Product{" +
//                "name='" + name + '\'' +
//                ", price=" + price +
//                '}';
//    }
}
