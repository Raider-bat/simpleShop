package kz.example.simpleshop.controller;

public enum ProductControllerResponse{
    PRODUCT_NOT_FOUND("Такого продукта не существует"),
    PRODUCT_BUY_SUCCESSFUL("Покупка успешно совершена"),
    PRODUCT_BUY_ERROR("Ошибка, покупка не совершена"),
    PRODUCT_BUY_ERROR_BALANCE("Ошибка, недостаточно средств"),
    PRODUCT_BUY_ERROR_COUNT("Ошибка, такого количеста товара нет");

    String VALUE;
    
    public String getVALUE() {
        return VALUE;
    }

    ProductControllerResponse(String VALUE){
        this.VALUE = VALUE;
    }
}
