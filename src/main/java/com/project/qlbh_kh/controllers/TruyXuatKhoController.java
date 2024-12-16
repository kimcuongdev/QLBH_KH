package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Stock_manager;
import com.project.qlbh_kh.utils.JDBCUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class TruyXuatKhoController extends BasicController {
    @FXML private TableView<Stock_manager> tableView;
    @FXML private TableColumn<Stock_manager, String> orderIdColumn;
    @FXML private TableColumn<Stock_manager, String> productColumn;
    @FXML private TableColumn<Stock_manager, Integer> quantityColumn;
    @FXML private TableColumn<Stock_manager, String> dateColumn;
    @FXML private TableColumn<Stock_manager, String> operationColumn;
    @FXML private TableColumn<Stock_manager, String> statusColumn;
    ObservableList<Stock_manager> data = FXCollections.observableArrayList();
    @FXML
    public void reset()
    {
        this.selectedProductId = 0;
        this.productField.clear();
    }
    @FXML public void resetProduct() {this.productField.clear(); this.selectedProductId = 0;}
    @FXML public void resetFromDate() {this.fromDate.setValue(null); this.fromDateValue = null;}
    @FXML public void resetToDate() {this.toDate.setValue(null); this.toDateValue = null;}
    @FXML public void resetOperation() {this.operationBox.setValue(null); this.operation = null;}
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Call the initialize method of the superclass to ensure it runs
        super.initialize(url, resourceBundle);

        // Set up table columns here
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        productColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        operationColumn.setCellValueFactory(new PropertyValueFactory<>("operation"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        String query = "{call dbo.Product_all}";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                data.add(new Stock_manager(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getString(5),
                        (rs.getInt(7) == 0) ? "Chưa thanh toán" : "Đã thanh toán"
                ));
            }
            tableView.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //chon hoa don
        tableView.setOnMouseClicked(mouseEvent -> {
            Stock_manager selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem.getOperation().equals("Nhập"))  showOrder(selectedItem.getOrderId(),"in");
            else showOrder(selectedItem.getOrderId(),"out");
        });
    }
    @FXML
    public void executeQuery()
    {

        System.out.println(selectedProductId);
        if (fromDate != null) System.out.println("From Date: " + fromDateValue);
        if (toDate != null) System.out.println("To Date: " + toDateValue);
        if (operation != null) System.out.println("Operation: " + operation);
        if (selectedProductName != null) System.out.println("Selected Product Name: " + selectedProductName);
        if (selectedProductName == null) System.out.println("Not Selected Product Name");
        if (operation == null || operation.equals("both"))
        {
            try
            {
                Connection connection = JDBCUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("{call dbo.Product_all(?,?,?)}");
                preparedStatement.setInt(1, selectedProductId );
                preparedStatement.setDate(2, fromDateValue == null ? null : Date.valueOf(fromDateValue));
                preparedStatement.setDate(3, toDateValue == null ? null : Date.valueOf(toDateValue));
                ResultSet resultSet = preparedStatement.executeQuery();
                data.clear();
                while (resultSet.next())
                {
                    data.add(new Stock_manager(
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getInt(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            (resultSet.getInt(7) == 0) ? "Chưa thanh toán" : "Đã thanh toán"
                    ));
                }
                tableView.setItems(data);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (operation.equals("in"))
        {
            try
            {
                Connection connection = JDBCUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("{call dbo.Product_in(?,?,?)}");
                preparedStatement.setInt(1,selectedProductId);
                preparedStatement.setDate(2, fromDateValue == null ? null : Date.valueOf(fromDateValue));
                preparedStatement.setDate(3, toDateValue == null ? null : Date.valueOf(toDateValue));
                ResultSet resultSet = preparedStatement.executeQuery();
                data.clear();
                while (resultSet.next())
                {
                    data.add(new Stock_manager(
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getInt(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            (resultSet.getInt(7) == 0) ? "Chưa thanh toán" : "Đã thanh toán"
                    ));
                }
                tableView.setItems(data);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                Connection connection = JDBCUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("{call dbo.Product_out(?,?,?)}");
                preparedStatement.setInt(1,selectedProductId);
                preparedStatement.setDate(2, fromDateValue == null ? null : Date.valueOf(fromDateValue));
                preparedStatement.setDate(3, toDateValue == null ? null : Date.valueOf(toDateValue));
                ResultSet resultSet = preparedStatement.executeQuery();
                data.clear();
                while (resultSet.next())
                {
                    data.add(new Stock_manager(
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getInt(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            (resultSet.getInt(7) == 0) ? "Chưa thanh toán" : "Đã thanh toán"
                    ));
                }
                tableView.setItems(data);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
