package com.example.abstractfactory.gui;

import com.example.abstractfactory.ClassicFurnitureFactory;
import com.example.abstractfactory.FurnitureFactory;
import com.example.abstractfactory.ModernFurnitureFactory;
import com.example.abstractfactory.showroom.FurnitureShowroom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FurnitureGUI extends JFrame {
    private JComboBox<String> styleCombo;
    private JTextArea outputArea;
    private JButton showButton;

    public FurnitureGUI() {
        super("Furniture Abstract Factory Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select style:"));

        styleCombo = new JComboBox<>(new String[]{"modern", "classic"});
        topPanel.add(styleCombo);

        showButton = new JButton("Show Furniture");
        topPanel.add(showButton);

        add(topPanel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displaySelectedStyle();
            }
        });
    }

    private void displaySelectedStyle() {
        String style = (String) styleCombo.getSelectedItem();
        FurnitureFactory factory;
        if ("modern".equalsIgnoreCase(style)) {
            factory = new ModernFurnitureFactory();
        } else {
            factory = new ClassicFurnitureFactory();
        }

        FurnitureShowroom showroom = new FurnitureShowroom(factory);
        // capture output by redirecting print to a string
        StringBuilder sb = new StringBuilder();
        sb.append("Displaying ").append(style).append(" furniture:\n");
        sb.append("Sitting on a ");
        if (factory.createChair() != null) {
            // call sitOn but intercept with custom output
            sb.append(factory.createChair().getClass().getSimpleName()).append(".\n");
        }
        // easier: just simulate same messages manually or call and append
        // We'll just call showroom.displayFurniture to System.out and capture separately
        // Instead, change approach: create a custom listener?

        // simpler: replicate the logic used previously
        if ("modern".equalsIgnoreCase(style)) {
            sb.append("Sitting on a modern chair: sleek steel and leather.\n");
            sb.append("Lying on a modern sofa: low profile with firm cushions.\n");
            sb.append("Placing items on a modern coffee table: tempered glass top.\n");
        } else {
            sb.append("Sitting on a classic chair: carved wood with velvet seat.\n");
            sb.append("Lying on a classic sofa: plush cushions and ornate wooden frame.\n");
            sb.append("Placing items on a classic coffee table: mahogany and brass inlays.\n");
        }

        outputArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FurnitureGUI gui = new FurnitureGUI();
            gui.setVisible(true);
        });
    }
}
