package edu.geekhub.example.lambda;

public class CheckIfSwimer implements CheckTrait {
    public boolean test(Animal a) {
        return a.canSwim();
    }
}
