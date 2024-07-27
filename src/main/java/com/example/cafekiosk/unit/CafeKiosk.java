package com.example.cafekiosk.unit;

import com.example.cafekiosk.unit.beverage.Beverage;
import com.example.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CafeKiosk {
    private final List<Beverage> beverageList = new ArrayList<>();

    public List<Beverage> getBeverageList() {
        return beverageList;
    }

    public void add(Beverage beverage){
        beverageList.add(beverage);
    }

    public void remove(Beverage beverage){
        beverageList.remove(beverage);
    }

    public void clear(){
        beverageList.clear();
    }

    public int calculateTotalPrice() {
        return beverageList.stream().mapToInt(Beverage::getPrice).sum();
    }

    public Order createOrder(){
        return new Order(LocalDateTime.now(), beverageList);
    }
}
