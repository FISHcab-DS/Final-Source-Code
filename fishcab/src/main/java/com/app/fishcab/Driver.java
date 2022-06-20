package com.app.fishcab;

public class Driver {
    public String name;
    public int capacity;
    public String eat;
    public String rating;
    public int timeDriverToCustomer;
    public int timeCustomerToDestination;

    public Driver(String name, int capacity, String eat, String rating){
        this.name = name;
        this.capacity = capacity;
        this.eat = eat;
        this.rating = rating;
    }

    public Driver(String name, int capacity, String eat, String rating, int timeDriverToCustomer, int timeCustomerToDestination){
        this.name = name;
        this.capacity = capacity;
        this.eat = eat;
        this.rating = rating;
        this.timeDriverToCustomer = timeDriverToCustomer;
        this.timeCustomerToDestination = timeCustomerToDestination;
    }

    public String getName(){
        return this.name;
    }

    public int getCapacity(){
        return this.capacity;
    }

    public String getEat(){
        return this.eat;
    }

    public String getRating(){
        return this.rating;
    }

    public int getTimeDriverToCustomer(){
        return this.timeDriverToCustomer;
    }

    public int getTimeCustomerToDestination(){
        return timeCustomerToDestination;
    }

}