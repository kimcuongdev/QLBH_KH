package com.project.qlbh_kh.entity;

public class Order_manager {
    private int id;
    private String customer_name;
    private double total_payment;
    private String receiver_name;
    private String date;
    private String operation;
    public Order_manager(int id, String customer_name, double total_payment, String receiver_name, String date, String operation)
    {
        this.id = id;
        this.customer_name = customer_name;
        this.total_payment = total_payment;
        this.receiver_name = receiver_name;
        this.date = date;
        this.operation = operation;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public double getTotal_payment() {
        return total_payment;
    }

    public void setTotal_payment(double total_payment) {
        this.total_payment = total_payment;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
