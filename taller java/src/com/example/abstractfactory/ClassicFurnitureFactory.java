package com.example.abstractfactory;

import com.example.abstractfactory.classic.ClassicChair;
import com.example.abstractfactory.classic.ClassicCoffeeTable;
import com.example.abstractfactory.classic.ClassicSofa;

public class ClassicFurnitureFactory implements FurnitureFactory {
    @Override
    public Chair createChair() {
        return new ClassicChair();
    }

    @Override
    public Sofa createSofa() {
        return new ClassicSofa();
    }

    @Override
    public CoffeeTable createCoffeeTable() {
        return new ClassicCoffeeTable();
    }
}
