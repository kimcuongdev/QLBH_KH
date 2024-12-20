package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Receiver;
import com.project.qlbh_kh.utils.JDBCUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChinhSuaNguoiNhanNhapController {
    @FXML
    private TextField oldAddress;
    @FXML
    private TextField newAddress;
    @FXML
    private TextField oldEmail;
    @FXML
    private TextField newEmail;
    @FXML
    private TextField oldPhoneNumber;
    @FXML
    private TextField newPhoneNumber;
    @FXML
    private Label receiverNameLabel;
    @FXML
    private Button confirmButton;
    private QuanLyNguoiNhanInController mainController;
    private Receiver selectedReceiver;
    public void setMainController(QuanLyNguoiNhanInController mainController) {
        this.mainController = mainController;
    }
    public void setSelectedReceiver(Receiver selectedReceiver) {
        this.selectedReceiver = selectedReceiver;
        receiverNameLabel.setText(selectedReceiver.getName());
        oldAddress.setText(selectedReceiver.getAddress());
        oldEmail.setText(selectedReceiver.getEmail());
        oldPhoneNumber.setText(selectedReceiver.getPhone_number());
        newPhoneNumber.requestFocus();
    }

    @FXML
    public void modifyDatabase() {
        String newAddressValue = newAddress.getText().trim();
        String newEmailValue = newEmail.getText().trim();
        String newPhoneNumberValue = newPhoneNumber.getText().trim();
        try {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE receivers_in_tb SET address = ?, email = ?, phone_number = ? WHERE receivers_in_id = ?");
            preparedStatement.setString(1, newAddressValue.isEmpty() ? selectedReceiver.getAddress() : newAddressValue);
            preparedStatement.setString(2, newEmailValue.isEmpty() ? selectedReceiver.getEmail() : newEmailValue);
            preparedStatement.setString(3, newPhoneNumberValue.isEmpty() ? selectedReceiver.getPhone_number() : newPhoneNumberValue);
            preparedStatement.setInt(4, selectedReceiver.getReceiver_id());

//            System.out.println(selectedReceiver.getReceiver_id() + " " + newAddressValue + " " + newEmailValue + " " + newPhoneNumberValue);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                // Thông báo thành công
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thành công");
                alert.setContentText("Đã cập nhật thông tin người nhận thành công!");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Đóng cửa sổ chỉnh sửa
                        confirmButton.getScene().getWindow().hide();
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.mainController.loadReceiverList(3);
    }
}