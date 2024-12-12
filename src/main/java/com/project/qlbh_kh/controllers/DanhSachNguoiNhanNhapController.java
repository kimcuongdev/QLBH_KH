package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Receiver;
import com.project.qlbh_kh.utils.JDBCUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DanhSachNguoiNhanNhapController extends DanhSachNguoiNhanController implements Initializable {

    @Override
    public void loadReceiverList()
    {
        Receivers.clear();
        String sql = "exec receivers_in_list";
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/ThemNguoiNhanMoiInView.fxml"));
            Scene addNewReceiverScene = new Scene(fxmlLoader.load());
            //set controller cha cho scene moi
            ThemNguoiNhanMoiInController themNguoiNhanMoiInController = fxmlLoader.getController();
            themNguoiNhanMoiInController.setMainController(this);
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

}
