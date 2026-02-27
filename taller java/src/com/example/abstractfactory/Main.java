package com.example.abstractfactory;

import com.example.abstractfactory.showroom.FurnitureShowroom;

public class Main {
    public static void main(String[] args) {
        // if "gui" is passed, launch the Swing interface
        if (args.length > 0 && "gui".equalsIgnoreCase(args[0])) {
            com.example.abstractfactory.gui.FurnitureGUI.main(args);
            return;
        }

        // Suppose we choose a style based on some configuration or user input
        String style = "modern"; // could be "classic" or "modern"

        FurnitureFactory factory;
        if ("modern".equalsIgnoreCase(style)) {
            factory = new ModernFurnitureFactory();
        } else {
            factory = new ClassicFurnitureFactory();
        }

        FurnitureShowroom showroom = new FurnitureShowroom(factory);
        System.out.println("Displaying " + style + " furniture:");
        showroom.displayFurniture();

        // We can also easily switch styles without changing showroom logic
        style = "classic";
        if ("modern".equalsIgnoreCase(style)) {
            factory = new ModernFurnitureFactory();
        } else {
            factory = new ClassicFurnitureFactory();
        }

        showroom = new FurnitureShowroom(factory);
        System.out.println("\nDisplaying " + style + " furniture:");
        showroom.displayFurniture();
    }
}
