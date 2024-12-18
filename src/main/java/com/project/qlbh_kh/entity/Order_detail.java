package com.project.qlbh_kh.entity;

public class Order_detail {
    private String prod_name;
    private int quantity;
    private double unit_price;
    private double total_amount;
    private String customerName;
    private String receiverName;
    private String email;
    private String phone_number;
    private double total_payment;
    private String order_date;
    public Order_detail(String prod_name, int quantity, double unit_price, double total_amount)
    {
        this.prod_name = prod_name;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.total_amount = total_amount;
        this.order_date = order_date;
    }

    public String getOrder_date() {return order_date;}

    public void setOrder_date(String order_date) {this.order_date = order_date;}

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

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public double getTotal_payment() {
        return total_payment;
    }

    public void setTotal_payment(double total_payment) {
        this.total_payment = total_payment;
    }
}
