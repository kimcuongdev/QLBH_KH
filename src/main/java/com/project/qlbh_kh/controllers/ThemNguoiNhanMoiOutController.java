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

public class ThemNguoiNhanMoiOutController {
    @FXML
    private TextField address;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField phone;
    @FXML
    private TextField email;

    @FXML
    private TextField receiverName;

    private QuanLyNguoiNhanOutController mainController;

    public void setMainController(QuanLyNguoiNhanOutController quanLyNguoiNhanController)
    {
        this.mainController = quanLyNguoiNhanController;
    }
    @FXML
    public void addNewReceiverToDatabase()
    {
        String receiverNameInput = receiverName.getText();
        String addressInput = address.getText();
        String phoneInput = phone.getText();
        String emailInput = email.getText();
        if (receiverNameInput.isEmpty() || addressInput.isEmpty() || phoneInput.isEmpty()) {
            showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin trước khi xác nhận!", Alert.AlertType.ERROR);
            return;
        }
        try
        {
            String[] receiverNameValue = receiverNameInput.split(" ");
            String firstName = receiverNameValue[0];
            String lastName = "";
            for(int i = 1; i < receiverNameValue.length; i++)
                lastName = lastName + receiverNameValue[i] + " ";

            String addressValue = addressInput;
            String phoneValue = phoneInput;
            String emailValue = emailInput;

            Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO receivers_out_tb (firstname, lastname, address, phone_number, email) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, addressValue);
            preparedStatement.setString(4, phoneValue);
            preparedStatement.setString(5, emailValue);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thành công");
                alert.setContentText("Đã thêm mới thành công!");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Đóng cửa sổ thêm mặt hàng
                        confirmButton.getScene().getWindow().hide();
                        // Gọi openProductList
                        if (mainController != null) {
                            mainController.loadReceiverList(3);
                        }
                    }
                });
            } else {
                showAlert("Lỗi", "Không thể thêm. Vui lòng thử lại.", Alert.AlertType.ERROR);
            }
        }
        catch (NumberFormatException e)
        {
            showAlert("Lỗi", "Vui lòng nhập đúng định dạng cho giá mua vào và giá bán ra!", Alert.AlertType.ERROR);
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