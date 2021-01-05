package kz.example.simpleshop.controller;

import kz.example.simpleshop.dto.Order;
import kz.example.simpleshop.dto.Product;
import kz.example.simpleshop.jdbc.dao.ProductDao;
import kz.example.simpleshop.jdbc.dao.UserDao;

import java.sql.Connection;
import java.util.List;

public class ProductController {
    private ProductDao productDao;
    private UserDao userDao;
    public ProductController(Connection connection){

        this.productDao = new ProductDao(connection);
        this.userDao = new UserDao(connection);
    }

    public List<Product> getAllProduct(){
        return productDao.getAllProduct();
    }


    public ProductControllerResponse buyProduct(long userId, long productId, int count){
        Order order = new Order();
        Product product = productDao.getProductById(productId);
        long price = product.getPrice() * count;
        long userBalance = userDao.getUserBalance(userId);
        int productCount = productDao.getProductCount(productId);
        if (userBalance >= price){
            if (productCount >= count){
                userDao.changeUserBalance(price, userId);
                order.setUserId(userId);
                order.setProductId(productId);
                order.setPrice(price);
                order.setCount(count);
                productDao.insertOrder(order);
                productDao.updateProductCount(count,productId);
                return ProductControllerResponse.PRODUCT_BUY_SUCCESSFUL;
            }else {
                return ProductControllerResponse.PRODUCT_BUY_ERROR_COUNT;
            }
        }else{
            return ProductControllerResponse.PRODUCT_BUY_ERROR_BALANCE;
        }
    }

    public List<Order> getOrdersByUserId(long id){

        List<Order> orderList = productDao.getOrdersById(id);
        for (Order orderItem :
                orderList) {
            String name = productDao.getProductById(orderItem.getProductId()).getName();
            orderItem.setName(name);
        }
        return orderList;
    }
}
