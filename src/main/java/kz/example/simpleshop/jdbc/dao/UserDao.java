package kz.example.simpleshop.jdbc.dao;

import com.sun.istack.internal.NotNull;
import kz.example.simpleshop.dto.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements IUserDAO<User> {

    @NotNull
    private final Connection connection;

    public UserDao(final Connection connection){

        this.connection = connection;
    }

    @Override
    public boolean insert(User user) {
        boolean result = false;
        int userCreatedId = 0;
        try(PreparedStatement statement = connection.prepareStatement(SQLUser.INSERT_USER.QUERY)) {
            statement.setString(1,user.getLogin());
            statement.setString(2,user.getPassword());
            ResultSet resultset = statement.executeQuery();
            while (resultset.next()){
                 userCreatedId = resultset.getInt("id");
            }
            PreparedStatement statementBalance = connection.prepareStatement(SQLUser.INSERT_BALANCE_USER.QUERY);
            System.out.println("Ваш ID: " +  userCreatedId);
            statementBalance.setLong(1, userCreatedId);
            statementBalance.setLong(2,1000000);
            result = statementBalance.execute();
        }catch (SQLException e){

            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        User user;
        try(PreparedStatement statement = connection.prepareStatement(SQLUser.GET_ALL.QUERY)) {

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){

                long id = resultSet.getLong("id");
                String name = resultSet.getString("login");
                String password = resultSet.getString("password");
                long balance = resultSet.getLong("balance");
                user = new User();
                user.setLogin(name);
                user.setPassword(password);
                user.setId(id);
                user.setBalance(balance);
                userList.add(user);
            }
        }catch (SQLException e){

            e.printStackTrace();
        }

        return userList;
    }

    @Override
    public boolean update(User model) {
        return false;
    }

    @Override
    public User getUserByLogin(String login) {
        User user = new User();
        try(PreparedStatement statement = connection.prepareStatement(SQLUser.GET_USER.QUERY)){
            statement.setString(1,login);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){

                long id = resultSet.getLong("id");
                String name = resultSet.getString("login");
                String password = resultSet.getString("password");
                long balance = resultSet.getLong("balance");
                user.setId(id);
                user.setLogin(name);
                user.setPassword(password);
                user.setBalance(balance);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public long getUserBalance(long id) {
        long balance = -1L;
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQLUser.GET_USER_BALANCE.QUERY)){
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
               balance = resultSet.getLong("balance");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return balance;
    }

    @Override
    public boolean delete(User model) {
        return false;
    }

    @Override
    public boolean changeUserBalance(long balance, long userId) {
        boolean result = false;
        try(PreparedStatement preparedStatement = connection.prepareStatement(SQLUser.UPDATE_USER_BALANCE.QUERY)) {
            preparedStatement.setLong(1, balance);
            preparedStatement.setLong(2, userId);
            result = preparedStatement.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }
}

enum SQLUser {
    GET_ALL("SELECT user_table.*, user_balance_table.balance FROM user_table, user_balance_table WHERE user_table.id = userid"),
    INSERT_USER("INSERT INTO user_table (id, login, password) VALUES (DEFAULT, ?, ?) RETURNING id"),
    INSERT_BALANCE_USER("INSERT INTO user_balance_table (id, userid, balance) VALUES (DEFAULT, ?, ?)"),
    GET_USER("SELECT user_table.*, user_balance_table.balance FROM user_table, user_balance_table WHERE user_table.id = userId AND login = ?"),
    UPDATE_USER_BALANCE("UPDATE user_balance_table SET balance = balance - ? WHERE userid = ?"),
    GET_USER_BALANCE("SELECT user_balance_table.balance FROM user_balance_table WHERE id = ?"),
    DELETE(""),
    UPDATE("");

    String QUERY;

    SQLUser(String QUERY){
        this.QUERY = QUERY;
    }
}
