package com.example.abstractfactory.classic;

import com.example.abstractfactory.Sofa;

public class ClassicSofa implements Sofa {
    @Override
    public void lieOn() {
        System.out.println("Lying on a classic sofa: plush cushions and ornate wooden frame.");
    }
}
