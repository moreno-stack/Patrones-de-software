package com.example.abstractfactory.classic;

import com.example.abstractfactory.CoffeeTable;

public class ClassicCoffeeTable implements CoffeeTable {
    @Override
    public void putOn() {
        System.out.println("Placing items on a classic coffee table: mahogany and brass inlays.");
    }
}
