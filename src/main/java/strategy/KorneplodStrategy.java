package strategy;

import data.entities.Korneplod;
import data.util.ParityChecker;
import util.enums.KorneplodFieldEnum;
import util.enums.SortTypeEnum;

import java.util.Comparator;
import java.util.Objects;

import static util.ConsoleUtil.getSortType;
import static util.ConsoleUtil.getValue;

public class KorneplodStrategy extends AbstractStrategy<Korneplod> implements Strategy {
    @Override
    public boolean collectInputData(int amount) {
        int korneplodCount = 0;

        do {
            String type, color;
            double weight;
            String strUserInput;
            Double doubleUserInput;

            System.out.println("\nЗаполните кореплод №" + korneplodCount);

            strUserInput = getValue(String.class, KorneplodFieldEnum.TYPE.getLocaleName(),
                    Objects::nonNull, "Тип неверный");

            if (strUserInput == null) {
                System.out.println("Не удалось считать тип, ввод объекта будет пропущен");
                continue;
            } else
                type = strUserInput;

            doubleUserInput = getValue(Double.class, KorneplodFieldEnum.WEIGHT.getLocaleName(),
                    Objects::nonNull, "Вес неверный");

            if (doubleUserInput == null) {
                System.out.println("Не удалось считать вес, ввод объекта будет пропущен");
                continue;
            } else
                weight = doubleUserInput;

            strUserInput = getValue(String.class, KorneplodFieldEnum.COLOR.getLocaleName(),
                    Objects::nonNull, "цвет неверный");

            if (strUserInput == null) {
                System.out.println("Не удалось считать цвет, ввод объекта будет пропущен");
                continue;
            } else
                color = strUserInput;

            Korneplod korneplod = new Korneplod.KorneplodBuilder()
                    .setType(type)
                    .setWeight(weight)
                    .setColor(color)
                    .build();

            this.rawData.add(korneplod);
            korneplodCount++;
        } while (korneplodCount < amount);

        return true;
    }

    @Override
    public boolean collectRandomData(int amount) {
        return false;
    }

    @Override
    public boolean collectDataFromFile(String name, int amount) {
        return false;
    }

    @Override
    public boolean saveResultsToFile(String name) {
        return false;
    }

    @Override
    public void showCollectedData() {

    }

    @Override
    public boolean sort() {
        try {
            KorneplodFieldEnum sortField = ConsoleUtil.getSortField();
            SortTypeEnum sortType = getSortType();
            sortByField(sortType, getFieldComparator(sortField), getFieldParityChecker(sortField));
        } catch (Exception e) {
           throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    private Comparator<Korneplod> getFieldComparator(KorneplodFieldEnum sortField) {
        switch (sortField) {
            case TYPE -> {
                return Comparator.comparing(Korneplod::getType);
            }
            case COLOR -> {
                return Comparator.comparing(Korneplod::getColor);
            }
            case WEIGHT -> {
                return Comparator.comparing(Korneplod::getWeight);
            }
            case ALL -> {
                return Comparator.comparing(Korneplod::getType)
                        .thenComparing(Korneplod::getColor).thenComparing(Korneplod::getWeight);
            }
        }
        return null;
    }

    private ParityChecker<Korneplod> getFieldParityChecker(KorneplodFieldEnum sortField) {
        if (sortField.equals(KorneplodFieldEnum.WEIGHT)) {
            return obj -> obj.getWeight() % 2 == 0;
        }
        return null;
    }

    @Override
    public boolean search() {
        return false;
    }

    @Override
    public void showResults() {

    }

    private static class ConsoleUtil {
        public static KorneplodFieldEnum getSortField() throws Exception {
            KorneplodFieldEnum sortField;
            StringBuilder requestTextBuilder = new StringBuilder("\nВыберите поля сортировки:");
            int fieldAmount = KorneplodFieldEnum.values().length;

            for (var field : KorneplodFieldEnum.values())
                requestTextBuilder.append("\n").append(field.getOrdinalLocaleName());

            Integer intUserInput = getValue(Integer.class, requestTextBuilder.toString(),
                    v -> v >= 0 && v < fieldAmount, "Значение должно быть от 0 до " + (fieldAmount - 1));

            if (intUserInput == null) {
                System.out.println("Не удалось выбрать поля сортировки, операция будет прервана!");
                throw new Exception("return false");
            } else
                sortField = KorneplodFieldEnum.values()[intUserInput];
            return sortField;
        }
    }


}
