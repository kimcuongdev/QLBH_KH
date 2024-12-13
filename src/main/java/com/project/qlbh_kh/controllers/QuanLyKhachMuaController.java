package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Customer;
import com.project.qlbh_kh.utils.JDBCUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class QuanLyKhachMuaController implements Initializable {
    @FXML private TextField searchField;
    @FXML private TableView<Customer> tableView; //tableView lưu customerOut
    @FXML private TableColumn<Customer,String> customerOutNameColumn; //tableColumn luu thuoc tinh customerOutName co kieu du lieu la String
    @FXML private TableColumn<Customer,String> addressColumn;
    @FXML private TableColumn<Customer,String> phoneNumberColumn;
    @FXML private TableColumn<Customer,String> emailColumn;
    @FXML private Button addNewCustomerOutButton;
    @FXML private Button reloadTableButton;
    private ObservableList<Customer> customerOutList = FXCollections.observableArrayList(); //observablelist de luu nhung doi tuong duoc hien thi trong tableview
    @Override
    //phuong thuong initialize se duoc goi de set up cac doi tuong trong scene truoc khi hien thi tren man hinh
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //thiet lap thuoc tinh cho cac cot, dung ten thuoc tinh cua lop product
        customerOutNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        //them su kien chinh sua mat hang cho tableview
        tableView.setOnMouseClicked(mouseEvent -> {
            Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();
            if (selectedCustomer != null) modifyCustomer(selectedCustomer);
        });
        //loc san pham trong tableview khi tuong tac voi searchfield
        searchField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            tableView.setItems(customerOutList.filtered(Customer -> {
                return Customer.getName().toLowerCase().contains(newValue.toLowerCase());
            }));
        }));
        //hien thi tat ca mat hang
        openCustomerOutList();
    }
    @FXML
    public void openCustomerOutList()
    {
        //clear searchfield
        searchField.clear();
        try
        {
            //connect voi sqlsever
            Connection connection = JDBCUtil.getConnection();
            //nhap truy van
            PreparedStatement preparedStatement = connection.prepareStatement("exec customers_out_list");
            //thuc hien truy van
            ResultSet resultSet = preparedStatement.executeQuery();
            //clear productlist de tranh lap lai
            customerOutList.clear();
            //lay cac ban ghi trong truy van sql
            while(resultSet.next())
            {
                customerOutList.add(new Customer(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        (resultSet.getString(5) == null || resultSet.getString(5).equals("")) ? "Không có email" : resultSet.getString(5)
                ));
            }
            //hien thi len tableview
            tableView.setItems(customerOutList);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //mo cua so phu de them mat hang moi
    @FXML
    public void addNewCustomerOut()
    {
        System.out.println("addnew");
        try
        {
            //load scence them mat hang
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/ThemKhachMuaMoiView.fxml"));
            Scene addNewCustomerOutScene = new Scene(fxmlLoader.load());
            //set controller cha cho scene moi
            ThemKhachMuaMoiController themKhachMuaMoiController = fxmlLoader.getController();
            themKhachMuaMoiController.setMainController(this);
            //tao stage moi
            Stage addNewProductStage = new Stage();
            //modal: phai dong cua so con neu muon thao tac cua so cha
            addNewProductStage.initModality(Modality.APPLICATION_MODAL);
            addNewProductStage.initOwner(addNewCustomerOutButton.getScene().getWindow());
            addNewProductStage.setTitle("Them khach hang moi");
            addNewProductStage.setScene(addNewCustomerOutScene);
            //show
            addNewProductStage.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //mo cua so phu de chinh sua mat hang
    public void modifyCustomer(Customer selectedCustomer)
    {
        System.out.println("modify");
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/ChinhSuaKhachHangView.fxml"));
            Scene modifyProductScene = new Scene(fxmlLoader.load());
            ChinhSuaKhachHangController chinhSuaKhachHangController = fxmlLoader.getController();
            chinhSuaKhachHangController.setMainController(this);
            chinhSuaKhachHangController.setSelectedCustomer(selectedCustomer);
            Stage modifyProductStage = new Stage();
            modifyProductStage.initModality(Modality.APPLICATION_MODAL);
            modifyProductStage.initOwner(tableView.getScene().getWindow());
            modifyProductStage.setTitle("Chinh sua khach hang: ");
            modifyProductStage.setScene(modifyProductScene);
            modifyProductStage.show();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
