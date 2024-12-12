package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Receiver;
import com.project.qlbh_kh.utils.JDBCUtil;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class DanhSachNguoiNhanXuatController extends DanhSachNguoiNhanController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url,resourceBundle);
        countColumn.setCellValueFactory(cellData -> {
            return Bindings.createIntegerBinding(
                    () -> {
                        try {
                            Receiver receiver = cellData.getValue();
                            Connection connection = JDBCUtil.getConnection();
                            String sql = "SELECT COUNT(*)\n" +
                                    "FROM order_out_tb\n" +
                                    "WHERE receiver_out_id =" + receiver.getReceiver_id() + ";";
                            System.out.println("id ng nhan" + receiver.getReceiver_id());
                            PreparedStatement statement = connection.prepareStatement(sql);
                            ResultSet resultSet = statement.executeQuery();
                            if (resultSet.next()) {
                                int orderCount = resultSet.getInt(1);
                                System.out.println("order count" + orderCount);
                                return orderCount;
                            }
                        } catch (Exception e) {
                            return 0;
                        }
                        return null;
                    }
            ).asObject();
        });
//        loadReceiverList();
    }
    @Override
    public void loadReceiverList()
    {
        Receivers.clear();
        String sql = "exec receivers_out_list";
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
                String email = resultSet.getString(5);
                if(email == null)
                    email = "";
                Receivers.add(new Receiver(id,name,address,phone_number,email));
            }
            receiverList.setItems(Receivers);
        } catch (Exception e)
        {
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
    @FXML
    public void refreshTableView() {
        Receivers.clear();
        loadReceiverList();
    }
    @Override
    public void modifyReceiver(Receiver selectedReceiver) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/ChinhSuaNguoiNhanXuat.fxml"));
            Scene scene = new Scene(loader.load());
            ChinhSuaNguoiNhanXuatController controller = loader.getController();
            controller.setSelectedReceiver(selectedReceiver);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
