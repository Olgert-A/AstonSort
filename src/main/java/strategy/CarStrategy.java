package strategy;

import data.entities.Car;
import data.util.CarUtil;
import data.util.ParityChecker;
import data.util.Validate;
import util.enums.CarFieldEnum;
import util.enums.SortTypeEnum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        return false;
    }

    @Override
    public boolean collectDataFromFile(String name, int amount) {

        try (FileReader fileReader = new FileReader(name);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            line = bufferedReader.readLine();
            if (line.isEmpty()) {
                while (line.isEmpty()) {
                    line = bufferedReader.readLine();
                }
            }
            if (!(line.equals("Cars")) || line == null) {
                System.out.println("Invalid file");
                return false;
            }

            int counter = 0;
            Validate<String> modelValidator = v -> v.length() > 2;
            Validate<Integer> powerValidator = v -> v > 100;
            Validate <Integer> productionYearValidator = v -> v > 2000 && v < 2025;
            String model = null;
            int power = 0, productionYear = 0;
            boolean startFlag = false;

            while((line = bufferedReader.readLine()) != null && counter < amount) {
                if (line.trim().startsWith("[")){
                    startFlag = true;
                    model = null;
                    power = 0;
                    productionYear = 0;
                }

                if (line.trim().endsWith("]")) {
                    startFlag = false;
                    if (model != null && power != 0 & productionYear != 0) {
                        if (modelValidator.isValid(model) && powerValidator.isValid(power)
                                && productionYearValidator.isValid(productionYear)){
                            Car car = new Car.CarBuilder()
                                    .setModel(model)
                                    .setPower(power)
                                    .setProductionYear(productionYear)
                                    .build();
                            rawData.add(car);
                            counter++;
                        }
                        else {
                            System.out.println("Были прочитаны невалидные данные. Сущность не будет записана в файл.");
                        }
                    }
                }

                String[] splitLine = line.split(":");

                if (startFlag && splitLine.length > 1) {
                    switch (splitLine[0].trim()) {
                        case "Model" -> {
                            model = splitLine[1].trim();
                        }
                        case "Power" -> {
                            power = Integer.valueOf(splitLine[1].trim());
                        }
                        case "Production year" -> {
                            productionYear = Integer.valueOf(splitLine[1].trim(), 10);
                        }
                    }
                }
            }
            bufferedReader.close();
            if (counter < amount) {
                System.out.println("Файл закончился раньше, чем массив заполнился");
            }
            return true;
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
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
                return new CarUtil.CarModelComparator();
            }
            case YEAR -> {
                return new CarUtil.CarYearComparator();
            }
            case POWER -> {
                return new CarUtil.CarPowerComparator();
            }
            case ALL -> {
                return new CarUtil.CarGeneralComparator();
            }
        }
        return null;
    }

    private ParityChecker<Car> getFieldParityChecker(CarFieldEnum sortField) {
        switch (sortField) {
            case YEAR -> {
                return new CarUtil.CarProductionYearParityCheker();
            }
            case POWER -> {
                return new CarUtil.CarPowerParityCheker();
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
