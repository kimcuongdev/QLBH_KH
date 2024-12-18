package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Order_detail;
import com.project.qlbh_kh.utils.JDBCUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class HienThiHoaDonController implements Initializable {
    @FXML private Label orderTypeLabel;
    @FXML private Label customerNameLabel;
    @FXML private Label addressLabel;
    @FXML private Label phoneNumberLabel;
    @FXML private Label emailLabel;
    @FXML private Label email;
    @FXML private Label receiverNameLabel;
    @FXML private Label totalPaymentLabel;
    @FXML private Label dateLabel;
    @FXML private TableView<Order_detail> tableView;
    @FXML private TableColumn<Order_detail,String> productNameColumn;
    @FXML private TableColumn<Order_detail,Integer> quantityColumn;
    @FXML private TableColumn<Order_detail,Double> unitPriceColumn;
    @FXML private TableColumn<Order_detail,Double> totalAmountColumn;
    ObservableList<Order_detail> orderDetails = FXCollections.observableArrayList();
    private String operation;
    private int id;
    //public void setOperation(String operation) { this.operation = operation; orderTypeLabel.setText("Hóa đơn mua vào");}
    public void setId(int id) {this.id = id; }
    public String formatDate(String inputDate)
    {
        String[] parts = inputDate.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        return day + " tháng " + month + " năm " + year;
    }
    public void setUpData(int id, String operation)
    {
        String sql;
        this.operation = operation;
        this.id = id;
        if (this.operation.equals("in"))
        {
            sql = "{call dbo.showOrderInDetail(?)}";
            orderTypeLabel.setText("Hóa đơn mua vào");
        }
        else
        {
            sql ="{call dbo.showOrderOutDetail(?)}";
            orderTypeLabel.setText("Hoá đơn bán ra");
        }
        try
        {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,this.id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                orderDetails.add(new Order_detail(
                        resultSet.getString(1),
                        resultSet.getInt(2),
                        resultSet.getDouble(3),
                        resultSet.getDouble(4)
                ));
                customerNameLabel.setText(resultSet.getString(5));
                addressLabel.setText(resultSet.getString(6));
                phoneNumberLabel.setText(resultSet.getString(7));
                if (resultSet.getString(8) == null)
                {
                    emailLabel.setVisible(false);
                    email.setVisible(false);
                }
                else emailLabel.setText(resultSet.getString(8));
                receiverNameLabel.setText(resultSet.getString(9));
                totalPaymentLabel.setText(resultSet.getString(10));
                dateLabel.setText(formatDate(resultSet.getString(11)));
            }
            while(resultSet.next())
            {
                orderDetails.add(new Order_detail(
                        resultSet.getString(1),
                        resultSet.getInt(2),
                        resultSet.getDouble(3),
                        resultSet.getDouble(4)
                ));
            }
            tableView.setItems(orderDetails);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("prod_name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
    }
}
