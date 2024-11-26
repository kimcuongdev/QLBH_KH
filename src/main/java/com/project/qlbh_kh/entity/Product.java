package com.project.qlbh_kh.entity;

public class Product {
    private String prod_name;
    private int prod_id;
    private int quantity;
    private String unit_sold;
    private double price_in;
    private double price_out;
    public Product(int prod_id, String prod_name, double price_in)
    {
        this.prod_name = prod_name;
        this.prod_id = prod_id;
        this.price_in = price_in;
    }
    public Product(int prod_id, String prod_name)
    {
        this.prod_name = prod_name;
        this.prod_id = prod_id;
    }
    public Product(String prod_name, int quantity)
    {
        this.prod_name = prod_name;
        this.quantity = quantity;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public int getProd_id() {
        return prod_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit_sold() {
        return unit_sold;
    }

    public void setUnit_sold(String unit_sold) {
        this.unit_sold = unit_sold;
    }

    public double getPrice_in() {
        return price_in;
    }

    public void setPrice_in(double price_in) {
        this.price_in = price_in;
    }

    public double getPrice_out() {
        return price_out;
    }

    public void setPrice_out(double price_out) {
        this.price_out = price_out;
    }
}
