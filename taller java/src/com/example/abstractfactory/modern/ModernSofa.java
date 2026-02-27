package com.example.abstractfactory.modern;

import com.example.abstractfactory.Sofa;

public class ModernSofa implements Sofa {
    @Override
    public void lieOn() {
        System.out.println("Lying on a modern sofa: low profile with firm cushions.");
    }
}
