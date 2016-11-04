package calc2;

public class Operate {

    public static double add(double num1, double num2) {
        return num1 + num2;
    }

    public static double subtract(double num1, double num2) {
        return num1 - num2;
    }

    public static double multiply(double num1, double num2) {
        return num1 * num2;
    }

    public static double divide(double num1, double num2) {
        return num1 / num2;
    }

    public static double sqrt(double num) {
        return Math.sqrt(num);
    }

    public static double percent(double num) {
        return num / 100;
    }

    public static double reciprocal(double num) {
        return 1 / num;
    }

    public static double negate(double num) {
        return num * -1;
    }
}
