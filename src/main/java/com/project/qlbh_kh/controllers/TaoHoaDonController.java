package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Order_manager;
import com.project.qlbh_kh.entity.Product;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TaoHoaDonController extends BasicController {
    ObservableList<Product> productList = FXCollections.observableArrayList();;
    @FXML private Button findCustomerButton;
    @FXML private Button addProductButton;
    @FXML private TableView<Product> tableView;
    @FXML private TableColumn<Product,Double> productNameColumn;
    @FXML private TableColumn<Product,Double> unitPriceColumn;
    @FXML private TableColumn<Product,Double> totalAmountColumn;
    @FXML private TableColumn<Product,Double> quantityColumn;
    ObservableList<Order_manager> data = FXCollections.observableArrayList();
    @FXML
    private TextField tongTienTextField;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        super.initialize(url,resourceBundle);
        customerNameField.setDisable(true);
        receiverNameField.setDisable(true);
        ObservableList<String> operations = FXCollections.observableArrayList("Nhập", "Xuất");
        operationBox.setItems(operations);
        operationBox.valueProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (oldValue == null || !oldValue.equals(newValue))
            {
                customerNameField.clear();
                selectedCustomerId = 0;
                receiverNameField.clear();
                selectedReceiverId = 0;
            }
            if (newValue.equals("Nhập") || newValue.equals("Xuất"))
            {
                customerNameField.setDisable(false);
                receiverNameField.setDisable(false);
            }
            else
            {
                customerNameField.setDisable(true);
                receiverNameField.setDisable(true);
            }
        }));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("prod_name"));

        // Thiết lập cho cột giá
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price_in"));

        // Thiết lập cho cột số lượng
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Thiết lập cho cột thành tiền, thành tiền = price_in * quantity
        totalAmountColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            double totalAmount = product.getPrice_in() * product.getQuantity();
            return new ReadOnlyObjectWrapper<>(totalAmount);
        });

        tableView.setItems(productList);
        tableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.BACK_SPACE ||event.getCode() == KeyCode.DELETE) {
                Product selectedItem = tableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    productList.remove(selectedItem); // Xóa mặt hàng đã chọn
                    tableView.setItems(productList); // Cập nhật bảng
                }
            }
        });
    }

    @FXML
    public void openCustomerList()
    {
        System.out.println("open customer list");
        if (operation == null)
        {
            System.out.println("null handle");
        }
        else if (operation.equals("in"))
        {
            System.out.println("in handle");
            try {
                //load view cho danh sach khach hang
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/danhSachKhachHangNhapView.fxml"));
                Scene customerInListScene = new Scene(fxmlLoader.load());
                //set controller cha cho controller cua danh sach ten mat hang
                DanhSachKhachHangNhapController controller = fxmlLoader.getController();
                controller.setMainController(this);
                //tao stage moi
                Stage customerListStage = new Stage();
                customerListStage.initModality(Modality.APPLICATION_MODAL);
                customerListStage.initOwner(customerNameField.getScene().getWindow());
                customerListStage.setTitle("Danh sách khách hàng");
                customerListStage.setScene(customerInListScene);
                //show
                customerListStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (operation.equals("out"))
        {
            System.out.println("out handle");
            try {
                //load view cho danh sach khach hang
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/danhSachKhachHangXuatView.fxml"));
                Scene customerInListScene = new Scene(fxmlLoader.load());
                //set controller cha cho controller cua danh sach ten mat hang
                DanhSachKhachHangXuatController controller = fxmlLoader.getController();
                controller.setMainController(this);
                //tao stage moi
                Stage customerListStage = new Stage();
                customerListStage.initModality(Modality.APPLICATION_MODAL);
                customerListStage.initOwner(customerNameField.getScene().getWindow());
                customerListStage.setTitle("Danh sách khách hàng");
                customerListStage.setScene(customerInListScene);
                //show
                customerListStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void addProductToTable(Product product) {
        boolean exists = productList.stream()
                .anyMatch(matHang -> matHang.getProd_name().equals(product.getProd_name()));

        if (exists) {
            Product existingProduct = productList.stream()
                    .filter(matHang -> matHang.getProd_name().equals(product.getProd_name()))
                    .findFirst().orElse(null);
            if (existingProduct != null) {
                existingProduct.setQuantity(existingProduct.getQuantity() + 1);
                tinhTongTien();
            }
        } else {
            Product newProduct = new Product(product.getProd_name(), (int) product.getPrice_in());
            newProduct.setQuantity(1);
            productList.add(newProduct);
            tinhTongTien();
        }
        tableView.setItems(productList);
    }
//
    @FXML
    private void addProduct() {
        String fxmlPath = "/com/project/qlbh_kh/views/product_popup.fxml";

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            if (loader.getLocation() == null) {
                System.out.println("FXML file not found: " + fxmlPath);
                return;
            }

            Scene productInList = new Scene(loader.load());
            PopUpController popupController = loader.getController();
            popupController.setMainController(this);
            String operationType = operationBox.getValue(); // Lấy giá trị được chọn
            if (operationType != null) {
                popupController.setType(operationType); // Truyền giá trị loại hàng
            }

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(tableView.getScene().getWindow());
            popupStage.setTitle("Chọn Mặt Hàng");
            popupStage.setScene(productInList);
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//
    private void tinhTongTien() {
        Double tongTien = productList.stream()
                .mapToDouble(product -> product.getPrice_in() * product.getQuantity())
                .sum();
        tongTienTextField.setText(String.valueOf(tongTien));
    }
}
