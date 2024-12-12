package com.project.qlbh_kh.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TrangChuController {
    private MainAppController mainAppController;

    @FXML
    private Button productManageButton;

    @FXML
    private Button receiverInManageButton;

    @FXML
    private Button receiverOutManageButton;

    public void setMainAppController(MainAppController mainAppController) {
        this.mainAppController = mainAppController;
    }

    @FXML
    private void initialize() {
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
                    mainAppController.loadContent("/com/project/qlbh_kh/views/danhSachNguoiNhanNhapView.fxml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        receiverOutManageButton.setOnAction(event -> {
            if (mainAppController != null) {
                try {
                    mainAppController.loadContent("/com/project/qlbh_kh/views/danhSachNguoiNhanXuatView.fxml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
