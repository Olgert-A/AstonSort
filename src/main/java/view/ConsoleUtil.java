package view;

import data.validate.Validate;

import java.util.Scanner;

public class ConsoleUtil {
    public static final int DEFAULT_READ_ATTEMPTS = 3;

    public static Double getDoubleValue(String requestText) {
        return getDoubleValue(requestText, null, null);
    }

    public static Double getDoubleValue(String requestText,
                                        Validate<Double> validate,
                                        String invalidText) {
        return getDoubleValue(requestText, DEFAULT_READ_ATTEMPTS, validate, invalidText);
    }

    public static Double getDoubleValue(String requestText,
                                        int readAttempts,
                                        Validate<Double> validate,
                                        String invalidText) {
        try(Scanner scanner = new Scanner(System.in)) {
            for(int attempt=0; attempt<readAttempts; attempt++) {
                System.out.println(requestText);
                Double result = scanner.nextDouble();    
                
                if(validate != null && invalidText != null) 
                    if(validate.isValid(result)) 
                        return result;
                    else
                        System.out.println(invalidText);
            }
        }
        return null;
    }
}
