package kz.example.simpleshop.jdbc.dao;

import java.util.List;

public interface IUserDAO<Entity> {
    boolean insert(Entity model);
    List<Entity> getAllUsers();
    boolean update(Entity model);
    Entity getUserByLogin(String login);
    long getUserBalance(long id);
    boolean delete(Entity model);
    boolean changeUserBalance(long balance, long userId);
}