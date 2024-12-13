package com.project.qlbh_kh.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainAppController implements Initializable {
    @FXML
    protected TreeView<String> treeView;

    @FXML
    protected BorderPane mainBorderPane;

    @FXML
    protected StackPane contentArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TreeItem<String> root = new TreeItem<>();
        TreeItem<String> trangChu = new TreeItem<>("Trang chu");
        TreeItem<String> hoaDon = new TreeItem<>("Hóa đơn");
        TreeItem<String> hangNhap = new TreeItem<>("Hàng Nhập");
        TreeItem<String> hangXuat = new TreeItem<>("Hàng Xuất");
        TreeItem<String> hangTonKho = new TreeItem<>("Hàng Tồn Kho");
        root.getChildren().addAll(trangChu, hoaDon, hangNhap, hangXuat, hangTonKho);

        // hóa đơn
        TreeItem<String> taoHoaDon = new TreeItem<>("Tạo hóa đơn");
        TreeItem<String> truyXuatHoaDon = new TreeItem<>("Truy xuất hóa đơn");
        hoaDon.getChildren().addAll(taoHoaDon, truyXuatHoaDon);

        // tồn kho
        TreeItem<String> kiemKho = new TreeItem<>("Kiểm kho");
        TreeItem<String> truyXuatKho = new TreeItem<>("Truy xuất kho");
        hangTonKho.getChildren().addAll(kiemKho, truyXuatKho);

        root.setExpanded(true);
        treeView.setRoot(root);
        treeView.setShowRoot(false);
        treeView.setOnMouseClicked(mouseEvent -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            selectedItem.setExpanded(!selectedItem.isExpanded());
            try {
                if (selectedItem == trangChu) loadContent("/com/project/qlbh_kh/views/TrangChuView.fxml");
                else if (selectedItem == taoHoaDon) loadContent("/com/project/qlbh_kh/views/taoHoaDonView.fxml");
                else if (selectedItem == truyXuatHoaDon) loadContent("/com/project/qlbh_kh/views/truyXuatHoaDonView.fxml");
                else if (selectedItem == hangNhap) loadContent("/com/project/qlbh_kh/views/quanLyHangNhapView.fxml");
                else if (selectedItem == hangXuat) loadContent("/com/project/qlbh_kh/views/quanLyHangXuatView.fxml");
                else if (selectedItem == kiemKho) loadContent("/com/project/qlbh_kh/views/kiemKhoView.fxml");
                else if (selectedItem == truyXuatKho) loadContent("/com/project/qlbh_kh/views/truyXuatKhoView.fxml");

            }catch (Exception e)
            {
                e.printStackTrace();
            }

        });

        try{
            loadContent("/com/project/qlbh_kh/views/TrangChuView.fxml");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Phương thức để tải và thay thế nội dung ở phần giữa (center) của BorderPane
    protected void loadContent(String fxmlFile) throws IOException {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Node node = loader.load();
            Object controller = loader.getController();
            if (controller instanceof TrangChuController) {
                ((TrangChuController) controller).setMainAppController(this);
            }
            contentArea.getChildren().setAll(node);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
