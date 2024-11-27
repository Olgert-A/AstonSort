package strategy;

import data.entities.Car;
import util.enums.CarFieldEnum;

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
