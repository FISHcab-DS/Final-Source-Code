package com.app.fishcab;

public class PendingDriver {
    public String name;
    public String coordinate;
    public int capacity;
    public String status;

    public String rating;

    public String customer;

    public PendingDriver(String name, String status, int capacity, String coordinate, String rating, String customer){
        this.name = name;
        this.coordinate = coordinate;
        this.capacity = capacity;
        this.status=  status;
        this.rating = rating;
        this.customer = customer;
    }

    public PendingDriver(String name, String coordinate, int capacity, String status, String rating){
        this.name = name;
        this.coordinate = coordinate;
        this.capacity = capacity;
        this.status=  status;
        this.rating = rating;
    }

    public PendingDriver(String name, String coordinate, int capacity, String status){
        this.name = name;
        this.coordinate = coordinate;
        this.capacity = capacity;
        this.status=  status;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }
    public String getCoordinate(){
        return this.coordinate;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public int getCapacity(){
        return this.capacity;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
    public String getRating(){
        return this.rating;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
    public String getCustomer(){
        return this.customer;
    }
}
