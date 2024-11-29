package com.project.qlbh_kh.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class BasicController implements Initializable {
    @FXML
    protected Button execute;

    @FXML
    protected DatePicker fromDate;
    protected LocalDate fromDateValue;

    @FXML
    protected ComboBox<String> operationBox;
    protected String operation;


    @FXML
    protected ComboBox<String> orderTypeBox;
    protected String orderType;

    @FXML
    protected TextField receiverField;

    @FXML
    protected TextField productField;
    protected String selectedProductName;
    protected int selectedProductId;

    @FXML
    protected DatePicker toDate;
    protected LocalDate toDateValue;

    @FXML
    protected TextField customerNameField;
    protected int selectedCustomerId;

    @FXML
    protected  TextField receiverNameField;
    protected int selectedReceiverId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // ComboBox
        //operationBox
        if (operationBox != null) {
            // Thực hiện các thao tác với operationBox
            ObservableList<String> operations = FXCollections.observableArrayList("Nhập", "Xuất", "Cả Hai");
            operationBox.setItems(operations);
            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if ("Nhập".equals(operationBox.getValue())) {
                        operation = "in";
                    } else if ("Xuất".equals(operationBox.getValue())) {
                        operation = "out";
                    } else{
                        operation = "both";
                    }
                }
            };
            operationBox.setOnAction(event);
        }
        //DatePicker
        //Format Date
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        fromDate.setConverter(converter);
        toDate.setConverter(converter);
    }
    @FXML
    public void enterFromDate() {
        fromDateValue = fromDate.getValue();
    }
    @FXML
    public void enterToDate() {
        toDateValue = toDate.getValue();
    }
    @FXML
    public void openProductNameList() {
        try {
            //load view cho danh sach ten mat hang
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/danhSachTenSanPhamView.fxml"));
            Scene productListScene = new Scene(fxmlLoader.load());
            //set controller cha cho controller cua danh sach ten mat hang
            DanhSachTenSanPhamController controller = fxmlLoader.getController();
            controller.setMainController(this);
            //tao stage moi
            Stage productListStage = new Stage();
            productListStage.initModality(Modality.APPLICATION_MODAL);
            productListStage.initOwner(productField.getScene().getWindow());
            productListStage.setTitle("Danh sách tên sản phẩm");
            productListStage.setScene(productListScene);
            //show
            productListStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //ham chon ten mat hang
    public void setSelectedProductName(String productName) {
        this.selectedProductName = productName;
        productField.setText(productName); // Optional: display selected name in the text field
    }
    public void setSelectedProductId(int productId, String producName)
    {
        this.selectedProductId = productId;
        productField.setText(producName);
    }
    public void setSelectedCustomer(int customerId, String customerName) {
        this.selectedCustomerId = customerId;
        customerNameField.setText(customerName); // Optional: display selected name in the text field
    }
    public void setSelectedReceiver(int receiverId, String receiverName)
    {
        this.selectedReceiverId = receiverId;
        receiverNameField.setText(receiverName);
    }
    public void showOrder(int id, String operation)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/HienThiHoaDonView.fxml"));
            Parent root = fxmlLoader.load();
            HienThiHoaDonController hienThiHoaDonController = fxmlLoader.getController();
            hienThiHoaDonController.setUpData(id,operation);
            Stage orderStage = new Stage();
            orderStage.initModality(Modality.APPLICATION_MODAL);
            //orderStage.initOwner();
            orderStage.setTitle("Hien thi hoa don");
            orderStage.setScene(new Scene(root));
            orderStage.show();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
