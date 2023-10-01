module ie.mtu.oopassignment {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.testng;
    requires junit;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens ie.mtu.oopassignment to javafx.fxml;
    exports ie.mtu.oopassignment.model;
    opens ie.mtu.oopassignment.model to javafx.fxml;
    exports ie.mtu.oopassignment.controller;
    opens ie.mtu.oopassignment.controller to javafx.fxml;
    exports ie.mtu.oopassignment;
    exports ie.mtu.oopassignment.testing;
    opens ie.mtu.oopassignment.testing to javafx.fxml;

}