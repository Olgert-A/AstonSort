package strategy;

import data.entities.Car;
import data.util.CarUtil;
import util.enums.CarFieldEnum;

import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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
    public boolean sort() {
        return false;
    }

    @Override
    public boolean search() {
        return false;
    }

    @Override
    public void showResults() {

    }
}
