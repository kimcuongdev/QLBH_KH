package com.project.qlbh_kh.controllers;
import com.project.qlbh_kh.utils.JDBCUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TrangChuController {
    private MainAppController mainAppController;
    @FXML private Label totalProduct;
    @FXML private Label totalCustomerOut;
    @FXML private Label totalCustomerIn;
    @FXML private Label totalReceiverOut;
    @FXML private Label totalReceiverIn;
    @FXML private Button customerOutManageButton;
    @FXML private Button productManageButton;
    @FXML private Button customerInManageButton;
    @FXML
    private Button receiverInManageButton;

    @FXML
    private Button receiverOutManageButton;
    public void setTotalProduct(int totalProduct) { this.totalProduct.setText(String.valueOf(totalProduct)); }

    public void setTotalCustomerOut(int totalCustomerOut) { this.totalCustomerOut.setText(String.valueOf(totalCustomerOut)); }

    public void setTotalCustomerIn(int totalCustomerIn) { this.totalCustomerIn.setText(String.valueOf(totalCustomerIn)); }

    public void setTotalReceiverOut(int totalReceiverOut) { this.totalReceiverOut.setText(String.valueOf(totalReceiverOut)); }

    public void setTotalReceiverIn(int totalReceiverIn) { this.totalReceiverIn.setText(String.valueOf(totalReceiverIn)); }
    public void setMainAppController(MainAppController mainAppController) {
        this.mainAppController = mainAppController;
    }
    @FXML
    private void initialize() {
        try
        {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("exec getTotalInfo");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                setTotalProduct(resultSet.getInt(1));
                setTotalCustomerOut(resultSet.getInt(2));
                setTotalCustomerIn(resultSet.getInt(3));
                setTotalReceiverOut(resultSet.getInt(4));
                setTotalReceiverIn(resultSet.getInt(5));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        productManageButton.setOnAction(event -> {
            if (mainAppController != null) {
                try {
                    mainAppController.loadContent("/com/project/qlbh_kh/views/QuanLyMatHangView.fxml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        receiverInManageButton.setOnAction(event -> {
            if (mainAppController != null) {
                try {
                    mainAppController.loadContent("/com/project/qlbh_kh/views/QuanLyNguoiNhanInView.fxml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        receiverOutManageButton.setOnAction(event -> {
            if (mainAppController != null) {
                try {
                    mainAppController.loadContent("/com/project/qlbh_kh/views/QuanLyNguoiNhanOutView.fxml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        customerOutManageButton.setOnAction(event -> {
            if (mainAppController != null) {
                try {
                    mainAppController.loadContent("/com/project/qlbh_kh/views/QuanLyKhachMuaView.fxml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        customerInManageButton.setOnAction(event -> {
            if (mainAppController != null) {
                try {
                    mainAppController.loadContent("/com/project/qlbh_kh/views/QuanLyNhaCungCapView.fxml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}