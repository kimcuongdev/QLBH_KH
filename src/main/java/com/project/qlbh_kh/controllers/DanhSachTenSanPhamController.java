package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Product;
import com.project.qlbh_kh.utils.JDBCUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class DanhSachTenSanPhamController implements Initializable {

    @FXML
    private TextField productNameField;

    @FXML
    private ListView<Product> productList;
    ObservableList<Product> products = FXCollections.observableArrayList();

    @FXML
    void filterProductName(ActionEvent event) {
        System.out.println(productNameField.getText());
    }
    //ham set controller cha
    private BasicController mainController;
    public void setMainController(BasicController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //load danh sach ten mat hang
        loadProductName();
        //filter theo ten mat hang
        productNameField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            productList.setItems(products.filtered(product -> product.getProd_name().toLowerCase().contains(newValue.toLowerCase())));
        }));
        //chon mat hang
        productList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedProduct) -> {
            if (selectedProduct != null && mainController != null) {
                mainController.setSelectedProductId(selectedProduct.getProd_id(), selectedProduct.getProd_name()); // truyen mat hang da chon ve cho controller cha
                ((Stage) productList.getScene().getWindow()).close(); // dong cua so
            }
        });
    }
    //ham load danh sach ten mat hang
    public void loadProductName()
    {
        String sql = "use BTL_QL_BanHang\n" +
                "select prod_id, prod_name\n" +
                "from products_tb";
        try{
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) products.add(new Product(resultSet.getInt(1),
                    resultSet.getString(2)));
            productList.setItems(products);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
