package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.utils.JDBCUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ThemKhachMuaMoiController {
    @FXML private Button confirmButton;
    @FXML private TextField firstNameCustomerOutName;
    @FXML private TextField lastNameCustomerOutName;
    @FXML private TextField address;
    @FXML private TextField phoneNumber;
    @FXML private TextField email;
    private QuanLyKhachMuaController mainController;
    public void setMainController(QuanLyKhachMuaController quanLyKhachMuaController)
    {
        this.mainController = quanLyKhachMuaController;
    }
    @FXML
    public void addNewCustomerOutToDatabase()
    {
        String firstCustomerOutNameInput = firstNameCustomerOutName.getText();
        String lastCustomerOutNameInput = lastNameCustomerOutName.getText();        
        String addressInput = address.getText();
        String phoneNumberInput = phoneNumber.getText();
        String emailInput = email.getText();            
        if (lastCustomerOutNameInput.isEmpty()  || firstCustomerOutNameInput.isEmpty() || addressInput.isEmpty() || phoneNumberInput.isEmpty() ) {
            showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin trước khi xác nhận!", Alert.AlertType.ERROR);
            return;
        }
        try
        {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO customers_out_tb (firstname, lastname, address, phone_number, email) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, firstCustomerOutNameInput);
            preparedStatement.setString(2, lastCustomerOutNameInput);
            preparedStatement.setString(3, addressInput);
            preparedStatement.setString(4, phoneNumberInput);
            preparedStatement.setString(5, emailInput);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thành công");
                alert.setContentText("Đã thêm mặt hàng mới thành công!");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Đóng cửa sổ thêm mặt hàng
                        confirmButton.getScene().getWindow().hide();
                        // Gọi openProductList
                        if (mainController != null) {
                            mainController.openCustomerOutList(3);
                        }
                    }
                });
            } else {
                showAlert("Lỗi", "Không thể thêm khách hàng. Vui lòng thử lại.", Alert.AlertType.ERROR);
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
