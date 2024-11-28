package strategy;

import data.entities.Car;
import data.util.ParityChecker;
import data.util.CarUtil;
import util.enums.CarFieldEnum;
import util.enums.SortTypeEnum;

import java.time.Year;
import java.util.List;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;

import static util.ConsoleUtil.getSortType;
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

            if (strUserInput == null) {
                System.out.println("Не удалось считать модель, ввод объекта будет пропущен");
                continue;
            } else
                model = strUserInput;

            intUserInput = getValue(Integer.class, CarFieldEnum.POWER.getLocaleName(),
                    Objects::nonNull, "Мощность неверна");

            if (intUserInput == null) {
                System.out.println("Не удалось считать мощность, ввод объекта будет пропущен");
                continue;
            } else
                power = intUserInput;

            intUserInput = getValue(Integer.class, CarFieldEnum.YEAR.getLocaleName(),
                    Objects::nonNull, "Год выпуска неверен");

            if (intUserInput == null) {
                System.out.println("Не удалось считать год выпуска, ввод объекта будет пропущен");
                continue;
            } else
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
        if (amount <= 0) return false;
        List<String> modelList = List.of("Lada", "Bmw", "Kia", "Porsche", "Hyundai", "Jeep", "Mercedes", "Geely");
        int minYear = 2000;
        int maxYear = Year.now().getValue();
        int minPower = 100;
        int maxPower = 1000;

        Random random = new Random();

        String model;
        int power;
        int year;

        while (rawData.size() < amount) {
            model = modelList.get(random.nextInt(modelList.size()));
            power = minPower + random.nextInt(maxPower - minPower + 1);
            year = minYear + random.nextInt(maxYear - minYear + 1);

            if (new CarUtil.CarModelValidator().isValid(model) && new CarUtil.CarPowerValidator().isValid(power)
                    && new CarUtil.CarProductionYearValidator().isValid(year)) {
                Car car = new Car.CarBuilder().setModel(model).setPower(power).setProductionYear(year).build();
                this.rawData.add(car);
            }
        }
        return true;
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
    public boolean sort(SortTypeEnum sortType) {
        try {
            CarFieldEnum sortField = ConsoleUtil.getSortField();
            sortByField(sortType, getFieldComparator(sortField), getFieldParityChecker(sortField));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    private Comparator<Car> getFieldComparator(CarFieldEnum sortField) {
        switch (sortField) {
            case MODEL -> {
                return Comparator.comparing(Car::getModel);
            }
            case YEAR -> {
                return Comparator.comparing(Car::getProductionYear);
            }
            case POWER -> {
                return Comparator.comparing(Car::getPower);
            }
            case ALL -> {
                return Comparator.comparing(Car::getProductionYear)
                        .thenComparing(Car::getModel).thenComparing(Car::getPower);
            }
        }
        return null;
    }

    private ParityChecker<Car> getFieldParityChecker(CarFieldEnum sortField) {
        switch (sortField) {
            case YEAR -> {
                return obj -> obj.getProductionYear() % 2 == 0;
            }
            case POWER -> {
                return obj -> obj.getPower() % 2 == 0;
            }
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
        public static CarFieldEnum getSortField() throws Exception {
            CarFieldEnum sortField;
            StringBuilder requestTextBuilder = new StringBuilder("\nВыберите поля сортировки:");
            int fieldAmount = CarFieldEnum.values().length;

            for (var field : CarFieldEnum.values())
                requestTextBuilder.append("\n").append(field.getOrdinalLocaleName());

            Integer intUserInput = getValue(Integer.class, requestTextBuilder.toString(),
                    v -> v >= 0 && v < fieldAmount, "Значение должно быть от 0 до " + (fieldAmount - 1));

            if (intUserInput == null) {
                System.out.println("Не удалось выбрать поля сортировки, операция будет прервана!");
                throw new Exception("return false");
            } else
                sortField = CarFieldEnum.values()[intUserInput];
            return sortField;
        }
    }
}
