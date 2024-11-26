import strategy.BookStrategy;
import strategy.CarStrategy;
import strategy.KorneplodStrategy;
import strategy.Strategy;
import view.EntityEnum;
import static view.ConsoleUtil.getValue;

public class Main {
    public static int DATA_TYPES_AMOUNT = EntityEnum.values().length;
    public static String DATA_TYPES_INVALID_TEXT = "Ошибка: Значение должно быть от 0 до " + (DATA_TYPES_AMOUNT-1);
    public static String DATA_SIZE_INVALID_TEXT = "Ошибка: Размер массива должен быть больше 2";

    public static String getDataTypeRequestText() {
        StringBuilder requestTextBuilder = new StringBuilder("Введите тип данных:");
        for(var dataType : EntityEnum.values())
            requestTextBuilder.append("\n").append(dataType.getRepresentation());
        return requestTextBuilder.toString();
    }

    public static String getInputTypeRequestText() {
        return """
                
                Введите способ ввода данных:
                0 - Ручной ввод
                1 - Случайное заполнение
                2 - Чтение из файла""";
    }

    public static void main(String[] args) {
        boolean createNewSession = true;

        do {
            Strategy strategy;
            int dataAmount;
            Integer userChoise;

            System.out.println("\nНачата новая сессия!\n");

            userChoise = getValue(Integer.class, getDataTypeRequestText(),
                    v -> v>=0 && v<DATA_TYPES_AMOUNT, DATA_TYPES_INVALID_TEXT);

            switch (userChoise) {
                case 0 -> strategy = new CarStrategy();
                case 1 -> strategy = new BookStrategy();
                case 2 -> strategy = new KorneplodStrategy();
                case null, default -> {
                    System.out.println("Не удалось выбрать тип данных, сессия будет перезапущена!");
                    continue;
                }
            }

            userChoise = getValue(Integer.class, "\nВведите размер массива данных:",
                    v -> v > 2, DATA_SIZE_INVALID_TEXT);

            if(userChoise == null) {
                System.out.println("Не удалось выбрать тип данных, сессия будет перезапущена!");
                continue;
            }
            else
                dataAmount = userChoise;

            userChoise = getValue(Integer.class, getInputTypeRequestText(),
                    v -> v>=0 && v<3, "Значение должно быть от 0 до 2");

            boolean isDataCollected;
            switch (userChoise) {
                case 0 -> isDataCollected = strategy.collectInputData(dataAmount);
                case 1 -> isDataCollected = strategy.collectRandomData(dataAmount);
                case 2 -> {
                    String fileName = getValue(String.class, "Введите имя файла:");
                    if(fileName != null)
                        isDataCollected = strategy.collectDataFromFile(fileName, dataAmount);
                    else {
                        System.out.println("Не удалось прочитать имя файла, сессия будет перезапущена!");
                        continue;
                    }
                }
                case null, default -> {
                    isDataCollected = false;
                    System.out.println("Не удалось выбрать тип данных, сессия будет перезапущена!");
                    continue;
                }
            }

            if(!isDataCollected) {
                System.out.println("Данные не были собраны, сессия будет перезапущена!");
                continue;
            }

            boolean continueWorkWithData = true;
            do {
                if(strategy.sort()) {
                    strategy.showResults();
                    String fileName = getValue(String.class, "Введите имя файла для сохранения",
                            s -> !s.isEmpty(), "Ошибка: Имя файла должно содержать хотя бы 1 символ");

                    if(fileName == null)
                        System.out.println("Не удалось прочитать имя файла, запись в файл будет пропущена");
                    else
                        strategy.saveResultsToFile(fileName);
                }

                if(strategy.search()) {
                    strategy.showResults();
                    String fileName = getValue(String.class, "Введите имя файла для сохранения",
                            s -> !s.isEmpty(), "Ошибка: Имя файла должно содержать хотя бы 1 символ");

                    if(fileName == null)
                        System.out.println("Не удалось прочитать имя файла, запись в файл будет пропущена");
                    else
                        strategy.saveResultsToFile(fileName);
                }

                String userAnswer = getValue(String.class, "Продолжить работать с данными? (Y/N)");
                if(userAnswer == null || !userAnswer.equals("Y"))
                    continueWorkWithData = false;

            } while (continueWorkWithData);

            String userAnswer = getValue(String.class, "Запустить сессию заново? (Y/N");
            if(userAnswer == null || !userAnswer.equals("Y"))
                createNewSession = false;
        } while (createNewSession);
    }
}
