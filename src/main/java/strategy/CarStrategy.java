package strategy;

import data.entities.Car;
import data.util.ParityChecker;
import util.enums.CarFieldEnum;

import java.util.Comparator;
import java.util.Objects;

import static util.ConsoleUtil.getValue;

public class CarStrategy extends AbstractStrategy<Car> implements Strategy {
    @Override
    public boolean collectInputData(int amount) {
        int carCount = 0;

        do {
            String model;
            int power, productionYear;
            String strUserInput;
            Integer intUserInput;

            System.out.println("\nЗаполните машину №" + carCount);

            strUserInput = getValue(String.class, CarFieldEnum.MODEL.getLocaleName(),
                    Objects::nonNull, "Модель неверная");

            if(strUserInput == null) {
                System.out.println("Не удалось считать модель, ввод объекта будет пропущен");
                continue;
            }
            else
                model = strUserInput;

            intUserInput = getValue(Integer.class, CarFieldEnum.POWER.getLocaleName(),
                    Objects::nonNull, "Мощность неверна");

            if (intUserInput == null) {
                System.out.println("Не удалось считать мощность, ввод объекта будет пропущен");
                continue;
            }
            else
                power = intUserInput;

            intUserInput = getValue(Integer.class, CarFieldEnum.YEAR.getLocaleName(),
                    Objects::nonNull, "Год выпуска неверен");

            if (intUserInput == null) {
                System.out.println("Не удалось считать год выпуска, ввод объекта будет пропущен");
                continue;
            }
            else
                productionYear = intUserInput;

            Car car = new Car.CarBuilder()
                    .setModel(model)
                    .setPower(power)
                    .setProductionYear(productionYear)
                    .build();

            this.rawData.add(car);
            carCount++;
        } while (carCount < amount);

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
        CarFieldEnum sortField;
        Comparator<Car> comparator = null;
        ParityChecker<Car> parityChecker = null;

        StringBuilder requestTextBuilder = new StringBuilder("\nВыберите поля сортировки:");
        int fieldAmount = CarFieldEnum.values().length;
        for(var field : CarFieldEnum.values())
            requestTextBuilder.append("\n").append(field.getOrdinalLocaleName());

        intUserInput = getValue(Integer.class, requestTextBuilder.toString(),
                v -> v>=0 && v<fieldAmount, "Значение должно быть от 0 до " + (fieldAmount-1));

        if(intUserInput == null) {
            System.out.println("Не удалось выбрать поля сортировки, операция будет прервана!");
            return false;
        }
        else
            sortField = CarFieldEnum.values()[intUserInput];

        switch (sortField) {
            case MODEL -> comparator = Comparator.comparing(Car::getModel);
            case YEAR ->  {
                comparator = Comparator.comparing(Car::getProductionYear);
                parityChecker = v -> v.getProductionYear() % 2 == 0;
            }
            case POWER -> {
                comparator = Comparator.comparing(Car::getPower);
                parityChecker = v -> v.getPower() % 2 == 0;
            }
            case ALL -> {
                comparator = Comparator.comparing(Car::getProductionYear)
                        .thenComparing(Car::getModel).thenComparing(Car::getPower);
                parityChecker = v -> v.getPower() % 2 == 0;
            }
        }

        String requestText = """
                Выберите тип сортировки:
                0 - Обычная
                1 - Сортировка четных полей""";

        intUserInput = getValue(Integer.class, requestText, v -> v>=0 && v<2, "Значение должно быть 0 или 1");

        if(intUserInput == null) {
            System.out.println("Не удалось выбрать тип сортировки, операция будет прервана!");
            return false;
        }
        else
            sortType = intUserInput;

        switch (sortType) {
            case 0 -> this.processedData = this.sortAlgorithm.sort(this.rawData, comparator);
            case 1 -> {
                if(parityChecker == null) {
                    System.out.println("Нельзя использовать сортировку четных полей для выбранного поля");
                    return false;
                }
                else
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
