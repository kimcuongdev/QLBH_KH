package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Receiver;
import com.project.qlbh_kh.utils.JDBCUtil;
import javafx.beans.binding.Bindings;
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

public class QuanLyNguoiNhanOutController implements Initializable {
    @FXML
    protected TableView<Receiver> receiverList;
    @FXML
    protected ObservableList<Receiver> Receivers = FXCollections.observableArrayList();
    @FXML
    protected TableColumn<Receiver, String> addressColumn;
    @FXML
    protected TableColumn<Receiver, String> receiverNameColumn;
    @FXML
    protected TableColumn<Receiver, String> emailColumn;
    @FXML
    protected TableColumn<Receiver, Integer> countColumn;
    @FXML
    protected TextField receiverNameField;
    @FXML
    protected TableColumn<Receiver, String> phoneNumberColumn;
    @FXML
    protected Button addNewReceiver;
    @FXML private Button statisticThisMonthButton;
    @FXML private Button statisticThisYearButton;
    @FXML private Button statisticAllButton;
    protected BasicController mainController;
    public void setMainController(BasicController basicController) {
        this.mainController = basicController;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        receiverNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("total_order"));
        receiverList.setOnMouseClicked(mouseEvent -> {
            System.out.println("modify");
            Receiver selectedReceiver = receiverList.getSelectionModel().getSelectedItem();
            modifyReceiver(selectedReceiver);
        });
        statisticThisMonthButton.setOnMouseClicked(mouseEvent -> {
            loadReceiverList(1);
        });
        statisticThisYearButton.setOnMouseClicked(mouseEvent -> {
            loadReceiverList(2);
        });
        statisticAllButton.setOnMouseClicked(mouseEvent -> {
            loadReceiverList(3);
        });
        //load du lieu
        loadReceiverList(3);
        //cai dat bo loc tim kiem theo ten KH
        receiverNameField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            receiverList.setItems(Receivers.filtered(Receiver -> {
                String name = Receiver.getName();
                System.out.println(name);
                return name.toLowerCase().contains(newValue.toLowerCase());
            }));
        }));
        //chon nguoi nhan
        receiverList.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue != null && mainController != null)
            {
                mainController.setSelectedReceiver(newValue.getReceiver_id(),newValue.getName());
                ((Stage) receiverList.getScene().getWindow()).close();
            }
        }));
    }
    public void loadReceiverList(int statisticType)
    {
        Receivers.clear();
        String sql = "{call dbo.receivers_out_list(?)}";
        try
        {
            Connection connection = JDBCUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,statisticType);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                String phone_number = resultSet.getString(4);
                String email = resultSet.getString(5);
                int total_order = resultSet.getInt(6);
                Receivers.add(new Receiver(id,name,address,phone_number,email,total_order));
            }
            receiverList.setItems(Receivers);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void modifyReceiver(Receiver selectedReceiver) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/ChinhSuaNguoiNhanXuatView.fxml"));
            Scene scene = new Scene(loader.load());
            ChinhSuaNguoiNhanXuatController controller = loader.getController();
            controller.setSelectedReceiver(selectedReceiver);
            controller.setMainController(this);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNewReceiver()
    {
        System.out.println("addnew");
        try
        {
            //load scence them nguoi nhan
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/ThemNguoiNhanMoiOutView.fxml"));
            Scene addNewReceiverScene = new Scene(fxmlLoader.load());
            //set controller cha cho scene moi
            ThemNguoiNhanMoiOutController themNguoiNhanMoiOutController = fxmlLoader.getController();
            themNguoiNhanMoiOutController.setMainController(this);
            //tao stage moi
            Stage addNewReceiverStage = new Stage();
            //modal: phai dong cua so con neu muon thao tac cua so cha
            addNewReceiverStage.initModality(Modality.APPLICATION_MODAL);
            addNewReceiverStage.initOwner(addNewReceiver.getScene().getWindow());
            addNewReceiverStage.setTitle("Them mat hang moi");
            addNewReceiverStage.setScene(addNewReceiverScene);
            //show
            addNewReceiverStage.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
