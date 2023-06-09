package edu.geekhub.homework;

public class Circle extends Figure {
    private final double radius;

    public Circle(double radius) {
        super("Circle");
        this.radius = radius;
    }

    @Override
    public double getP() {
        return 2 * Math.PI * radius;
    }

    @Override
    public double getS() {
        return Math.PI * Math.pow(radius, 2);
    }

    @Override
    public String toString() {
        return String.format("%s %n%s %nP = %.2f %nS = %.2f %nColor - %s %n", super.toString(), super.type, getP(), getS(), super.color);
    }
}
