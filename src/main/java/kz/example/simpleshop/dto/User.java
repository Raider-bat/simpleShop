package kz.example.simpleshop.dto;

public class User {
    private long id;
    private String login;
    private String password;
    private long balance;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Login: %s, Password: %s, Balance: %d ", id, login, password, balance);
    }
}
