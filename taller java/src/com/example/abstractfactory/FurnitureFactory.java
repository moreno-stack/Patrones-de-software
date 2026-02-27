package com.example.abstractfactory;

// Abstract Factory interface defines methods to create each type of product
public interface FurnitureFactory {
    Chair createChair();
    Sofa createSofa();
    CoffeeTable createCoffeeTable();
}
