package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Product;
import com.project.qlbh_kh.utils.JDBCUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class SanPhamPopUpXuat implements Initializable {

    @FXML
    private TextField productNameField; // Trường nhập tên mặt hàng

    @FXML
    private TextField productPriceField; // Trường nhập giá

    @FXML
    private TableView<Product> tableView; // Bảng hiển thị mặt hàng
    @FXML
    private TableColumn<Product, Integer> idColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;
    ObservableList<Product> products = FXCollections.observableArrayList(); // Danh sách mặt hàng

    private TaoHoaDonController mainController;
    private String type = "in";

    public void setType(String type) {
        this.type = type;
    }

    public void setMainController(TaoHoaDonController mainController) {
        this.mainController = mainController;
//        this.type =
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setPrefWidth(40);
        nameColumn.setPrefWidth(150);
        priceColumn.setPrefWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("prod_id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("prod_name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price_in"));

        loadProductName();

        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Kiểm tra xem có phải nhấp đúp không
                Product selectedProduct = tableView.getSelectionModel().getSelectedItem(); // Lấy sản phẩm được chọn
                if (selectedProduct != null) {
                    handleDoubleClick(selectedProduct);

                    // Close the current stage after selecting a product
                    Stage stage = (Stage) tableView.getScene().getWindow();
                    stage.close();
                }
            }
        });
    }

    // Method to load products from the database
    public void loadProductName() {

        String sql = "use BTL_QL_BanHang\n" +
                "select prod_id, prod_name," + "price_out" + "\n" +
                "from products_tb";
        try {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();
            products.clear();
            while (resultSet.next()) {
                products.add(new Product(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getDouble(3))
                );
                System.out.println(resultSet.getInt(1) + resultSet.getString(2) + resultSet.getDouble(3));
            }
            tableView.setItems(products);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDoubleClick(Product selectedProduct) {
        if (mainController != null) {
            mainController.addProductToTable(selectedProduct); // Gọi phương thức để thêm mặt hàng vào bảng trong HelloController
        }
    }

    // Handle adding a new product
    @FXML
    private void handleAddProduct() {
        String name = productNameField.getText();
        String priceText = productPriceField.getText();

        if (name.isEmpty() || priceText.isEmpty()) {
            System.out.println("Vui lòng nhập đủ thông tin.");
            return;
        }

        try {
            double price = Integer.parseInt(priceText); // Convert the price to integer
            Product newProduct = new Product(name, price);

            // Insert the new product into the database
            String sql = "INSERT INTO products_tb (prod_name, price_in) VALUES (?, ?)"; // Assuming price_in is used for input price
            try (Connection connection = JDBCUtil.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(sql)) {

                stmt.setString(1, name); // Set the product name
                stmt.setDouble(2, price);   // Set the price
                int rowsAffected = stmt.executeUpdate(); // Execute the insert query

                if (rowsAffected > 0) {
                    System.out.println("Sản phẩm mới đã được thêm vào cơ sở dữ liệu.");
                    // After successful insertion, add the product to the local list and tableView
                    products.add(newProduct); // Add product to the local ObservableList
                    tableView.setItems(products); // Update the TableView
                } else {
                    System.out.println("Lỗi khi thêm sản phẩm.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Lỗi khi kết nối và thêm sản phẩm vào cơ sở dữ liệu.");
            }

            // Clear input fields after adding
            productNameField.clear();
            productPriceField.clear();

        } catch (NumberFormatException e) {
            System.out.println("Giá phải là một số hợp lệ.");
        }
    }
}
