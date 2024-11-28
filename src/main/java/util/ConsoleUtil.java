package util;

import data.util.Validate;

import util.enums.CarFieldEnum;
import util.enums.DataProcessEnum;
import util.enums.EntityEnum;
import util.enums.InputTypeEnum;

import util.enums.SortTypeEnum;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ConsoleUtil {
    public static final Scanner scanner = new Scanner(System.in);

    private static final List<Class<?>> INPUT_COMPATIBLE_CLASSES = List.of(Integer.class, Double.class, String.class);

    public static final int DEFAULT_READ_ATTEMPTS = 3;

    public static String CHOICE_INVALID_TEXT = "Ошибка: выберите одно из предлагаемых значений";
    public static String DATA_SIZE_INVALID_TEXT = "Ошибка: Размер массива должен быть больше 2";
    public static String FILE_NAME_INVALID_TEXT = "Ошибка: Имя файла должно быть больше 0";


    public static EntityEnum getDataType() throws IOException {
        EntityEnum[] values = EntityEnum.values();
        final int minOrdinal = values[0].ordinal();
        final int maxOrdinal = values[values.length-1].ordinal();

        StringBuilder requestText = new StringBuilder("Введите тип данных:");
        for(var dataType : values)
            requestText.append("\n").append(dataType.getOrdinalLocaleName());

        Integer userChoice = getValue(Integer.class, requestText.toString(),
                v -> v>=minOrdinal && v<= maxOrdinal, CHOICE_INVALID_TEXT);

        if(userChoice == null)
            throw new IOException("Не удалось выбрать тип данных.");

        return values[userChoice];
    }

    public static Integer getDataSize() throws IOException {
        Integer userChoice = getValue(Integer.class, "Введите размер массива данных:",
                v -> v > 2, DATA_SIZE_INVALID_TEXT);

        if(userChoice == null)
            throw new IOException("Не удалось выбрать размер массива данных.");

        return userChoice;
    }

    public static InputTypeEnum getInputType() throws IOException {
        InputTypeEnum[] values = InputTypeEnum.values();
        final int minOrdinal = values[0].ordinal();
        final int maxOrdinal = values[values.length-1].ordinal();

        StringBuilder requestText = new StringBuilder("Введите способ ввода данных:");
        for(var inputType : values)
            requestText.append("\n").append(inputType.getOrdinalLocaleName());

        Integer userChoice = getValue(Integer.class, requestText.toString(),
                v -> v>=minOrdinal && v<=maxOrdinal, CHOICE_INVALID_TEXT);

        if(userChoice == null)
            throw new IOException("Не удалось выбрать тип входных данных.");

        return values[userChoice];
    }

    public static String getFileName() throws IOException {
        String fileName = getValue(String.class, "Введите имя файла:",
                v -> !v.isEmpty(), FILE_NAME_INVALID_TEXT);

        if(fileName == null)
            throw new IOException("Не удалось выбрать имя файла.");

        return fileName;
    }

    public static DataProcessEnum getDataProcessType() throws IOException {
        DataProcessEnum[] values = DataProcessEnum.values();
        final int minOrdinal = values[0].ordinal();
        final int maxOrdinal = values[values.length-1].ordinal();

        StringBuilder requestText = new StringBuilder("Введите способ работы с данными:");
        for(var inputType : values)
            requestText.append("\n").append(inputType.getOrdinalLocaleName());

        Integer userChoice = getValue(Integer.class, requestText.toString(),
                v -> v>=minOrdinal && v<=maxOrdinal, CHOICE_INVALID_TEXT);

        if(userChoice == null)
            throw new IOException("Не удалось способ работы с данными.");

        return values[userChoice];
    }

    public static boolean shouldSaveToFile() {
        String requestText = """
                Сохранить в файл?
                0 - Да
                1 - Нет
                """;

        Integer userChoice = getValue(Integer.class, requestText, v -> v>=0 && v<2, CHOICE_INVALID_TEXT);

        if(userChoice == null){
            System.out.println("Не удалось выбрать вариант. Сохранение не будет произведено.");
            return false;
        }

        return userChoice == 0;
    }

    public static SortTypeEnum getSortType() throws IOException {
        return SortTypeEnum.SORT;
    }

    public static boolean shouldRestartSession() {
        String requestText = """
                Запустить сессию заново?
                0 - Да
                1 - Нет
                """;

        Integer userChoice = getValue(Integer.class, requestText, v -> v>=0 && v<2, CHOICE_INVALID_TEXT);

        if(userChoice == null){
            System.out.println("Не удалось выбрать вариант. Сохранение не будет произведено.");
            return false;
        }

        return userChoice == 0;
    }

    public static SortTypeEnum getSortType() throws Exception {
        SortTypeEnum sortType;
        StringBuilder requestTextBuilder = new StringBuilder("\nВыберите тип сортировки:");
        int fieldAmount = CarFieldEnum.values().length;

        for (var field : CarFieldEnum.values())
            requestTextBuilder.append("\n").append(field.getOrdinalLocaleName());

        Integer intUserInput = getValue(Integer.class, requestTextBuilder.toString(), v -> v >= 0 && v < fieldAmount,"Значение должно быть от 0 до " + (fieldAmount - 1));

        if (intUserInput == null) {
            System.out.println("Не удалось выбрать тип сортировки, операция будет прервана!");
            throw new Exception("ConsoleUtil.getSortType()");
        } else
            sortType = SortTypeEnum.values()[intUserInput];
        return sortType;
    }

    @SuppressWarnings("unckecked")
    public static <T> T getValue(Class<T> cls,
                                 String requestText) {
        return getValue(cls, requestText, null, null);
    }

    @SuppressWarnings("unckecked")
    public static <T> T getValue(Class<T> cls,
                                 String requestText,
                                 Validate<T> validate,
                                 String invalidText) {
        return getValue(cls, requestText, DEFAULT_READ_ATTEMPTS, validate, invalidText);
    }


    @SuppressWarnings("unckecked")
    public static <T> T getValue(Class<T> cls,
                                 String requestText,
                                 int readAttempts,
                                 Validate<T> validate,
                                 String invalidText) {
        if(!INPUT_COMPATIBLE_CLASSES.contains(cls))
            throw new IllegalArgumentException(cls.toString() + " is not compatible to use in getValue");

        T result = null;

        for(int attempt=0; attempt<readAttempts; attempt++) {
            System.out.println(requestText);
            String line = scanner.nextLine();

            try {
                if(cls.isAssignableFrom(Integer.class))
                    result = (T) Integer.valueOf(line);
                else if(cls.isAssignableFrom(Double.class))
                    result = (T) Double.valueOf(line);
                else if(cls.isAssignableFrom(String.class))
                    result = (T) line;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Введённое значение должно быть числом!\n");
                continue;
            }

            if(validate == null && invalidText == null)
                break;

            if(validate != null && invalidText != null)
                if(!validate.isValid(result)) {
                    System.out.println(invalidText);
                    result = null;
                }
                else
                    break;
        }

        return result;
    }
}
