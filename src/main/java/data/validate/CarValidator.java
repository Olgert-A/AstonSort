package data.validate;

public interface CarValidator {
    boolean isModelValid(String model);
    boolean isPowerValid(int power);
    boolean isProductionYearValid(int year);
}
