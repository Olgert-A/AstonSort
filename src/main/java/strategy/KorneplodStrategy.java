package strategy;

import data.entities.Book;
import data.entities.Korneplod;
import data.util.Validate;
import util.enums.KorneplodFieldEnum;

import java.io.*;
import java.util.List;
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

            if(strUserInput == null) {
                System.out.println("Не удалось считать тип, ввод объекта будет пропущен");
                continue;
            }
            else
                type = strUserInput;

            doubleUserInput = getValue(Double.class, KorneplodFieldEnum.WEIGHT.getLocaleName(),
                    Objects::nonNull, "Вес неверный");

            if(doubleUserInput == null) {
                System.out.println("Не удалось считать вес, ввод объекта будет пропущен");
                continue;
            }
            else
                weight = doubleUserInput;

            strUserInput = getValue(String.class, KorneplodFieldEnum.COLOR.getLocaleName(),
                    Objects::nonNull, "цвет неверный");

            if (strUserInput == null) {
                System.out.println("Не удалось считать цвет, ввод объекта будет пропущен");
                continue;
            }
            else
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

        try (FileReader fileReader = new FileReader(name);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            line = bufferedReader.readLine();
            if (line.isEmpty()) {
                while (line.isEmpty()) {
                    line = bufferedReader.readLine();
                }
            }
            if (!(line.equals("Korneplods")) || line == null) {
                System.out.println("Invalid file");
                return false;
            }

            int counter = 0;
            Validate<String> typeAndColorValidator = v -> v.length() > 5;
            Validate <Double> weightValidator = v -> v > 0;
            String type = null, color = null;
            double weight = 0;
            boolean startFlag = false;

            while((line = bufferedReader.readLine()) != null && counter < amount) {
                if (line.trim().startsWith("[")){
                    startFlag = true;
                    type = null;
                    weight = 0;
                    color = null;
                }

                if (line.trim().endsWith("]")) {
                    startFlag = false;
                    if (type != null && color != null & weight != 0) {
                        if (typeAndColorValidator.isValid(type) && weightValidator.isValid(weight)
                                && typeAndColorValidator.isValid(color)){
                            Korneplod korneplod = new Korneplod.KorneplodBuilder()
                                    .setType(type)
                                    .setWeight(weight)
                                    .setColor(color)
                                    .build();
                            rawData.add(korneplod);
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
                        case "Type" -> {
                            type = splitLine[1].trim();
                        }
                        case "Weight" -> {
                            weight = Double.valueOf(splitLine[1].trim());
                        }
                        case "Color" -> {
                            color = splitLine[1].trim();
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
                bufferedWriter.write("Korneplods");
                bufferedWriter.newLine();
            }
            bufferedReader.close();
            List<Korneplod> korneplodsList = processedData;
            for (Korneplod korneplod:korneplodsList) {
                bufferedWriter.write("[");
                bufferedWriter.newLine();
                bufferedWriter.write("Type: ");
                bufferedWriter.write(korneplod.getType());
                bufferedWriter.newLine();
                bufferedWriter.write("Weight: ");
                bufferedWriter.write(String.valueOf(korneplod.getWeight()));
                bufferedWriter.newLine();
                bufferedWriter.write("Color: ");
                bufferedWriter.write(korneplod.getColor());
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
