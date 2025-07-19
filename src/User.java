/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 吴欣霖
 */
//User class
public class User {
    private String username, password;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    //returns username and password
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
}
