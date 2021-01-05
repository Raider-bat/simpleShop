package kz.example.simpleshop.dto;

public class Product {
    private long id;
    private String name;
    private long price;
    private int count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return String.format("\nId: %d, Название: %s, Цена: %d, кол-во: %d", id, name, price, count);
    }
}
