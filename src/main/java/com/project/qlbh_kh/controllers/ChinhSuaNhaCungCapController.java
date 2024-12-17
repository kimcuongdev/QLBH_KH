package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Customer;
import com.project.qlbh_kh.utils.JDBCUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChinhSuaNhaCungCapController {
    @FXML private TextField oldPhoneNumber;
    @FXML private TextField newPhoneNumber;
    @FXML private TextField oldAddress;
    @FXML private TextField newAddress;
    @FXML private TextField oldEmail;
    @FXML private TextField newEmail;
    @FXML private Label customerNameLabel;
    @FXML private Button confirmButton;
    private QuanLyNhaCungCapController mainController;
    private Customer selectedCustomer;
    public void setMainController(QuanLyNhaCungCapController quanLyNhaCungCapController)
    {
        this.mainController = quanLyNhaCungCapController;
    }
    public void setSelectedCustomer(Customer selectedCustomer)
    {
        this.selectedCustomer = selectedCustomer;
        customerNameLabel.setText(selectedCustomer.getName());
        oldPhoneNumber.setText(selectedCustomer.getPhone_number());
        oldAddress.setText(selectedCustomer.getAddress());
        if(!selectedCustomer.getEmail().isEmpty()) oldEmail.setText(selectedCustomer.getEmail());
        else oldEmail.setText("Chưa có email");
    }
    @FXML
    public void modifyDatabase()
    {
        String newPhoneNumberValue = newPhoneNumber.getText().trim();
        String newAddressValue = newAddress.getText().trim();
        String newEmailValue = newEmail.getText().trim();
        // Nếu giá mới không được nhập, sử dụng giá cũ
        try
        {
            String phoneNumber = newPhoneNumberValue.isEmpty() ? selectedCustomer.getPhone_number() : (newPhoneNumberValue);
            String address = newAddressValue.isEmpty() ? selectedCustomer.getAddress() : (newAddressValue);
            String email = newEmailValue.isEmpty() ? selectedCustomer.getEmail() : (newEmailValue);
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE customers_in_tb SET phone_number = ?, address = ?, email = ? WHERE customer_in_id = ?");
            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, email);
            preparedStatement.setInt(4, selectedCustomer.getCustomer_id());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                // Thông báo thành công
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thành công");
                alert.setContentText("Đã cập nhật thông tin nhà cung cấp thành công!");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Đóng cửa sổ chỉnh sửa
                        confirmButton.getScene().getWindow().hide();
                        // Gọi phương thức cập nhật danh sách trong mainController
                        if (mainController != null) {
                            mainController.openCustomerInList();
                        }
                    }
                });
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
