package strategy;

import data.entities.Korneplod;
import data.util.ParityChecker;
import util.enums.KorneplodFieldEnum;

import java.util.Comparator;
import java.util.Objects;

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
        Integer intUserInput;
        int sortType;
        KorneplodFieldEnum sortField;
        Comparator<Korneplod> comparator = null;
        ParityChecker<Korneplod> parityChecker = null;

        StringBuilder requestTextBuilder = new StringBuilder("\nВыберите поля сортировки:");
        int fieldAmount = KorneplodFieldEnum.values().length;
        for (var field : KorneplodFieldEnum.values())
            requestTextBuilder.append("\n").append(field.getOrdinalLocaleName());

        intUserInput = getValue(Integer.class, requestTextBuilder.toString(),
                v -> v >= 0 && v < fieldAmount, "Значение должно быть от 0 до " + (fieldAmount - 1));

        if (intUserInput == null) {
            System.out.println("Не удалось выбрать поля сортировки, операция будет прервана!");
            return false;
        } else
            sortField = KorneplodFieldEnum.values()[intUserInput];

        switch (sortField) {
            case TYPE -> comparator = Comparator.comparing(Korneplod::getType);
            case COLOR -> comparator = Comparator.comparing(Korneplod::getColor);
            case WEIGHT -> {
                comparator = Comparator.comparing(Korneplod::getWeight);
                parityChecker = v -> v.getWeight() % 2 == 0;
            }
            case ALL -> {
                comparator = Comparator.comparing(Korneplod::getType)
                        .thenComparing(Korneplod::getColor).thenComparing(Korneplod::getWeight);
                parityChecker = v -> v.getWeight() % 2 == 0;
            }
        }

        String requestText = """
                Выберите тип сортировки:
                0 - Обычная
                1 - Сортировка четных полей""";

        intUserInput = getValue(Integer.class, requestText, v -> v >= 0 && v < 2, "Значение должно быть 0 или 1");

        if (intUserInput == null) {
            System.out.println("Не удалось выбрать тип сортировки, операция будет прервана!");
            return false;
        } else
            sortType = intUserInput;

        switch (sortType) {
            case 0 -> this.processedData = this.sortAlgorithm.sort(this.rawData, comparator);
            case 1 -> {
                if (parityChecker == null) {
                    System.out.println("Нельзя использовать сортировку четных полей для выбранного поля");
                    return false;
                } else
                    this.processedData = this.sortAlgorithm.sortEvenValues(this.rawData, comparator, parityChecker);
            }
        }

        return true;
    }

    @Override
    public boolean search() {
        return false;
    }

    @Override
    public void showResults() {

    }
}
