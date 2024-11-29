package strategy;

import data.entities.Korneplod;

import data.util.KorneplodUtil;

import data.util.ParityChecker;
import data.util.Validate;

import util.enums.KorneplodFieldEnum;
import util.enums.SortTypeEnum;

import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static util.ConsoleUtil.getValue;
import data.search.BinarySearch;
import view.KorneplodFieldEnum;
import view.ViewRepresentationEnum;
import java.util.Comparator;
import java.util.List;

import static view.KorneplodFieldEnum.*;


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

            if (strUserInput == null) {
                System.out.println("Не удалось считать тип, ввод объекта будет пропущен");
                continue;
            } else
                type = strUserInput;

            doubleUserInput = getValue(Double.class, KorneplodFieldEnum.WEIGHT.getLocaleName(),
                    Objects::nonNull, "Вес неверный");

            if (doubleUserInput == null) {
                System.out.println("Не удалось считать вес, ввод объекта будет пропущен");
                continue;
            } else
                weight = doubleUserInput;

            strUserInput = getValue(String.class, KorneplodFieldEnum.COLOR.getLocaleName(),
                    Objects::nonNull, "цвет неверный");

            if (strUserInput == null) {
                System.out.println("Не удалось считать цвет, ввод объекта будет пропущен");
                continue;
            } else
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
        if (amount <= 0) return false;
        List<String> typeList = List.of("Potato", "Carrot", "Beet", "Onion", "Celery", "Turnip", "Garlic", "Radish");
        List<String> colorList = List.of("Blue", "White", "Black", "Yellow", "Green", "Brown", "Purple", "Orange");
        double minWeight = 1;
        double maxWeight = 10;

        Random random = new Random();

        String type;
        String color;
        double weight;

        while (rawData.size() < amount) {
            type = typeList.get(random.nextInt(typeList.size()));
            color = colorList.get(random.nextInt(colorList.size()));
            weight = minWeight + random.nextDouble(maxWeight - minWeight + 1);

            if (new KorneplodUtil.KorneplodTypeValidator().isValid(type) && new KorneplodUtil.KorneplodColorValidator().isValid(color)
                    && new KorneplodUtil.KorneplodWeightValidator().isValid(weight)) {
                Korneplod korneplod = new Korneplod.KorneplodBuilder().setType(type).setColor(color).setWeight(weight).build();
                this.rawData.add(korneplod);
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
    public boolean sort(SortTypeEnum sortType) {
        try {
            KorneplodFieldEnum sortField = ConsoleUtil.getSortField();
            sortByField(sortType, getFieldComparator(sortField), getFieldParityChecker(sortField));
        } catch (Exception e) {
           throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    private Comparator<Korneplod> getFieldComparator(KorneplodFieldEnum sortField) {
        switch (sortField) {
            case TYPE -> {
                return new KorneplodUtil.KorneplodTypeComparator();
            }
            case COLOR -> {
                return new KorneplodUtil.KorneplodColorComparator();
            }
            case WEIGHT -> {
                return new KorneplodUtil.KorneplodWeightComparator();
            }
            case ALL -> {
                return new KorneplodUtil.KorneplodGeneralComparator();
            }
        }
        return null;
    }

    private ParityChecker<Korneplod> getFieldParityChecker(KorneplodFieldEnum sortField) {
        return null;
    }

    @Override
    public <T> void searchByField(ViewRepresentationEnum field, T fieldValue) {
      if (!(field instanceof Korneplod)){
          return;
      }
        KorneplodFieldEnum korneplodFieldEnum = (KorneplodFieldEnum) field;
        if (korneplodFieldEnum == TYPE ){
            if (!(fieldValue instanceof String)){
                return;
            }
            Korneplod searchingKorneplod = new Korneplod.KorneplodBuilder()
                    .setType((String)fieldValue)
                    .build();
            Comparator <Korneplod> comparator = Comparator.comparing(Korneplod::getType);
            Korneplod result = binarySearch.findByField(rawData, searchingKorneplod, comparator);
            System.out.println(result);
            return;
        }
        if (korneplodFieldEnum == COLOR){
            if (!(fieldValue instanceof String)){
                return;
            }
            Korneplod searchingKorneplod = new Korneplod.KorneplodBuilder()
                    .setColor((String)fieldValue)
                    .build();
            Comparator <Korneplod> comparator = Comparator.comparing(Korneplod::getColor);
            Korneplod result = binarySearch.findByField(rawData, searchingKorneplod, comparator);
            System.out.println(result);
            return;
        }
        if (korneplodFieldEnum == WEIGHT){
            if (!(fieldValue instanceof Integer)){
                return;
            }
            Korneplod searchingKorneplod = new Korneplod.KorneplodBuilder()
                    .setWeight((Integer)fieldValue)
                    .build();
            Comparator <Korneplod> comparator = Comparator.comparing(Korneplod::getWeight);
            Korneplod result = binarySearch.findByField(rawData, searchingKorneplod, comparator);
            System.out.println(result);
            return;

    private static class ConsoleUtil {
        public static KorneplodFieldEnum getSortField() throws Exception {
            KorneplodFieldEnum sortField;
            StringBuilder requestTextBuilder = new StringBuilder("\nВыберите поля сортировки:");
            int fieldAmount = KorneplodFieldEnum.values().length;

            for (var field : KorneplodFieldEnum.values())
                requestTextBuilder.append("\n").append(field.getOrdinalLocaleName());
        }
    }




      @Override
    public boolean search (){
        KorneplodFieldEnum searchField = KorneplodStrategy.KorneplodConsoleUtil.getSearchField();

        if (searchField == null){
            System.out.println("No search field found");
            return false;
        }
        Korneplod searchValue =  getSearchValue(searchField);
        if (searchValue != null){
            System.out.println( "что-то пошло не так");
            return false;
        }
        Comparator <Korneplod> comparator = getFieldComparator(searchField);
        if (comparator == null){

            System.out.println("Компаратор не найден!");
            return false;
        }
        List<Korneplod> sortedData = this.sortAlgorithm(this.rawData, comparator);
        Korneplod result = this.searchAlgorithm.findByField(sortedData, searchValue, comparator);

        if (result == null){
            return false;
        }
        this.processedData.clear();
        this.processedData.add(result);
        return true;

    }

    private List<Korneplod> sortAlgorithm(List<Korneplod> rawData, Comparator<Korneplod> comparator) {
        return null;
    }

    private Comparator <Korneplod> getFieldComparator(KorneplodFieldEnum field) {

        switch (field) {
            case TYPE ->{
                return Comparator.comparing(Korneplod::getType);
            }
            case COLOR ->{
                return Comparator.comparing(Korneplod::getColor);
            }
            case WEIGHT ->{
                return Comparator.comparing(Korneplod::getWeight);
            }
            default ->
            {
                return null;
            }
        }

    }
    private Korneplod getSearchValue (KorneplodFieldEnum searchField) {
        Korneplod.KorneplodBuilder builder = new Korneplod.KorneplodBuilder();
        switch (searchField) {
            case TYPE ->{
                String type = KorneplodStrategy.KorneplodConsoleUtil.getType();
                if (type == null) {
                    return null;
                }
                return builder.setType(type).build();
            }
            case COLOR ->{
                String color = KorneplodStrategy.KorneplodConsoleUtil.getColor();
                if (color == null) {
                    return null;
                }
                return builder.setColor(color).build();
            }
            case WEIGHT ->{
                Double weight = KorneplodStrategy.KorneplodConsoleUtil.getWeight();
                if (weight == null) {
                    return null;
                }
                return builder.setWeight(weight).build();
            }
            case ALL -> {
                System.out.println("нельзя искать всё!");
                return null;
            }
            default -> {
                return null;
            }
        }

    }


            Integer intUserInput = getValue(Integer.class, requestTextBuilder.toString(),
                    v -> v >= 0 && v < fieldAmount, "Значение должно быть от 0 до " + (fieldAmount - 1));

            if (intUserInput == null) {
                System.out.println("Не удалось выбрать поля сортировки, операция будет прервана!");
                throw new Exception("return false");
            } else
                sortField = KorneplodFieldEnum.values()[intUserInput];
            return sortField;
        }
    }
    public static class KorneplodConsoleUtil {
        public static KorneplodFieldEnum getSearchField() {
            return null;
        }
        public static String getType(){
            return null;
        }
        public static Double getWeight(){
            return null;
        }
        public static String getColor(){
            return null;
        }

    }
}
