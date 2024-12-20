package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Order_manager;
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

public class TruyXuatHoaDonController extends BasicController {
    @FXML private TableView<Order_manager> tableView;
    @FXML private TableColumn<Order_manager,Integer> idColumn;
    @FXML private TableColumn<Order_manager,String> customerNameColumn;
    @FXML private TableColumn<Order_manager,Double> totalPaymentColumn;
    @FXML private TableColumn<Order_manager,String> receiverNameColumn;
    @FXML private TableColumn<Order_manager,String> dateColumn;
    @FXML private TableColumn<Order_manager,String> operationColumn;
    @FXML private TableColumn<Order_manager,String> statusColumn;
    ObservableList<Order_manager> data = FXCollections.observableArrayList();
    @FXML public void resetCustomer() {this.customerNameField.clear(); this.selectedCustomerId = 0;}
    @FXML public void resetFromDate() {this.fromDate.setValue(null); this.fromDateValue = null;}
    @FXML public void resetToDate() {this.toDate.setValue(null); this.toDateValue = null;}
    @FXML public void resetReceiver() {this.receiverNameField.clear(); this.selectedReceiverId = 0;}
    @FXML public void resetOperation() {this.operationBox.setValue(null); this.operation = null;}
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        super.initialize(url,resourceBundle);
        customerNameField.setDisable(true);
        receiverNameField.setDisable(true);
        ObservableList<String> operations = FXCollections.observableArrayList("Nhập", "Xuất", "Cả Hai");
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
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
        totalPaymentColumn.setCellValueFactory(new PropertyValueFactory<>("total_payment"));
        receiverNameColumn.setCellValueFactory(new PropertyValueFactory<>("receiver_name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        operationColumn.setCellValueFactory(new PropertyValueFactory<>("operation"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        try
        {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("{call dbo.Order_all}");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                data.add(new Order_manager(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getDouble(3),
                        resultSet.getString(4),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        (resultSet.getInt(8) == 0) ? "Chưa thanh toán" : "Đã thanh toán"
                ));
            }
            tableView.setItems(data);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        //chon hoa don
        tableView.setOnMouseClicked(mouseEvent -> {
            Order_manager selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem.getOperation().equals("Nhập"))
            {
                showOrder(selectedItem.getId(),"in");
                System.out.println("after show");
                System.out.println(updated);
                if (updated) executeQuery();
                updated = false;
            }
            else
            {
                showOrder(selectedItem.getId(),"out");
                System.out.println("after show");
                System.out.println(updated);
                if (updated) executeQuery();
                updated = false;
            }
//            System.out.println(updated);
//            if (updated) executeQuery();
//            updated = false;
        });

    }
    @FXML
    public void executeQuery() {
        if (fromDate != null) System.out.println("From Date: " + fromDateValue);
        if (toDate != null) System.out.println("To Date: " + toDateValue);
        if (operation != null) System.out.println("Operation " + operation);
        System.out.println(selectedCustomerId);
        System.out.println(selectedReceiverId);
        if (operation == null || operation.equals("both"))
        {
            try
            {
                Connection connection = JDBCUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("{call dbo.Order_all(?,?,?,?)}");
                preparedStatement.setInt(1,selectedCustomerId);
                preparedStatement.setInt(2,selectedReceiverId);
                preparedStatement.setDate(3,fromDateValue == null ? null : Date.valueOf(fromDateValue));
                preparedStatement.setDate(4,toDateValue == null ? null : Date.valueOf(toDateValue));
                ResultSet resultSet = preparedStatement.executeQuery();
                data.clear();
                while(resultSet.next())
                {
                    data.add(new Order_manager(
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getDouble(3),
                            resultSet.getString(4),
                            resultSet.getString(6),
                            resultSet.getString(7),
                            (resultSet.getInt(8) == 0) ? "Chưa thanh toán" : "Đã thanh toán"
                    ));
                }
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
                PreparedStatement preparedStatement = connection.prepareStatement("{call dbo.Order_in(?,?,?,?)}");
                preparedStatement.setInt(1,selectedCustomerId);
                preparedStatement.setInt(2,selectedReceiverId);
                preparedStatement.setDate(3,fromDateValue == null ? null : Date.valueOf(fromDateValue));
                preparedStatement.setDate(4,toDateValue == null ? null : Date.valueOf(toDateValue));
                ResultSet resultSet = preparedStatement.executeQuery();
                data.clear();
                while(resultSet.next())
                {
                    data.add(new Order_manager(
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getDouble(3),
                            resultSet.getString(4),
                            resultSet.getString(6),
                            resultSet.getString(7),
                            (resultSet.getInt(8) == 0) ? "Chưa thanh toán" : "Đã thanh toán"
                    ));
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (operation.equals("out"))
        {
            try
            {
                Connection connection = JDBCUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("{call dbo.Order_out(?,?,?,?)}");
                preparedStatement.setInt(1,selectedCustomerId);
                preparedStatement.setInt(2,selectedReceiverId);
                preparedStatement.setDate(3,fromDateValue == null ? null : Date.valueOf(fromDateValue));
                preparedStatement.setDate(4,toDateValue == null ? null : Date.valueOf(toDateValue));
                ResultSet resultSet = preparedStatement.executeQuery();
                data.clear();
                while(resultSet.next())
                {
                    data.add(new Order_manager(
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getDouble(3),
                            resultSet.getString(4),
                            resultSet.getString(6),
                            resultSet.getString(7),
                            (resultSet.getInt(8) == 0) ? "Chưa thanh toán" : "Đã thanh toán"
                    ));
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
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
        else if (operation.equals("both"))
        {
            System.out.println("both handle");
            try {
                //load view cho danh sach khach hang
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/danhSachKhachHangView.fxml"));
                Scene customerInListScene = new Scene(fxmlLoader.load());
                //set controller cha cho controller cua danh sach ten mat hang
                DanhSachKhachHangController controller = fxmlLoader.getController();
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
                Scene receiverInListScene = new Scene(fxmlLoader.load());
                //set controller cha cho controller cua danh sach ten mat hang
                DanhSachNguoiNhanNhapController controller = fxmlLoader.getController();
                controller.setMainController(this);
                //tao stage moi
                Stage receiverListStage = new Stage();
                receiverListStage.initModality(Modality.APPLICATION_MODAL);
                receiverListStage.initOwner(receiverNameField.getScene().getWindow());
                receiverListStage.setTitle("Danh sách người nhận");
                receiverListStage.setScene(receiverInListScene);
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
                Scene receiverOutListScene = new Scene(fxmlLoader.load());
                //set controller cha cho controller cua danh sach ten mat hang
                DanhSachNguoiNhanXuatController controller = fxmlLoader.getController();
                controller.setMainController(this);
                //tao stage moi
                Stage receiverListStage = new Stage();
                receiverListStage.initModality(Modality.APPLICATION_MODAL);
                receiverListStage.initOwner(receiverNameField.getScene().getWindow());
                receiverListStage.setTitle("Danh sách người nhận");
                receiverListStage.setScene(receiverOutListScene);
                //show
                receiverListStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (operation.equals("both"))
        {
            System.out.println("both handle");
            try {
                //load view cho danh sach khach hang
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/danhSachNguoiNhanView.fxml"));
                Scene receiverInListScene = new Scene(fxmlLoader.load());
                //set controller cha cho controller cua danh sach ten mat hang
                DanhSachNguoiNhanController controller = fxmlLoader.getController();
                controller.setMainController(this);
                //tao stage moi
                Stage receiverListStage = new Stage();
                receiverListStage.initModality(Modality.APPLICATION_MODAL);
                receiverListStage.initOwner(receiverNameField.getScene().getWindow());
                receiverListStage.setTitle("Danh sách người nhận");
                receiverListStage.setScene(receiverInListScene);
                //show
                receiverListStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
