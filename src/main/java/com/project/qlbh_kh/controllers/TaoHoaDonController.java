package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Product;
import com.project.qlbh_kh.utils.JDBCUtil;
import javafx.beans.binding.Bindings;
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
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class TaoHoaDonController extends BasicController {
    ObservableList<Product> productList = FXCollections.observableArrayList();;
    @FXML public TextField phoneNumberField;
    @FXML public TextField addressField;
    @FXML public Button addCustomerButton;
    @FXML public Button addProductButton;
    @FXML public Button saveButton;
    @FXML public Button addReceiverButton;

    @FXML public TableView<Product> tableView;
    @FXML public TableColumn<Product,String> productNameColumn;
    @FXML public TableColumn<Product,Double> unitPriceColumn;
    @FXML public TableColumn<Product,Double> totalAmountColumn;
    @FXML public TableColumn<Product,Integer> quantityColumn;
    @FXML
    public TextField tongTienTextField;
    @FXML
    public ComboBox<String> statusBox;
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
        operationBox.setValue("Nhập");
        operationBox.valueProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (oldValue == null || !oldValue.equals(newValue))
            {
                customerNameField.clear();
                selectedCustomerId = 0;
                receiverNameField.clear();
                selectedReceiverId = 0;
                productList.clear();
                tableView.refresh();
                tongTienTextField.clear();
                phoneNumberField.clear();
                addressField.clear();

            }

        }));
        operationBox.valueProperty().addListener(((observableValue, oldValue, newValue) -> {
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

        ObservableList<String> status = FXCollections.observableArrayList("Chưa thanh toán","Đã thanh toán" );
        statusBox.setItems(status);
        statusBox.setValue("Chưa thanh toán");

        tableView.setEditable(true);

        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("prod_name"));

        // Thiết lập cho cột giá
        this.operation = "in";
        if(operation.equals("in"))
            unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price_in"));
        else if(operation.equals("out"))
            unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price_out"));

        // Thiết lập cho cột số lượng
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Product, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Product, Integer> productIntegerCellEditEvent) {
                Product product = productIntegerCellEditEvent.getRowValue();
                product.setQuantity(productIntegerCellEditEvent.getNewValue());
                for(Product product1: productList){
                    if(product1.getProd_id() == product.getProd_id()){
                        product1.setQuantity(productIntegerCellEditEvent.getNewValue());
                        break;
                    }
                }
                tableView.refresh();
                tinhTongTien();
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

    @FXML
    public void openReceiverList()
    {
        System.out.println("open receiver list");
        if (operation == null)
        {
            System.out.println("null handle");
        }
        else if (operation.equals("in"))
        {
            System.out.println("in handle");
            try {
                //load view cho danh sach khach hang
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/danhSachNguoiNhanNhapView.fxml"));
                Scene customerInListScene = new Scene(fxmlLoader.load());
                //set controller cha cho controller cua danh sach ten mat hang
                DanhSachNguoiNhanNhapController controller = fxmlLoader.getController();
                controller.setMainController(this);
                //tao stage moi
                Stage receiverListStage = new Stage();
                receiverListStage.initModality(Modality.APPLICATION_MODAL);
                receiverListStage.initOwner(receiverNameField.getScene().getWindow());
                receiverListStage.setTitle("Danh sách khách hàng nhập");
                receiverListStage.setScene(customerInListScene);
                //show
                receiverListStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (operation.equals("out"))
        {
            System.out.println("out handle");
            try {
                //load view cho danh sach khach hang
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/danhSachNguoiNhanXuatView.fxml"));
                Scene customerInListScene = new Scene(fxmlLoader.load());
                //set controller cha cho controller cua danh sach ten mat hang
                DanhSachNguoiNhanXuatController controller = fxmlLoader.getController();
                controller.setMainController(this);
                //tao stage moi
                Stage receiverListStage = new Stage();
                receiverListStage.initModality(Modality.APPLICATION_MODAL);
                receiverListStage.initOwner(receiverNameField.getScene().getWindow());
                receiverListStage.setTitle("Danh sách khách hàng xuất");
                receiverListStage.setScene(customerInListScene);
                //show
                receiverListStage.show();

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
            product.setQuantity(1);
            productList.add(product);
            tinhTongTien();
        }
        tableView.setItems(productList);
    }
    //
    @FXML
    private void addProduct() {
        if(operation.equals("in")){
            String fxmlPath = "/com/project/qlbh_kh/views/product_popup_in.fxml";
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                if (loader.getLocation() == null) {
                    System.out.println("FXML file not found: " + fxmlPath);
                    return;
                }

                Scene productInList = new Scene(loader.load());
                Stage popupStage = new Stage();
                SanPhamPopUpNhap controller = loader.getController();
                controller.setMainController(this);
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.initOwner(tableView.getScene().getWindow());
                popupStage.setTitle("Chọn Mặt Hàng");
                popupStage.setScene(productInList);
                popupStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void tinhTongTien() {
        Double tongTien = productList.stream()
                .mapToDouble(product -> product.getPrice_in() * product.getQuantity())
                .sum();
        tongTienTextField.setText(String.valueOf(tongTien));
    }
    public void setCustomerInformation(int customerID)
    {
//        System.out.println("tao dang setting " + customerID );
        String sql = "exec customers_list";

        try
        {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
//                System.out.println("dang lay sql");
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                String phone_number = resultSet.getString(4);
                if(id == customerID){
                    phoneNumberField.setText(phone_number);
                    addressField.setText(address);
//                    System.out.println("da lay dc khach hang");
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void luuHoaDon(){
        if (fromDate != null) System.out.println("Bill Date: " + fromDateValue);
        if (selectedCustomerId != 0) System.out.println("Customer in ID " + selectedCustomerId);
        if (selectedReceiverId != 0) System.out.println("receiver Name: "+ selectedReceiverId);
        int newOrderId = 0;
        if(operation.equals("in")){
            try
            {
                Connection connection = JDBCUtil.getConnection();
                // Gọi stored procedure
                String sql = "{CALL dbo.TaoHD_in(?, ?, ?, ?)}";  // Cú pháp gọi procedure

                try (CallableStatement stmt = connection.prepareCall(sql)) {
                    // Set các input parameters
                    stmt.setInt(1, selectedCustomerId); // @customer_id
                    stmt.setInt(2, selectedReceiverId);  // @receiver_id
                    stmt.setDate(3, fromDateValue == null ? null : Date.valueOf(fromDateValue));   // @bill_date

                    // Đăng ký output parameter
                    stmt.registerOutParameter(4, Types.INTEGER);  // @new_order_id (output)

                    // Thực thi stored procedure
                    stmt.execute();

                    // Lấy giá trị của OUTPUT parameter
                    newOrderId = stmt.getInt(4);  // Lấy giá trị của @new_order_id

                    int confirmedStatus = statusBox.getSelectionModel().getSelectedIndex();
                    String update =
                            "update order_in_tb set status = (?) where order_in_id = (?)" ;
                    PreparedStatement preparedStatement = connection.prepareStatement(update);
                    preparedStatement.setInt(1, confirmedStatus);
                    preparedStatement.setInt(2, newOrderId);
                    preparedStatement.executeUpdate();

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            } catch (Exception e)
            {
                e.printStackTrace();
            }
//            String updateStatus = "update order_in_tb set status = (?) where order_in_id = (?)"


            Connection connection = JDBCUtil.getConnection();
            for (Product product : productList){
                System.out.println(newOrderId +" "+  product.getProd_id() + " " + product.getQuantity());
                try{
                    String sql = "{call dbo.TaoHD_in_detail(?,?,?)}";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    // Set các input parameters
                    stmt.setInt(1, newOrderId);
                    stmt.setInt(2, product.getProd_id());
                    stmt.setInt(3, product.getQuantity());
                    stmt.execute();
                }
                catch (Exception e){
                    System.out.println("Them san pham loi "  + e);
                }
            }
            System.out.println("Tao Hoa Don Thanh cong");
        }
        else {
            try
            {
                Connection connection = JDBCUtil.getConnection();
                // Gọi stored procedure
                String sql = "{CALL dbo.TaoHD_out(?, ?, ?, ?)}";  // Cú pháp gọi procedure

                try (CallableStatement stmt = connection.prepareCall(sql)) {
                    // Set các input parameters
                    stmt.setInt(1, selectedCustomerId); // @customer_id
                    stmt.setInt(2, selectedReceiverId);  // @receiver_id
                    stmt.setDate(3, fromDateValue == null ? null : Date.valueOf(fromDateValue));   // @bill_date

                    // Đăng ký output parameter
                    stmt.registerOutParameter(4, Types.INTEGER);  // @new_order_id (output)

                    // Thực thi stored procedure
                    stmt.execute();

                    // Lấy giá trị của OUTPUT parameter
                    newOrderId = stmt.getInt(4);  // Lấy giá trị của @new_order_id

                    // In kết quả
//                System.out.println("New Order ID: " + newOrderId);

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            } catch (Exception e)
            {
                e.printStackTrace();
            }
            Connection connection = JDBCUtil.getConnection();

            for (Product product : productList){
                System.out.println(newOrderId +" "+  product.getProd_id() + " " + product.getQuantity());
                try{
                    String sql = "{call dbo.TaoHD_out_detail(?,?,?)}";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    // Set các input parameters
                    stmt.setInt(1, newOrderId);
                    stmt.setInt(2, product.getProd_id());
                    stmt.setInt(3, product.getQuantity());
                    stmt.execute();
                }
                catch (Exception e){
                    System.out.println("Them san pham loi "  + e);
                }
            }
            System.out.println("Tao Hoa Don Thanh cong");
        }
    }
    @FXML
    public void addNewCustomer()
    {
        if(operation.equals("in")){

        }

        System.out.println("addnew");
        try
        {
            //load scence them mat hang
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/ThemNhaCungCapMoiView.fxml"));
            if(operation.equals("out")){
                fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/ThemKhachMuaMoiView.fxml"));
            }
            Scene addNewCustomerInScene = new Scene(fxmlLoader.load());
            //set controller cha cho scene moi

            //tao stage moi
            Stage addNewProductStage = new Stage();
            //modal: phai dong cua so con neu muon thao tac cua so cha
            addNewProductStage.initModality(Modality.APPLICATION_MODAL);
            addNewProductStage.initOwner(addCustomerButton.getScene().getWindow());
            addNewProductStage.setTitle("Them nha cung cap moi");
            addNewProductStage.setScene(addNewCustomerInScene);
            //show
            addNewProductStage.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @FXML
    public void addReceiver(){
        System.out.println("addnew");
        try
        {
            //load scence them mat hang
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/ThemNguoiNhanMoiInView.fxml"));
            if(operation.equals("out")){
                fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/ThemNguoiNhanMoiOutView.fxml"));
            }
            Scene addNewCustomerInScene = new Scene(fxmlLoader.load());
            //set controller cha cho scene moi

            //tao stage moi
            Stage addNewProductStage = new Stage();
            //modal: phai dong cua so con neu muon thao tac cua so cha
            addNewProductStage.initModality(Modality.APPLICATION_MODAL);
            addNewProductStage.initOwner(addReceiverButton.getScene().getWindow());
            addNewProductStage.setTitle("Them nha cung cap moi");
            addNewProductStage.setScene(addNewCustomerInScene);
            //show
            addNewProductStage.show();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


}