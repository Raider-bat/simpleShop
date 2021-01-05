package kz.example.simpleshop.jdbc.dao;

import java.util.List;

public interface IProductDAO<Entity, Model> {
    List<Entity> getAllProduct();
    Entity getProductById(long id);
    List<Model> getOrdersById(long id);
    int getProductCount(long id);
    boolean insert(Entity model);
    boolean insertOrder(Model model);
    boolean updateProductCount(int count, long id);
}
