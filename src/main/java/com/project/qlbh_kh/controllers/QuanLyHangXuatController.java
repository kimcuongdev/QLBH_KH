package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Product_manager;
import com.project.qlbh_kh.utils.JDBCUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class QuanLyHangXuatController extends BasicController {
    @FXML private TableView<Product_manager> tableView;
    @FXML private TableColumn<Product_manager,Integer> idColumn;
    @FXML private TableColumn<Product_manager,String> customerNameColumn;
    @FXML private TableColumn<Product_manager,String> prodNameColumn;
    @FXML private TableColumn<Product_manager,Integer> quantityColumn;
    @FXML private TableColumn<Product_manager,Integer> unitPriceColumn;
    @FXML private TableColumn<Product_manager,Integer> totalAmountColumn;
    @FXML private TableColumn<Product_manager,String> dateColumn;
    ObservableList<Product_manager> data = FXCollections.observableArrayList();
    @FXML public void resetCustomer() {this.selectedCustomerId = 0; this.customerNameField.clear();}
    @FXML public void resetProduct() {this.selectedProductId = 0; this.productField.clear();}
    @FXML public void resetFromDate() {this.fromDate.setValue(null); this.fromDateValue = null;}
    @FXML public void resetToDate() {this.toDate.setValue(null); this.toDateValue = null;}
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        super.initialize(url,resourceBundle);
        //set up table view
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
        prodNameColumn.setCellValueFactory(new PropertyValueFactory<>("prod_name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        try
        {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("{call dbo.Hang_Xuat(?,?,?,?)}");
            preparedStatement.setInt(1,selectedCustomerId);
            preparedStatement.setInt(2,selectedProductId);
            preparedStatement.setDate(3,fromDateValue == null ? null : Date.valueOf(fromDateValue));
            preparedStatement.setDate(4,toDateValue == null ? null : Date.valueOf(toDateValue));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                data.add(new Product_manager(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        resultSet.getDouble(5),
                        resultSet.getDouble(6),
                        resultSet.getString(7)
                ));
            }
            tableView.setItems(data);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        //chon hoa don
        tableView.setOnMouseClicked(mouseEvent -> {
            Product_manager selectedItem = tableView.getSelectionModel().getSelectedItem();
            showOrder(selectedItem.getId(),"out");
        });

    }
    @FXML
    public void executeQuery()
    {
        if (fromDate != null) System.out.println("From Date: " + fromDateValue);
        if (toDate != null) System.out.println("To Date: " + toDateValue);
        if (selectedCustomerId != 0) System.out.println("Customer out ID " + selectedCustomerId);
        if (selectedProductName != null) System.out.println("Product Name: "+ selectedProductName);
        try
        {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("{call dbo.Hang_Xuat(?,?,?,?)}");
            preparedStatement.setInt(1,selectedCustomerId);
            preparedStatement.setInt(2,selectedProductId);
            preparedStatement.setDate(3,fromDateValue == null ? null : Date.valueOf(fromDateValue));
            preparedStatement.setDate(4,toDateValue == null ? null : Date.valueOf(toDateValue));
            ResultSet resultSet = preparedStatement.executeQuery();
            data.clear();
            while (resultSet.next())
            {
                data.add(new Product_manager(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        resultSet.getDouble(5),
                        resultSet.getDouble(6),
                        resultSet.getString(7)
                ));
            }
            tableView.setItems(data);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @FXML
    void openCustomerOutList() {
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
