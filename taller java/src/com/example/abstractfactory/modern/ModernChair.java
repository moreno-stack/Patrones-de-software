package com.example.abstractfactory.modern;

import com.example.abstractfactory.Chair;

public class ModernChair implements Chair {
    @Override
    public void sitOn() {
        System.out.println("Sitting on a modern chair: sleek steel and leather.");
    }
}
