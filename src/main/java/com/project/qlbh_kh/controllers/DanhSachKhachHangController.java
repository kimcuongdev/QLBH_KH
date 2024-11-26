package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Customer;
import com.project.qlbh_kh.utils.JDBCUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class DanhSachKhachHangController implements Initializable {
    @FXML
    protected TableView<Customer> customerList;
    protected ObservableList<Customer> customers = FXCollections.observableArrayList();
    @FXML
    protected TableColumn<Customer, String> addressColumn;

    @FXML
    protected TableColumn<Customer, String> customerNameColumn;

    @FXML
    protected TextField customerNameField;

    @FXML
    protected TableColumn<Customer, String> phoneNumberColumn;

    protected BasicController mainController;
    public void setMainController(BasicController basicController) {
        this.mainController = basicController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //cai dat thuoc tinh du lieu cho cac cot
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        //load du lieu
        loadCustomerList();
        //cai dat bo loc tim kiem theo ten KH
        customerNameField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            customerList.setItems(customers.filtered(customer -> {
                String name = customer.getName();
                System.out.println(name);
                return name.toLowerCase().contains(newValue.toLowerCase());
            }));
        }));
        //chon KH
        customerList.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue != null && mainController != null)
            {
                mainController.setSelectedCustomer(newValue.getCustomer_id(),newValue.getName());
                ((Stage) customerList.getScene().getWindow()).close();
            }
        }));
    }
    public void loadCustomerList()
    {
        String sql = "exec customers_list";
        try
        {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                String phone_number = resultSet.getString(4);
                customers.add(new Customer(id,name,address,phone_number));
            }
            customerList.setItems(customers);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
