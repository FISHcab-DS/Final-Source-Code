package com.app.fishcab;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Pair;

public class Request {
    public static String name;
    public static String pul;
    public static String dl;
    public static String eat;
    public static int capacity;
    public static String price;
    public static double distanceCustomer;
    public static int timeDriverToCustomer;

    public static int timeCustomerToDestination;

    public static int rate;

    //    public Request(String name, String pul, String dl, String eat, int capacity){
//        this.name = name;
//        this.pul = pul;
//        this.dl = dl;
//        this.eat = eat;
//        this.capacity = capacity;
//        this.price = calcPrice(pul, dl);
//    }
    public Request(){
//        Request.name = CurrentUserInfo.getUsername();
//        Request.pul = "1100,2048";
//        Request.dl = "1024,3022";
//        Request.eat = "16:30";
//        Request.capacity = 2;
//        Request.price = "12.50";
    }

    public static int getTimeDriverToCustomer() {
        return timeDriverToCustomer;
    }

    public static void setTimeDriverToCustomer(int timeDriverToCustomer) {
        Request.timeDriverToCustomer = timeDriverToCustomer;
    }

    public static double getDistanceCustomer() {
        return distanceCustomer;
    }

    public static void setDistanceCustomer(double distanceCustomer) {
        Request.distanceCustomer = distanceCustomer;
    }

    public static String getName(){
        return name;
    }

    public static void setName(String name) {
        Request.name = name;
    }

    public static String getPul(){
        return pul;
    }
    public static void setPul(String pul){
        Request.pul = pul;
    }

    public static String getDl(){
        return dl;
    }
    public static void setDl(String dl){
        Request.dl = dl;
    }

    public static String getEat(){
        return eat;
    }
    public static void setEat(String eat){
        Request.eat = eat;
    }

    public static int getCapacity(){
        return capacity;
    }
    public static void setCapacity(int capacity){
        Request.capacity = capacity;
    }

    public static String getPrice(){
        return price;
    }
    public static void setPrice(String price){
        Request.price = price;
    }

    public static int getTimeCustomerToDestination() {
        return timeCustomerToDestination;
    }

    public static void setTimeCustomerToDestination(int timeCustomerToDestination) {
        Request.timeCustomerToDestination = timeCustomerToDestination;
    }

    public static void setRate(int rate){
        Request.rate = rate;
    }

    public static int getRate() {
        return rate;
    }
}