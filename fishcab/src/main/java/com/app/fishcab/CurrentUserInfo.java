package com.app.fishcab;

public class CurrentUserInfo {
    public static String role;
    public static String username;
    public static String password;

    public CurrentUserInfo(){}
    public CurrentUserInfo(String role, String username, String password){
        CurrentUserInfo.role = role;
        CurrentUserInfo.username = username;
        CurrentUserInfo.password = password;
    }

    public static String getRole(){
        return role;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword(){
        return password;
    }
}
