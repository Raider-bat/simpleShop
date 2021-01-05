package kz.example.simpleshop.controller;


import at.favre.lib.crypto.bcrypt.BCrypt;
import kz.example.simpleshop.response.UserResponse;
import kz.example.simpleshop.jdbc.dao.UserDao;
import kz.example.simpleshop.dto.User;

import java.sql.Connection;
import java.util.List;


public class UserController {

    private UserDao userDao;

    public UserController(Connection connection){

        this.userDao = new UserDao(connection);
    }

    public String insertUserInDB(User user){
        int nameSize = user.getLogin().length();
        int passwordSize = user.getPassword().length();
        if (nameSize >= 4 && nameSize < 20 && passwordSize >= 6 && passwordSize < 100){
          User userIsExist = userDao.getUserByLogin(user.getLogin());
          if (userIsExist.getLogin() == null){
             String hashPassword =  BCrypt.withDefaults().hashToString(12,user.getPassword().toCharArray());
             user.setPassword(hashPassword);
              userDao.insert(user);
              return UserControllerResponse.OKSIGNUP.RESPONSE;
          }else{
              return UserControllerResponse.USERISEXIST.RESPONSE;
          }
        }else{
            return UserControllerResponse.WRONGDATA.RESPONSE;
        }
    }

    public UserResponse<User,String> signInUser(User user){
        int nameSize = user.getLogin().length();
        int passwordSize = user.getPassword().length();
        UserResponse<User,String> userResponse = new UserResponse<>();
        if (nameSize >= 4 && nameSize < 20 && passwordSize >= 6 && passwordSize < 100){
            User userIsExist = userDao.getUserByLogin(user.getLogin());
            if(userIsExist.getLogin() == null){
                userResponse.setResponse(UserControllerResponse.WRONGDATA.RESPONSE);
            }else{
                if (BCrypt.verifyer().
                        verify(
                                user.getPassword().toCharArray(),
                                userIsExist.getPassword()
                        ).verified
                ){
                    userResponse.setUser(userIsExist);
                    userResponse.setResponse(UserControllerResponse.OKSIGNIN.RESPONSE);
                }else{

                    userResponse.setResponse(UserControllerResponse.WRONGDATA.RESPONSE);
                }
            }
        }else {
            userResponse.setResponse(UserControllerResponse.WRONGDATA.RESPONSE);
        }
        return userResponse;
    }

    public List<User> getAllUser(){
        return userDao.getAllUsers();
    }

    public long getUserBalanceById(long id){
        return userDao.getUserBalance(id);
    }
}

enum UserControllerResponse {
    OKSIGNUP("Регистрация успешна"),
    OKSIGNIN("Успешный вход"),
    WRONG("Ошибка"),
    WRONGDATA("Неверно введённые данные"),
    USERISEXIST("Подобный никнейм уже существует");
    String RESPONSE;

    UserControllerResponse(String RESPONSE){
        this.RESPONSE = RESPONSE;
    }
}
