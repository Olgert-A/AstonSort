package data.util;

import data.entities.Korneplod;
import data.validate.Validate;

import java.util.Comparator;

public class KorneplodUtil {

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

    public static class KorneplodTypeValidator implements Validate<Korneplod> {
        @Override
        public boolean isValid(Korneplod obj) {
            return obj.getType() != null && !obj.getType().trim().isEmpty() && obj.getType().length()>=3;
            // тип не нулевое значение и не пустая строка, символы больше или равно 3
        }
    }

    public static class KorneplodWeightValidator implements Validate<Korneplod> {
        @Override
        public boolean isValid(Korneplod obj) {
            return obj.getWeight() > 0 && obj.getWeight()<=50;
            // вес больше 0 и меньше или равно 50
        }
    }

    public static class KorneplodColorValidator implements Validate<Korneplod> {
        @Override
        public boolean isValid(Korneplod obj) {
            return obj.getColor() != null && !obj.getColor().trim().isEmpty();
            // цвет не нулевое значение и не пустая строка
        }
    }

    public static final String TYPE_INVALID_TEXT = "Тип корнеплода не нулевое значение и не пустая строка, символы должны быть больше или равно 3";
    public static final String WEIGHT_INVALID_TEXT = "Вес больше 0 и меньше или равно 50";
    public static final String COLOR_INVALID_TEXT = "Цвет корнеплода не нулевое значение и не пустая строка";
}
