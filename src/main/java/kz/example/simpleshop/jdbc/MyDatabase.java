package kz.example.simpleshop.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    private static MyDatabase instance;
    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/simple_shop";
    private static final String USER = "postgres";
    private static final String PASS = "741852963v";
    private static Connection connection;
    private MyDatabase(){
        try {
            connection = DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static synchronized MyDatabase getInstance(){
        if(instance == null){
            instance = new MyDatabase();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
