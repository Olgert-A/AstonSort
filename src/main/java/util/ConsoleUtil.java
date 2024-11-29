package util;

import data.util.Validate;

import util.enums.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static util.enums.ViewOrdinalUtil.*;

public class ConsoleUtil {
    public static final Scanner scanner = new Scanner(System.in);

    private static final List<Class<?>> INPUT_COMPATIBLE_CLASSES = List.of(Integer.class, Double.class, String.class);

    public static final int DEFAULT_READ_ATTEMPTS = 3;

    public static final String CHOICE_INVALID_TEXT = "Выберите одно из предлагаемых значений";
    public static final String DATA_SIZE_INVALID_TEXT = "Размер массива должен быть больше 2";
    public static final String FILE_NAME_INVALID_TEXT = "Имя файла должно быть больше 0";


    public static EntityEnum getDataType() throws IOException {
        EntityEnum[] values = EntityEnum.values();
        final int minOrdinal = getViewOrdinal(values[0].ordinal());
        final int maxOrdinal = getViewOrdinal(values[values.length-1].ordinal());

        StringBuilder requestText = new StringBuilder("Введите тип данных:");
        for(var dataType : values)
            requestText.append("\n").append(dataType.getOrdinalLocaleName());

        Integer userChoice = getValue(Integer.class, requestText.toString(),
                v -> v>=minOrdinal && v<= maxOrdinal, CHOICE_INVALID_TEXT);

        if(userChoice == null)
            throw new IOException("Не удалось выбрать тип данных.");

        return values[getOrdinalBy(userChoice)];
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
        final int minOrdinal = getViewOrdinal(values[0].ordinal());
        final int maxOrdinal = getViewOrdinal(values[values.length-1].ordinal());

        StringBuilder requestText = new StringBuilder("Введите способ ввода данных:");
        for(var inputType : values)
            requestText.append("\n").append(inputType.getOrdinalLocaleName());

        Integer userChoice = getValue(Integer.class, requestText.toString(),
                v -> v>=minOrdinal && v<=maxOrdinal, CHOICE_INVALID_TEXT);

        if(userChoice == null)
            throw new IOException("Не удалось выбрать тип входных данных.");

        return values[getOrdinalBy(userChoice)];
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
        final int minOrdinal = getViewOrdinal(values[0].ordinal());
        final int maxOrdinal = getViewOrdinal(values[values.length-1].ordinal());

        StringBuilder requestText = new StringBuilder("Введите способ работы с данными:");
        for(var inputType : values)
            requestText.append("\n").append(inputType.getOrdinalLocaleName());

        Integer userChoice = getValue(Integer.class, requestText.toString(),
                v -> v>=minOrdinal && v<=maxOrdinal, CHOICE_INVALID_TEXT);

        if(userChoice == null)
            throw new IOException("Не удалось способ работы с данными.");

        return values[getOrdinalBy(userChoice)];
    }

    public static boolean shouldSaveToFile() {
        YesNoEnum[] values = YesNoEnum.values();
        final int minOrdinal = getViewOrdinal(values[0].ordinal());
        final int maxOrdinal = getViewOrdinal(values[values.length-1].ordinal());

        StringBuilder requestText = new StringBuilder("Сохранить в файл?");
        for(var dataType : values)
            requestText.append("\n").append(dataType.getOrdinalLocaleName());

        Integer userChoice = getValue(Integer.class, requestText.toString(),
                v -> v>=minOrdinal && v<= maxOrdinal, CHOICE_INVALID_TEXT);

        if(userChoice == null){
            System.out.println("Не удалось выбрать вариант. Сохранение не будет произведено.");
            return false;
        }

        return YesNoEnum.YES.ordinal() == getOrdinalBy(userChoice);
    }

    public static boolean shouldRestartSession() {
        YesNoEnum[] values = YesNoEnum.values();
        final int minOrdinal = getViewOrdinal(values[0].ordinal());
        final int maxOrdinal = getViewOrdinal(values[values.length-1].ordinal());

        StringBuilder requestText = new StringBuilder("Запустить сессию заново?");
        for(var dataType : values)
            requestText.append("\n").append(dataType.getOrdinalLocaleName());

        Integer userChoice = getValue(Integer.class, requestText.toString(),
                v -> v>=minOrdinal && v<= maxOrdinal, CHOICE_INVALID_TEXT);

        if(userChoice == null){
            System.out.println("Не удалось выбрать вариант. Сохранение не будет произведено.");
            return false;
        }

        return YesNoEnum.YES.ordinal() == getOrdinalBy(userChoice);
    }

    public static SortTypeEnum getSortType() throws IOException {
        SortTypeEnum[] values = SortTypeEnum.values();
        final int minOrdinal = getViewOrdinal(values[0].ordinal());
        final int maxOrdinal = getViewOrdinal(values[values.length-1].ordinal());

        StringBuilder requestText = new StringBuilder("Выберите тип сортировки:");
        for(var dataType : values)
            requestText.append("\n").append(dataType.getOrdinalLocaleName());

        Integer userChoice = getValue(Integer.class, requestText.toString(),
                v -> v>=minOrdinal && v<= maxOrdinal, CHOICE_INVALID_TEXT);

        if(userChoice == null)
            throw new IOException("Не удалось выбрать тип сортировки.");

        return values[getOrdinalBy(userChoice)];
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
