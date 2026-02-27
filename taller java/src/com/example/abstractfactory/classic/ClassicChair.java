package com.example.abstractfactory.classic;

import com.example.abstractfactory.Chair;

public class ClassicChair implements Chair {
    @Override
    public void sitOn() {
        System.out.println("Sitting on a classic chair: carved wood with velvet seat.");
    }
}
