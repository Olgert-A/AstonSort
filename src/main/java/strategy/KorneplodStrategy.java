package strategy;

import data.entities.Car;
import data.entities.Korneplod;
import data.validate.Validator;
import view.ViewRepresentationEnum;

import java.io.*;
import java.util.List;

public class KorneplodStrategy extends AbstractStrategy<Korneplod> implements Strategy {
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
            if (!(line.equals("Korneplods"))) {
                System.out.println("Invalid file");
                return false;
            }

            int counter = 0;
            Validator<String> typeAndColorValidator = v -> v.length() > 3;
            Validator<Double> weightValidator = v -> v > 0;
            String type = null, color = null;
            double weight = 0;
            boolean entityFlag = false;
            while((line = bufferedReader.readLine()) != null && counter < amount) {
                String[] splitLine = line.split(":");
                switch (splitLine[0]) {
                    case "[Type" -> { type = splitLine[1].trim();
                    }
                    case "Weight" -> { weight = Double.valueOf(splitLine[1].trim());
                    }
                    case "Color" -> {
                        color = splitLine[1].substring(0, splitLine[1].length() - 1).trim();
                        entityFlag = true;
                        counter++;
                    }
                }
                if (entityFlag) {
                    entityFlag = false;
                    if (typeAndColorValidator.isValid(type) && weightValidator.isValid(weight)
                            && typeAndColorValidator.isValid(color)){
                        Korneplod korneplod = new Korneplod.KorneplodBuilder()
                                .setType(type)
                                .setWeight(weight)
                                .setColor(color)
                                .build();
                        rawData.add(korneplod);
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
                bufferedWriter.write("Korneplods");
                bufferedWriter.newLine();
            }
            bufferedReader.close();
            List<Korneplod> korneplodsList = processedData;
            for (Korneplod korneplod:korneplodsList) {
                bufferedWriter.write("[Type: ");
                bufferedWriter.write(korneplod.getType());
                bufferedWriter.newLine();
                bufferedWriter.write("Weight: ");
                bufferedWriter.write(String.valueOf(korneplod.getWeight()));
                bufferedWriter.newLine();
                bufferedWriter.write("Color: ");
                bufferedWriter.write(korneplod.getColor());
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
