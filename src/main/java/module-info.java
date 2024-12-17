module com.project.qlbh_kh {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    requires java.base;
    opens com.project.qlbh_kh.controllers to javafx.fxml; // Cho phép JavaFX truy cập vào package controllers
    opens com.project.qlbh_kh.views to javafx.fxml;      // Cho phép JavaFX truy cập vào package views
    opens com.project.qlbh_kh to javafx.fxml;            // Nếu cần mở package gốc cho FXML


    exports com.project.qlbh_kh;                          // Xuất package gốc
    exports com.project.qlbh_kh.controllers;              // Xuất package controllers nếu cần

    opens com.project.qlbh_kh.entity to javafx.fxml;
    exports com.project.qlbh_kh.entity;
}
