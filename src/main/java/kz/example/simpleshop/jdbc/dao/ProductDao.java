package kz.example.simpleshop.jdbc.dao;

import com.sun.org.apache.xpath.internal.operations.Or;
import kz.example.simpleshop.dto.Order;
import kz.example.simpleshop.dto.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDao implements IProductDAO<Product, Order> {

    private final Connection connection;

    public ProductDao(Connection connection){

        this.connection = connection;
    }

    @Override
    public List<Product> getAllProduct() {
        Product product = new Product();
        List<Product> productList = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(SQLProduct.GET_ALL.QUERY)) {

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){

                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                long price = resultSet.getLong("price");
                int count = resultSet.getInt("count");
                product = new Product();
                product.setId(id);
                product.setName(name);
                product.setPrice(price);
                product.setCount(count);
                productList.add(product);
            }
        }catch (SQLException e){

            e.printStackTrace();
        }

        return productList;
    }

    @Override
    public Product getProductById(long id) {
        Product product = new Product();
        try(PreparedStatement statement = connection.prepareStatement(SQLProduct.GET_PRODUCT.QUERY)){
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String name = resultSet.getString("name");
                int count = resultSet.getInt("count");
                long price = resultSet.getLong("price");
                product.setId(id);
                product.setCount(count);
                product.setName(name);
                product.setPrice(price);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public List<Order> getOrdersById(long id) {
        Order order;
        List<Order> orderList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQLProduct.GET_ORDERS.QUERY)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                order = new Order();
                order.setId(resultSet.getLong("id"));
                order.setUserId(resultSet.getLong("userid"));
                order.setProductId(resultSet.getLong("productid"));
                order.setPrice(resultSet.getLong("price"));
                order.setCount(resultSet.getInt("count"));
                orderList.add(order);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return orderList;
    }

    @Override
    public int getProductCount(long id) {
        int productCount = -1;
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQLProduct.GET_PRODUCT_COUNT.QUERY)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
             productCount = resultSet.getInt("count");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return productCount;
    }

    @Override
    public boolean insert(Product product) {
        boolean result = false;
        try(PreparedStatement statement = connection.prepareStatement(SQLProduct.INSERT.QUERY)) {
            statement.setString(1,product.getName());
            statement.setLong(2,product.getPrice());
            statement.setInt(3,product.getCount());
            result = statement.execute();

        }catch (SQLException e){

            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean insertOrder(Order order) {
        boolean result = false;
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQLProduct.INSERT_ORDER.QUERY)) {
            preparedStatement.setLong(1, order.getUserId());
            preparedStatement.setLong(2,order.getProductId());
            preparedStatement.setLong(3,order.getPrice());
            preparedStatement.setInt(4,order.getCount());
            result = preparedStatement.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean updateProductCount(int count, long id) {
        boolean result = false;
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQLProduct.UPDATE_COUNT.QUERY)) {
            preparedStatement.setInt(1,count);
            preparedStatement.setLong(2,id);
            result = preparedStatement.execute();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }
}

enum SQLProduct {
    GET_ALL("SELECT * FROM product_table ORDER BY id"),
    INSERT("INSERT INTO product_table (id, name, price, count) VALUES (DEFAULT, ?, ?, ?)"),
    INSERT_ORDER("INSERT INTO order_table (id, userid, productid, price, count) VALUES(DEFAULT, ?, ?, ?, ?)"),
    GET_PRODUCT("SELECT * FROM product_table WHERE id = ?"),
    GET_ORDERS("SELECT * FROM order_table WHERE userid = ?"),
    GET_PRODUCT_COUNT("SELECT product_table.count FROM product_table WHERE id = ?"),
    UPDATE_COUNT("UPDATE product_table SET count = count - ? WHERE product_table.id = ?"),
    DELETE(""),
    UPDATE("");

    String QUERY;

    SQLProduct(String QUERY){
        this.QUERY = QUERY;
    }

}