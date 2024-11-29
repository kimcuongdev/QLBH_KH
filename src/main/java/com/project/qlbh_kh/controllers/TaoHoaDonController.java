package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Customer;
import com.project.qlbh_kh.entity.Order_manager;
import com.project.qlbh_kh.entity.Product;
import com.project.qlbh_kh.utils.JDBCUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TaoHoaDonController extends BasicController {
    ObservableList<Product> productList = FXCollections.observableArrayList();;
    @FXML public TextField phoneNumberField;
    @FXML public TextField addressField;
    @FXML public Button findCustomerButton;
    @FXML public Button addProductButton;
    @FXML public Button saveButton;

    @FXML public TableView<Product> tableView;
    @FXML public TableColumn<Product,String> productNameColumn;
    @FXML public TableColumn<Product,Double> unitPriceColumn;
    @FXML public TableColumn<Product,Double> totalAmountColumn;
    @FXML public TableColumn<Product,Integer> quantityColumn;
    ObservableList<Order_manager> data = FXCollections.observableArrayList();
    @FXML
    public TextField tongTienTextField;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        super.initialize(url,resourceBundle);
        ObservableList<String> operations = FXCollections.observableArrayList("Nhập", "Xuất");
        customerNameField.textProperty().addListener(((observableValue, oldValue,newValue) -> {
        // Kiểm tra xem TextField có trống không
        if (newValue.isEmpty()) {
            System.out.println("TextField is empty.");
        } else {
            System.out.println(selectedCustomerId);
            System.out.println("TextField has text.");
            setCustomerInformation(selectedCustomerId);
        }
    }));
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

        tableView.setEditable(true);

        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("prod_name"));

        // Thiết lập cho cột giá
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price_in"));

        // Thiết lập cho cột số lượng
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Product, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Product, Integer> productIntegerCellEditEvent) {
                Product product = productIntegerCellEditEvent.getRowValue();
                product.setQuantity(productIntegerCellEditEvent.getNewValue());
                tableView.refresh();
            }
        });

        // Thiết lập cho cột thành tiền, thành tiền = price_in * quantity
        totalAmountColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            return Bindings.createDoubleBinding(
                    () -> {
                        try {
                            double price = product.getPrice_in();
                            int quantity = product.getQuantity();
                            return price * quantity ;
                        } catch (NumberFormatException nfe) {
                            return (double) 0;
                        }
                    }
            ).asObject();
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
                .anyMatch(product1 -> product1.getProd_name().equals(product.getProd_name()));

        if (exists) {
            Product existingProduct = productList.stream()
                    .filter(product1 -> product1.getProd_name().equals(product.getProd_name()))
                    .findFirst().orElse(null);
            if (existingProduct != null) {
                existingProduct.setQuantity(existingProduct.getQuantity() + 1);
                tinhTongTien();
            }
        } else {
            Product newProduct = new Product(product.getProd_name(), 1,product.getPrice_in());
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
    public void setCustomerInformation(int customerID)
    {
        System.out.println("tao dang setting " + customerID );
        String sql = "exec customers_list";

        try
        {

            Connection connection = JDBCUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                System.out.println("dang lay sql");
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                String phone_number = resultSet.getString(4);
                if(id == customerID){
                    phoneNumberField.setText(phone_number);
                    addressField.setText(address);
                    System.out.println("da lay dc khach hang");
                }

            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
