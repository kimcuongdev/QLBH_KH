package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Product;
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

public class SanPhamPopUpNhap implements Initializable {

    @FXML private TextField searchField;
    @FXML private TableView<Product> tableView; //tableView lưu product
    @FXML private TableColumn<Product,String> productNameColumn; //tableColumn luu thuoc tinh productName co kieu du lieu la String
    @FXML private TableColumn<Product,Double> priceInColumn;
    @FXML private TableColumn<Product,Double> priceOutColumn;
    @FXML private TableColumn<Product,String> unitPriceColumn;
    @FXML private Button addNewProductButton;
    @FXML private Button reloadTableButton;
    private ObservableList<Product> productList = FXCollections.observableArrayList(); //observablelist de luu nhung doi tuong duoc hien thi trong tableview
    private TaoHoaDonController mainController;


    public void setMainController(TaoHoaDonController mainController) {
        this.mainController = mainController;
    }

    @Override
    //phuong thuong initialize se duoc goi de set up cac doi tuong trong scene truoc khi hien thi tren man hinh
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //thiet lap thuoc tinh cho cac cot, dung ten thuoc tinh cua lop product
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("prod_name"));
        priceInColumn.setCellValueFactory(new PropertyValueFactory<>("price_in"));
        priceOutColumn.setCellValueFactory(new PropertyValueFactory<>("price_out"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unit_sold"));
        //loc san pham trong tableview khi tuong tac voi searchfield
        searchField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            tableView.setItems(productList.filtered(Product -> {
                return Product.getProd_name().toLowerCase().contains(newValue);
            }));
        }));
        tableView.setOnMouseClicked(mouseEvent -> {
            Product selectedProduct = tableView.getSelectionModel().getSelectedItem();
            if(selectedProduct != null)
                mainController.addProductToTable(selectedProduct);
            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.close();
        });
        //hien thi tat ca mat hang
        openProductList();

    }
    @FXML
    public void openProductList()
    {
        //clear searchfield
        searchField.clear();
        try
        {
            //connect voi sqlsever
            Connection connection = JDBCUtil.getConnection();
            //nhap truy van
            PreparedStatement preparedStatement = connection.prepareStatement("exec openProductList");
            //thuc hien truy van
            ResultSet resultSet = preparedStatement.executeQuery();
            //clear productlist de tranh lap lai
            productList.clear();
            //lay cac ban ghi trong truy van sql
            while(resultSet.next())
            {
                productList.add(new Product(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getDouble(3),
                        resultSet.getDouble(4),
                        resultSet.getString(5)
                ));
            }
            //hien thi len tableview
            tableView.setItems(productList);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //mo cua so phu de them mat hang moi
    @FXML
    public void addNewProduct()
    {
        System.out.println("addnew");
        try
        {
            //load scence them mat hang
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/ThemMatHangMoiView.fxml"));
            Scene addNewProductScene = new Scene(fxmlLoader.load());
            //tao stage moi
            Stage addNewProductStage = new Stage();
            //modal: phai dong cua so con neu muon thao tac cua so cha
            addNewProductStage.initModality(Modality.APPLICATION_MODAL);
            addNewProductStage.initOwner(addNewProductButton.getScene().getWindow());
            addNewProductStage.setTitle("Them mat hang moi");
            addNewProductStage.setScene(addNewProductScene);
            //show
            addNewProductStage.showAndWait();
            //reload lai tableview sau khi them mat hang moi
            openProductList();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}