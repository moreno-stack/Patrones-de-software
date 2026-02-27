package com.example.abstractfactory;

import com.example.abstractfactory.modern.ModernChair;
import com.example.abstractfactory.modern.ModernCoffeeTable;
import com.example.abstractfactory.modern.ModernSofa;

public class ModernFurnitureFactory implements FurnitureFactory {
    @Override
    public Chair createChair() {
        return new ModernChair();
    }

    @Override
    public Sofa createSofa() {
        return new ModernSofa();
    }

    @Override
    public CoffeeTable createCoffeeTable() {
        return new ModernCoffeeTable();
    }
}
