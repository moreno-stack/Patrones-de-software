package com.example.abstractfactory.showroom;

import com.example.abstractfactory.Chair;
import com.example.abstractfactory.CoffeeTable;
import com.example.abstractfactory.FurnitureFactory;
import com.example.abstractfactory.Sofa;

public class FurnitureShowroom {
    private Chair chair;
    private Sofa sofa;
    private CoffeeTable coffeeTable;

    // Client receives factory and uses it to create the family of products
    public FurnitureShowroom(FurnitureFactory factory) {
        chair = factory.createChair();
        sofa = factory.createSofa();
        coffeeTable = factory.createCoffeeTable();
    }

    public void displayFurniture() {
        chair.sitOn();
        sofa.lieOn();
        coffeeTable.putOn();
    }
}
