package com.example.abstractfactory.modern;

import com.example.abstractfactory.CoffeeTable;

public class ModernCoffeeTable implements CoffeeTable {
    @Override
    public void putOn() {
        System.out.println("Placing items on a modern coffee table: tempered glass top.");
    }
}
