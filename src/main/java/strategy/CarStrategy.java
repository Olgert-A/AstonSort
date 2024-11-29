package strategy;

import data.entities.Car;
import data.util.CarUtil;
import data.util.ParityChecker;
import util.enums.BookFieldEnum;
import util.enums.CarFieldEnum;
import util.enums.SortTypeEnum;
import util.enums.ViewOrdinalUtil;

import java.io.*;
import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static util.ConsoleUtil.getValue;
import static util.enums.ViewOrdinalUtil.*;


public class CarStrategy extends AbstractStrategy<Car> implements Strategy {
    @Override
    public boolean collectInputData(int amount) {
        int carCount = 0;

        do {
            String model;
            int power, productionYear;

            System.out.println("\nЗаполните машину №" + carCount);
            try {
                model = ConsoleUtil.getModelField();
                power = ConsoleUtil.getPowerField();
                productionYear = ConsoleUtil.getProductionYearField();
            } catch (IOException e) {
                System.out.println(e.getMessage() + " Заполнение машины начнётся с начала");
                continue;
            }

            Car car = new Car.CarBuilder()
                    .setModel(model)
                    .setPower(power)
                    .setProductionYear(productionYear)
                    .build();

            this.rawData.add(car);
        } while (++carCount < amount);

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

            if (new CarUtil.CarModelValidator().isValid(model) &&
                    new CarUtil.CarPowerValidator().isValid(power) &&
                    new CarUtil.CarProductionYearValidator().isValid(year)) {

                Car car = new Car.CarBuilder()
                        .setModel(model)
                        .setPower(power)
                        .setProductionYear(year)
                        .build();
                this.rawData.add(car);
            }
        }
        return true;
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
            String model = null;
            int power = 0, productionYear = 0;
            boolean startFlag = false;

            while ((line = bufferedReader.readLine()) != null && counter < amount) {
                if (line.trim().startsWith("[")) {
                    startFlag = true;
                    model = null;
                    power = 0;
                    productionYear = 0;
                }

                if (line.trim().endsWith("]")) {
                    startFlag = false;
                    if (model != null && power != 0 & productionYear != 0) {
                        if (new CarUtil.CarModelValidator().isValid(model) &&
                                new CarUtil.CarPowerValidator().isValid(power) &&
                                new CarUtil.CarProductionYearValidator().isValid(productionYear)) {
                            Car car = new Car.CarBuilder()
                                    .setModel(model)
                                    .setPower(power)
                                    .setProductionYear(productionYear)
                                    .build();
                            rawData.add(car);
                            counter++;
                        } else {
                            System.out.println("Были прочитаны невалидные данные. Сущность не будет записана в файл.");
                        }
                    }
                }

                String[] splitLine = line.split(":");

                if (startFlag && splitLine.length > 1) {
                    switch (splitLine[0].trim()) {
                        case "Model" -> model = splitLine[1].trim();
                        case "Power" -> power = Integer.parseInt(splitLine[1].trim());
                        case "Production year" -> productionYear = Integer.valueOf(splitLine[1].trim(), 10);
                    }
                }
            }
            bufferedReader.close();
            if (counter < amount) {
                System.out.println("Файл закончился раньше, чем массив заполнился");
            }
            return true;
        } catch (IOException ex) {
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
            for (Car car : carList) {
                bufferedWriter.write("[");
                bufferedWriter.newLine();
                bufferedWriter.write("Model: ");
                bufferedWriter.write(car.getModel());
                bufferedWriter.newLine();
                bufferedWriter.write("Power: ");
                bufferedWriter.write(String.valueOf(car.getPower()));
                bufferedWriter.newLine();
                bufferedWriter.write("Production year: ");
                bufferedWriter.write(String.valueOf(car.getProductionYear()));
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
    public boolean sort(SortTypeEnum sortType) {
        try {
            CarFieldEnum sortField = ConsoleUtil.getSortField();
            sortByField(sortType, getFieldComparator(sortField), getFieldParityChecker(sortField));
        } catch (IOException e) {
            System.out.println(e.getMessage() + " Сортировка будет прекращена.");
            return false;
        }

        return true;
    }

    private Comparator<Car> getFieldComparator(CarFieldEnum sortField) {
        return switch (sortField) {
            case MODEL -> new CarUtil.CarModelComparator();
            case YEAR -> new CarUtil.CarYearComparator();
            case POWER -> new CarUtil.CarPowerComparator();
            case ALL -> new CarUtil.CarGeneralComparator();
            case null, default -> throw new IllegalArgumentException("Невозможно выбрать компаратор для данного поля.");
        };
    }

    private ParityChecker<Car> getFieldParityChecker(CarFieldEnum sortField) {
        return switch (sortField) {
            case YEAR -> new CarUtil.CarProductionYearParityCheker();
            case POWER -> new CarUtil.CarPowerParityCheker();
            case null -> throw new IllegalArgumentException("Невозможно выбрать проверку четности для данного поля.");
            default -> null;
        };
    }

    @Override
    public boolean search() {
        CarFieldEnum searchField;
        Car searchValue;
        Comparator<Car> comparator;

        try {
            searchField = ConsoleUtil.getSearchField();
            searchValue = getSearchValue(searchField);
            comparator = getFieldComparator(searchField);
        } catch (IOException e) {
            System.out.println(e.getMessage() + " Поиск будет прекращён.");
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }

        List<Car> sortedData = this.sortAlgorithm.sort(this.rawData, comparator);
        Car result = this.searchAlgorithm.findByField(sortedData, searchValue, comparator);

        if (result == null) {
            return false;
        }
        this.processedData.clear();
        this.processedData.add(result);
        return true;

    }

    private Car getSearchValue(CarFieldEnum searchField) throws IOException {
        Car.CarBuilder builder;

        builder = switch (searchField) {
            case MODEL -> {
                String model = ConsoleUtil.getModelField();
                yield new Car.CarBuilder().setModel(model);
            }
            case POWER -> {
                Integer power = ConsoleUtil.getPowerField();
                yield new Car.CarBuilder().setPower(power);
            }
            case YEAR -> {
                Integer year = ConsoleUtil.getProductionYearField();
                yield new Car.CarBuilder().setProductionYear(year);
            }
            case ALL -> throw new IOException("Поиск возможен только по одиночному полю.");
            case null, default -> null;
        };

        if (builder == null)
            throw new IOException("Корректное поле для поиска не было выбрано.");

        return builder.build();
    }

    private static class ConsoleUtil {
        public static CarFieldEnum getSortField() throws IOException {
            return getCarField("сортировки");
        }

        public static CarFieldEnum getSearchField() throws IOException {
            return getCarField("поиска");
        }

        public static String getModelField() throws IOException {
            String model = getValue(String.class, CarFieldEnum.MODEL.getLocaleName(),
                    new CarUtil.CarModelValidator(), CarUtil.MODEL_INVALID_TEXT);

            if (model == null)
                throw new IOException("Не удалось считать модель.");

            return model;
        }

        public static Integer getPowerField() throws IOException {
            Integer power = getValue(Integer.class, CarFieldEnum.POWER.getLocaleName(),
                    new CarUtil.CarPowerValidator(), CarUtil.POWER_INVALID_TEXT);

            if (power == null)
                throw new IOException("Не удалось считать мощность.");

            return power;
        }

        public static Integer getProductionYearField() throws IOException {
            Integer year = getValue(Integer.class, CarFieldEnum.YEAR.getLocaleName(),
                    new CarUtil.CarProductionYearValidator(), CarUtil.YEAR_INVALID_TEXT);

            if (year == null)
                throw new IOException("Не удалось считать год выпуска.");

            return year;
        }

        private static CarFieldEnum getCarField(String fieldUsage) throws IOException {
            CarFieldEnum[] values = CarFieldEnum.values();
            final int minOrdinal = getViewOrdinal(values[0].ordinal());
            final int maxOrdinal = getViewOrdinal(values[values.length - 1].ordinal());

            StringBuilder requestTextBuilder = new StringBuilder("Выберите поля " + fieldUsage + ":");
            for (var field : values)
                requestTextBuilder.append("\n").append(field.getOrdinalLocaleName());

            Integer userInput = getValue(Integer.class, requestTextBuilder.toString(),
                    v -> v >= minOrdinal && v <= maxOrdinal, util.ConsoleUtil.CHOICE_INVALID_TEXT);

            if (userInput == null)
                throw new IOException("Не удалось выбрать поля " + fieldUsage + ".");

            return values[getOrdinalBy(userInput)];
        }
    }
}
