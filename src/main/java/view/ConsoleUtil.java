package view;

import data.validate.Validate;

import java.util.List;
import java.util.Scanner;

public class ConsoleUtil {
    private static final List<Class<?>> INPUT_COMPATIBLE_CLASSES = List.of(Integer.class, Double.class, String.class);
    public static final int DEFAULT_READ_ATTEMPTS = 3;


    public static <T> T getValue(Class<T> cls,
                                 String requestText) {
        return getValue(cls, requestText, null, null);
    }

    public static <T> T getValue(Class<T> cls,
                                 String requestText,
                                 Validate<T> validate,
                                 String invalidText) {
        return getValue(cls, requestText, DEFAULT_READ_ATTEMPTS, validate, invalidText);
    }



    public static <T> T getValue(Class<T> cls,
                                 String requestText,
                                 int readAttempts,
                                 Validate<T> validate,
                                 String invalidText) {
        if(!INPUT_COMPATIBLE_CLASSES.contains(cls))
            throw new IllegalArgumentException(cls.toString() + " is not compatible to use in getValue");

        T result = null;

        try(Scanner scanner = new Scanner(System.in)) {
            for(int attempt=0; attempt<readAttempts; attempt++) {
                System.out.println(requestText);
                String line = scanner.nextLine();

                if(cls.isAssignableFrom(Integer.class))
                    result = (T) Integer.valueOf(line);
                else if(cls.isAssignableFrom(Double.class))
                    result = (T) Double.valueOf(line);
                else if(cls.isAssignableFrom(String.class))
                    result = (T) line;

                if(validate != null && invalidText != null)
                    if(!validate.isValid(result)) {
                        System.out.println(invalidText);
                        result = null;
                    }
                    else
                        break;
            }
        }

        return result;
    }
}
