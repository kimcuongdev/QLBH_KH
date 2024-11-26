package com.project.qlbh_kh.entity;

public class Product_manager {
    private int id;
    private String customer_name;
    private String prod_name;
    private int quantity;
    private double unit_price;
    private double total_amount;
    private String date;
    public Product_manager(int id, String customer_name, String prod_name, int quantity, double unit_price, double total_amount, String date)
    {
        this.id = id;
        this.customer_name = customer_name;
        this.prod_name = prod_name;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.total_amount = total_amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(int unit_price) {
        this.unit_price = unit_price;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
