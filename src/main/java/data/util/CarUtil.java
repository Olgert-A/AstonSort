package data.util;

import data.entities.Car;

import java.time.Year;
import java.util.Comparator;

public class CarUtil {

    public static final String MODEL_INVALID_TEXT = "Модель не нулевое значение и не пустая строка, символы должны быть больше или равно 3";
    public static final String POWER_INVALID_TEXT = "Мощность должна быть больше 100 и меньше или равно 1000";
    public static final String YEAR_INVALID_TEXT = "Год выпуска должна находится от 2000 до текущего";

    public static class CarModelComparator implements Comparator<Car>{
        @Override
        public int compare(Car o1, Car o2) {
            return o1.getModel().compareTo(o2.getModel());
        }
    }

    public static class CarPowerComparator implements Comparator<Car>{
        @Override
        public int compare(Car o1, Car o2) {
            if (o1.getPower()< o2.getPower()){
                return -1;
            }

            if (o1.getPower() == o2.getPower()){
                return 0;
            }
            return 1;
        }
    }

    public static class CarYearComparator implements Comparator <Car> {
        @Override
        public int compare(Car o1, Car o2) {
            if (o1.getProductionYear()< o2.getProductionYear()){
                return -1;
            }

            if (o1.getProductionYear() == o2.getProductionYear()){
                return 0;
            }
            return 1;
        }
    }

    public static class CarGeneralComparator implements Comparator<Car> {
        @Override
        public int compare(Car o1, Car o2) {
            return Comparator.comparing(Car::getModel)
                    .thenComparingInt(Car::getPower)
                    .thenComparingInt(Car::getProductionYear)
                    .compare(o1, o2);
        }
    }

    public static class CarPowerParityCheker implements ParityChecker<Car> {
        @Override
        public boolean isEven(Car obj) {
            return obj != null && obj.getPower() % 2 == 0;
        }
    }

    public static class CarProductionYearParityCheker implements ParityChecker<Car> {
        @Override
        public boolean isEven(Car obj) {
            return obj != null && obj.getProductionYear() % 2 == 0;
        }
    }

    public static class CarParityChekerGeneral implements ParityChecker<Car> {
        @Override
        public boolean isEven(Car obj) {
            return obj != null && obj.getPower() % 2 == 0 && obj.getProductionYear() % 2 == 0;
        }
    }

    public static class CarModelValidator implements Validate<String> {
        @Override
        public boolean isValid(String str) {
            return str!= null && !str.trim().isEmpty() && str.length() >= 3;
        }
    }

    public static class CarPowerValidator implements Validate<Integer> {
        @Override
        public boolean isValid(Integer integer) {
            return integer > 100 && integer <= 1000;
        }
    }

    public static class CarProductionYearValidator implements Validate<Integer> {
        @Override
        public boolean isValid(Integer integer) {
            int currentYear = Year.now().getValue();
            return integer >= 2000 && integer <= currentYear;
        }
    }
}
