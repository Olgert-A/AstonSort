package util;

import data.util.Validate;
import util.enums.CarFieldEnum;
import util.enums.SortTypeEnum;

import java.util.List;
import java.util.Scanner;

public class ConsoleUtil {
    private static final List<Class<?>> INPUT_COMPATIBLE_CLASSES = List.of(Integer.class, Double.class, String.class);
    public static final int DEFAULT_READ_ATTEMPTS = 3;
    private static final Scanner scanner = new Scanner(System.in);

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
