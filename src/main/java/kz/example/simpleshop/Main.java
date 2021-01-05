package kz.example.simpleshop;

import kz.example.simpleshop.controller.ProductController;
import kz.example.simpleshop.controller.ProductControllerResponse;
import kz.example.simpleshop.controller.UserController;
import kz.example.simpleshop.dto.Order;
import kz.example.simpleshop.dto.Product;
import kz.example.simpleshop.dto.User;
import kz.example.simpleshop.jdbc.MyDatabase;
import kz.example.simpleshop.response.UserResponse;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

       Connection connection = MyDatabase.getInstance().getConnection();
       Scanner in = new Scanner(System.in);
       UserController userController = new UserController(connection);
       ProductController productController = new ProductController(connection);

       User user = verificationUser(in,userController);
       buyInShop(in,user,productController, userController);

    }

    private static User verificationUser(Scanner in, UserController userController){
        User verifyUser;
        while (true){
            System.out.print("Для регистрации введите 1, для входа введите 2: ");
            String choice= in.next();
            System.out.println();
            if (choice.equals("1")){
                User user = new User();

                System.out.println("Ввод данных \n");
                in.nextLine();
                System.out.print("Введите логин: ");
                user.setLogin(in.nextLine().trim());
                System.out.print("Введите пароль: ");
                user.setPassword(in.nextLine().trim());
                String result = userController.insertUserInDB(user);
                System.out.println(result);
            }else if (choice.equals("2")){
                User user = new User();
                System.out.println("Ввод данных \n");
                in.nextLine();
                System.out.print("Введите логин: ");
                user.setLogin(in.nextLine().trim());
                System.out.print("Введите пароль: ");
                user.setPassword(in.nextLine().trim());
                UserResponse<User,String> userResponse = userController.signInUser(user);
                if (userResponse.getUser() != null){

                    verifyUser = userResponse.getUser();
                    System.out.println("\n" + userResponse.getResponse()+ "\n" + userResponse.getUser());
                    break;
                }else{
                    System.out.println(userResponse.getResponse());
                }
            } else {
                System.out.println("Неверно");
            }
        }
        return verifyUser;
    }

    private static void buyInShop(Scanner in, User user, ProductController productController, UserController userController){
        System.out.println("Меню\n\nВыберете нужное действие\n\n");
        while (true){

            System.out.println("1 - Список доступных товаров для покупки\n" +
                    "2 - Мои покупки\n" +
                    "3 - Мой баланс\n" +
                    "4 - Выход\n");
            String choice = in.nextLine().trim();

            switch (choice){
                case "1" : {
                    List<Product> productList = productController.getAllProduct();
                    for (Product productItem :
                            productList) {
                        System.out.println(String.format("%d - %s Стоимость: %d  Количество: %d",
                                productItem.getId(),
                                productItem.getName(),
                                productItem.getPrice(),
                                productItem.getCount()));
                    }
                    System.out.print("Хотите что-нибудь купить? Y/N: ");
                    String choiceBuyOrNo = in.nextLine();
                    if (
                    choiceBuyOrNo.equals("Y") ||
                    choiceBuyOrNo.equals("y") ||
                    choiceBuyOrNo.equals("yes")
                    ){
                        System.out.print("\nВведите номер желаемого товара: ");
                        String id = in.next();
                        if (id.matches("[-+]?\\d+")){
                            int intId = Integer.parseInt(id);
                            System.out.print("\nКоличество: ");
                            String count = in.next();
                            if (count.matches("[-+]?\\d+")){
                                int intCount = Integer.parseInt(count);
                                ProductControllerResponse response =  productController.buyProduct(user.getId(),intId, intCount);

                                    if(response == ProductControllerResponse.PRODUCT_BUY_SUCCESSFUL){
                                        long balance = userController.getUserBalanceById(user.getId());
                                        System.out.println(response.getVALUE()+
                                                "\n Ваше баланс составляет: " + balance + "\n");
                                    }else {
                                        System.out.println(response.getVALUE());
                                }
                            } else {
                                System.out.println("Неверные данные");

                            }

                        }else {
                            System.out.println("Неверные данные");
                        }
                    }else if
                    (
                    choiceBuyOrNo.equals("N") ||
                    choiceBuyOrNo.equals("NO") ||
                    choiceBuyOrNo.equals("no")
                    ){
                        System.out.println("\n");
                        continue;
                    }
                    break;
                }

                case "2": {
                    List<Order> orderList = productController.getOrdersByUserId(user.getId());
                    if(orderList.isEmpty()){
                        System.out.println("Вы пока не совешали покупок");
                    }else {
                        for (Order orderItem :
                                orderList) {
                            System.out.println(String.format("%s, Количество: %d", orderItem.getName(),orderItem.getCount()));
                        }
                    }

                    System.out.print("\n");
                    break;
                }
                case "3": {
                   long balance = userController.getUserBalanceById(user.getId());
                    System.out.println("Ваш баланс составляет: " + balance+"\n");
                    break;
                }

                case "4": return;
            }

        }

    }

}
