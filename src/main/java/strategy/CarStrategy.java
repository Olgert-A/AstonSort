package strategy;

import data.entities.Book;
import data.entities.Car;
import data.util.Validate;
import util.enums.CarFieldEnum;

import java.io.*;
import java.util.List;
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

        try (FileWriter fileWriter = new FileWriter(name);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            FileReader fileReader = new FileReader(name);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            if (processedData.isEmpty()) {
                System.out.println("Нечего записывать в файл.");
                return false;
            }
            String line = bufferedReader.readLine();
            if (line == null) {
                bufferedReader.close();
                bufferedWriter.write("Cars");
                bufferedWriter.newLine();
            }
            bufferedReader.close();
            List<Car> carList = processedData;
            for (Car car:carList) {
                bufferedWriter.write("[");
                bufferedWriter.newLine();
                bufferedWriter.write("Model: ");
                bufferedWriter.write(car.getModel());
                bufferedWriter.newLine();
                bufferedWriter.write("Power: ");
                bufferedWriter.write(car.getPower());
                bufferedWriter.newLine();
                bufferedWriter.write("Production year: ");
                bufferedWriter.write(car.getProductionYear());
                bufferedWriter.newLine();
                bufferedWriter.write("]");
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
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
