
package com.app.fishcab;

public class SelectedDriver {
    public static String name;
    public static int capacity;
    public static String eat;
    public static String rating;
    //during registration
    public static String password;
    public static String coordinate;

    ///
    public static double total_rating;
    public static int rating_count;
    ///
    public static int timeDriverToCustomer;
    public static int timeCustomerToDestination;
    ///

    public SelectedDriver(String name, int capacity, String eat, String rating){
        SelectedDriver.name = name;
        SelectedDriver.capacity = capacity;
        SelectedDriver.eat = eat;
        SelectedDriver.rating = rating;
    }

    // holding temporary driver during registration
    public SelectedDriver(){}

    public SelectedDriver(String name, int capacity, String eat, String rating, int timeDriverToCustomer, int timeCustomerToDestination){
        SelectedDriver.name = name;
        SelectedDriver.capacity = capacity;
        SelectedDriver.eat = eat;
        SelectedDriver.rating = rating;
        SelectedDriver.timeDriverToCustomer = timeDriverToCustomer;
        SelectedDriver.timeCustomerToDestination = timeCustomerToDestination;
    }

    public SelectedDriver(String name, String password, String coordinate, int capacity){
        SelectedDriver.name = name;
        SelectedDriver.password = password;
        SelectedDriver.coordinate = coordinate;
        SelectedDriver.capacity = capacity;
    }


    public static void setName(String name){
        SelectedDriver.name = name;
    }
    public static String getName(){
        return SelectedDriver.name;
    }

    public static void setPassword(String password){
        SelectedDriver.password = password;
    }
    public static String getPassword(){
        return SelectedDriver.password;
    }

    public static void setCoordinate(String coordinate){
        SelectedDriver.coordinate = coordinate;
    }
    public static String getCoordinate(){
        return SelectedDriver.coordinate;
    }

    public static void setCapacity(int capacity){
        SelectedDriver.capacity = capacity;
    }
    public static int getCapacity(){
        return SelectedDriver.capacity;
    }

    public static void setEat(String eat){
        SelectedDriver.eat = eat;
    }
    public static String getEat(){
        return SelectedDriver.eat;
    }

    public static void setRating(String rating){
        SelectedDriver.rating = rating;
    }
    public static String getRating(){
        return SelectedDriver.rating;
    }

    public static double getTotal_rating() {
        return total_rating;
    }

    public static void setTotal_rating(double total_rating) {
        SelectedDriver.total_rating = total_rating;
    }

    public static int getRating_count() {
        return rating_count;
    }

    public static void setRating_count(int rating_count) {
        SelectedDriver.rating_count = rating_count;
    }

    public static int getTimeCustomerToDestination() {
        return timeCustomerToDestination;
    }

    public static void setTimeCustomerToDestination(int timeCustomerToDestination) {
        SelectedDriver.timeCustomerToDestination = timeCustomerToDestination;
    }

    public static int getTimeDriverToCustomer() {
        return timeDriverToCustomer;
    }

    public static void setTimeDriverToCustomer(int timeDriverToCustomer) {
        SelectedDriver.timeDriverToCustomer = timeDriverToCustomer;
    }
}