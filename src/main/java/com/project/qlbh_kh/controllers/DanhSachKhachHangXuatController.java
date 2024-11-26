package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Customer;
import com.project.qlbh_kh.utils.JDBCUtil;
import javafx.fxml.Initializable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DanhSachKhachHangXuatController extends DanhSachKhachHangController implements Initializable {
    @Override
    public void loadCustomerList()
    {
        String sql = "exec customers_out_list";
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
                customers.add(new Customer(id,name,address,phone_number));
            }
            customerList.setItems(customers);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
