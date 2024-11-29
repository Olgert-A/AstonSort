package strategy;

import data.entities.Korneplod;
import data.util.KorneplodUtil;
import data.util.ParityChecker;
import util.enums.KorneplodFieldEnum;
import util.enums.SortTypeEnum;


import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static util.ConsoleUtil.getValue;
import static util.enums.ViewOrdinalUtil.*;


public class KorneplodStrategy extends AbstractStrategy<Korneplod> implements Strategy {
    @Override
    public boolean collectInputData(int amount) {
        int korneplodCount = 0;

        do {
            String type, color;
            double weight;

            System.out.println("\nЗаполните кореплод №" + (korneplodCount + 1));
            try {
                type = ConsoleUtil.getType();
                weight = ConsoleUtil.getWeight();
                color = ConsoleUtil.getColor();
            } catch (IOException e) {
                System.out.println();
                System.out.println(e.getMessage() + " Заполнение корнеплода начнётся с начала");
                System.out.println();
                continue;
            }

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

            if (new KorneplodUtil.KorneplodTypeValidator().isValid(type) &&
                    new KorneplodUtil.KorneplodColorValidator().isValid(color) &&
                    new KorneplodUtil.KorneplodWeightValidator().isValid(weight)) {

                Korneplod korneplod = new Korneplod.KorneplodBuilder()
                        .setType(type)
                        .setColor(color)
                        .setWeight(weight)
                        .build();
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
                System.out.println("Файл не содержит данных выбранного типа.");
                return false;
            }

            int counter = 0;
            String type = null, color = null;
            double weight = 0;
            boolean startFlag = false;

            while ((line = bufferedReader.readLine()) != null && counter < amount) {
                if (line.trim().startsWith("[")) {
                    startFlag = true;
                    type = null;
                    weight = 0;
                    color = null;
                }

                if (line.trim().endsWith("]")) {
                    startFlag = false;
                    if (type != null && color != null & weight != 0) {
                        if (new KorneplodUtil.KorneplodTypeValidator().isValid(type) && new KorneplodUtil.KorneplodWeightValidator().isValid(weight) && new KorneplodUtil.KorneplodColorValidator().isValid(color)) {
                            Korneplod korneplod = new Korneplod.KorneplodBuilder()
                                    .setType(type)
                                    .setWeight(weight)
                                    .setColor(color)
                                    .build();
                            rawData.add(korneplod);
                            counter++;
                        }
                    }
                }

                String[] splitLine = line.split(":");

                if (startFlag && splitLine.length > 1) {
                    switch (splitLine[0].trim()) {
                        case "Type" -> type = splitLine[1].trim();
                        case "Weight" -> weight = Double.parseDouble(splitLine[1].trim());
                        case "Color" -> color = splitLine[1].trim();
                    }
                }
            }
            bufferedReader.close();
            if (counter < amount) {
                System.out.println();
                System.out.println("Файл закончился раньше, чем массив заполнился. Прочитано " + counter + " корнеплодов.");
                System.out.println();
            }
            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return false;

    }

    @Override
    public boolean saveResultsToFile(String name) {
        try (FileWriter fileWriter = new FileWriter(name, true);
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
            bufferedWriter.newLine();
            bufferedWriter.write(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy : HH-mm")));
            bufferedWriter.newLine();
            bufferedReader.close();
            List<Korneplod> korneplodsList = processedData;
            for (Korneplod korneplod : korneplodsList) {
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
        } catch (IOException e) {
            System.out.println();
            System.out.println(e.getMessage() + " Сортировка будет прекращена.");
            System.out.println();
            return false;
        }

        return true;
    }

    @Override
    public boolean search() {
        KorneplodFieldEnum searchField;
        Korneplod searchValue;
        Comparator<Korneplod> comparator;

        try {
            searchField = ConsoleUtil.getSearchField();
            searchValue = getSearchValue(searchField);
            comparator = getFieldComparator(searchField);
        } catch (IOException e) {
            System.out.println();
            System.out.println(e.getMessage() + " Поиск будет прекращён.");
            System.out.println();
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }

        List<Korneplod> sortedData = this.sortAlgorithm.sort(this.rawData, comparator);
        Korneplod result = this.searchAlgorithm.findByField(sortedData, searchValue, comparator);

        if (result == null) {
            return false;
        }
        this.processedData.clear();
        this.processedData.add(result);
        return true;

    }

    private Comparator<Korneplod> getFieldComparator(KorneplodFieldEnum sortField) {
        return switch (sortField) {
            case TYPE -> new KorneplodUtil.KorneplodTypeComparator();
            case COLOR -> new KorneplodUtil.KorneplodColorComparator();
            case WEIGHT -> new KorneplodUtil.KorneplodWeightComparator();
            case ALL -> new KorneplodUtil.KorneplodGeneralComparator();
            case null, default -> throw new IllegalArgumentException("Невозможно выбрать компаратор для данного поля.");
        };
    }

    private ParityChecker<Korneplod> getFieldParityChecker(KorneplodFieldEnum sortField) {
        return switch (sortField) {
            case null -> throw new IllegalArgumentException("Невозможно выбрать проверку четности для данного поля.");
            default -> null;
        };
    }

    private Korneplod getSearchValue(KorneplodFieldEnum searchField) throws IOException {
        Korneplod.KorneplodBuilder builder;

        builder = switch (searchField) {
            case TYPE -> {
                String type = ConsoleUtil.getType();
                yield new Korneplod.KorneplodBuilder().setType(type);
            }
            case COLOR -> {
                String color = ConsoleUtil.getColor();
                yield new Korneplod.KorneplodBuilder().setColor(color);
            }
            case WEIGHT -> {
                Double weight = ConsoleUtil.getWeight();
                yield new Korneplod.KorneplodBuilder().setWeight(weight);
            }
            case ALL -> throw new IOException("Поиск возможен только по одиночному полю.");
            case null, default -> null;
        };

        if (builder == null)
            throw new IOException("Корректное поле для поиска не было выбрано.");

        return builder.build();
    }


    private static class ConsoleUtil {
        public static KorneplodFieldEnum getSortField() throws IOException {
            return getKorneplodField("сортировки");
        }

        public static KorneplodFieldEnum getSearchField() throws IOException {
            return getKorneplodField("поиска");
        }

        public static String getType() throws IOException {
            String type = getValue(String.class, KorneplodFieldEnum.TYPE.getLocaleName(),
                    new KorneplodUtil.KorneplodTypeValidator(), KorneplodUtil.TYPE_INVALID_TEXT);

            if (type == null)
                throw new IOException("Не удалось считать тип.");

            return type;
        }

        public static Double getWeight() throws IOException {
            Double weight = getValue(Double.class, KorneplodFieldEnum.WEIGHT.getLocaleName(),
                    new KorneplodUtil.KorneplodWeightValidator(), KorneplodUtil.WEIGHT_INVALID_TEXT);

            if (weight == null)
                throw new IOException("Не удалось считать вес");

            return weight;
        }

        public static String getColor() throws IOException {
            String color = getValue(String.class, KorneplodFieldEnum.COLOR.getLocaleName(),
                    new KorneplodUtil.KorneplodColorValidator(), KorneplodUtil.COLOR_INVALID_TEXT);

            if (color == null)
                throw new IOException("Не удалось считать цвет.");

            return color;
        }

        private static KorneplodFieldEnum getKorneplodField(String fieldUsage) throws IOException {
            KorneplodFieldEnum[] values = KorneplodFieldEnum.values();
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
