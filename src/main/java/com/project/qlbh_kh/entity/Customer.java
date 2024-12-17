package com.project.qlbh_kh.entity;
public class Customer {
    private int customer_id;
    private String name;
    private String address;
    private String address_detail;
    private String operation;
    private String phone_number;
    private String email;
    private Double paid_payment;
    private Double unpaid_payment;

    public Customer(int customer_id, String name, String address, String phone_number){
        this.customer_id = customer_id;
        this.name = name;
        this.phone_number = phone_number;
        this.address = address;
    }
    public Customer(int customer_id, String name, String address, String phone_number, String email, Double paid_payment, Double unpaid_payment)
    {
        this.customer_id = customer_id;
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
        this.email = email;
        this.paid_payment = paid_payment;
        this.unpaid_payment = unpaid_payment;
    }

    public int getCustomer_id() {
        return customer_id;
    }
    public void setCustomer_id(int customer_in_id) {
        this.customer_id = customer_in_id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress_detail() {
        return address_detail;
    }
    public void setAddress_detail(String address_detail) {
        this.address_detail = address_detail;
    }
    public String getOperation() {
        return operation;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }
    public String getPhone_number() {
        return phone_number;
    }
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
    public String getEmail() {
        return  this.email;
    }

    public void setPaid_payment(Double paid_payment){
        this.paid_payment = paid_payment;
    }

    public Double getPaid_payment(){
        return paid_payment;
    }

    public void setUnpaid_payment(Double unpaid_payment){
        this.unpaid_payment = unpaid_payment;
    }

    public Double getUnpaid_payment(){
        return unpaid_payment;
    }


}