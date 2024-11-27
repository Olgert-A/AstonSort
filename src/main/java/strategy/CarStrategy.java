package strategy;

import data.entities.Book;
import data.entities.Car;
import data.validate.Validator;
import view.ViewRepresentationEnum;

import java.io.*;
import java.util.List;

public class CarStrategy extends AbstractStrategy<Car> implements Strategy {
    @Override
    public void collectInputData(int amount) {

    }

    @Override
    public void collectRandomData(int amount) {

    }

    @Override
    public boolean collectDataFromFile(String name, int amount) {
        try (FileReader fileReader = new FileReader(name);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            line = bufferedReader.readLine();
            if (!(line.equals("Cars"))) {
                System.out.println("Invalid file");
                return false;
            }

            int counter = 0;
            Validator<String> modelValidator = v -> v.length() > 3;
            Validator<Integer> powerValidator = v -> v > 0 && v <= 1000;
            Validator <Integer> productionYearValidator = v -> v > 2000;
            String model = null;
            int power = 0, productionYear = 0;
            boolean entityFlag = false;
            while((line = bufferedReader.readLine()) != null && counter < amount) {
                String[] splitLine = line.split(":");
                switch (splitLine[0]) {
                    case "[Model" -> { model = splitLine[1].trim();
                    }
                    case "Power" -> { power = Integer.valueOf(splitLine[1].trim());
                    }
                    case "Production year" -> {
                        String intLine = splitLine[1].substring(0, splitLine[1].length() - 1).trim();
                        productionYear = Integer.valueOf(intLine, 10);
                        entityFlag = true;
                        counter++;
                    }
                }
                if (entityFlag) {
                    entityFlag = false;
                    if (modelValidator.isValid(model) && powerValidator.isValid(power)
                            && productionYearValidator.isValid(productionYear)){
                        Car car = new Car.CarBuilder()
                                .setModel(model)
                                .setPower(power)
                                .setProductionYear(productionYear)
                                .build();
                        rawData.add(car);
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
            List<Car> carsList = processedData;
            for (Car car:carsList) {
                bufferedWriter.write("[Model: ");
                bufferedWriter.write(car.getModel());
                bufferedWriter.newLine();
                bufferedWriter.write("Power: ");
                bufferedWriter.write(car.getPower());
                bufferedWriter.newLine();
                bufferedWriter.write("Production year: ");
                bufferedWriter.write(car.getProductionYear());
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
    public void sortBy(ViewRepresentationEnum field, boolean sortOnlyEven) {

    }

    @Override
    public void sortByAllFields(boolean sortOnlyEven) {

    }

    @Override
    public void searchByField(ViewRepresentationEnum field, Number fieldValue) {

    }

    @Override
    public void showResultsData() {

    }
}
