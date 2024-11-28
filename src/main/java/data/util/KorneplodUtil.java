package data.util;

import data.entities.Korneplod;

import java.util.Comparator;

public class KorneplodUtil {

    public static final String TYPE_INVALID_TEXT = "Тип корнеплода не нулевое значение и не пустая строка, символы должны быть больше или равно 3";
    public static final String WEIGHT_INVALID_TEXT = "Вес должен быть больше 0 и меньше или равен 50";
    public static final String COLOR_INVALID_TEXT = "Цвет корнеплода не нулевое значение и не пустая строка";

    public static class KorneplodColorComparator implements Comparator<Korneplod> {
        @Override
        public int compare(Korneplod o1, Korneplod o2) {
            return o1.getColor().compareTo(o2.getColor());
        }
    }

    public static class KorneplodTypeComparator implements Comparator<Korneplod> {
        @Override
        public int compare(Korneplod o1, Korneplod o2) {
            return o1.getType().compareTo(o2.getType());
        }
    }

    public static class KorneplodWeightComparator implements Comparator<Korneplod> {
        @Override
        public int compare(Korneplod o1, Korneplod o2) {
            if (o1.getWeight()< o2.getWeight()){
                return -1;
            }

            if (o1.getWeight() == o2.getWeight()){
                return 0;
            }
            return 1;
        }
    }

    public static class KorneplodGeneralComparator implements Comparator<Korneplod> {
        @Override
        public int compare(Korneplod o1, Korneplod o2) {
            return Comparator.comparing(Korneplod::getType)
                    .thenComparingDouble(Korneplod::getWeight)
                    .thenComparing(Korneplod::getColor)
                    .compare(o1, o2);
        }
    }

    public static class KorneplodTypeValidator implements Validate<String> {
        @Override
        public boolean isValid(String str) {
            return str != null && !str.trim().isEmpty() && str.length()>=3;
        }
    }

    public static class KorneplodWeightValidator implements Validate<Double> {
        @Override
        public boolean isValid(Double db) {
            return db > 0 && db <= 50;
        }
    }

    public static class KorneplodColorValidator implements Validate<String> {
        @Override
        public boolean isValid(String str) {
            return str != null && !str.trim().isEmpty();
        }
    }
}
