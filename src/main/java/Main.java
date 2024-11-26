import strategy.BookStrategy;
import strategy.CarStrategy;
import strategy.KorneplodStrategy;
import strategy.Strategy;
import view.ClassReprEnum;
import static view.ConsoleUtil.getValue;

public class Main {
    public static int DATA_TYPES_AMOUNT = ClassReprEnum.values().length;
    public static String DATA_TYPES_INVALID_TEXT = "Значение должно быть от 0 до " + (DATA_TYPES_AMOUNT-1);
    public static String DATA_SIZE_INVALID_TEXT = "Размер массива должен быть больше 2";

    public static String getDataTypeRequestText() {
        StringBuilder requestTextBuilder = new StringBuilder("Введите тип данных:");
        for(var dataType : ClassReprEnum.values())
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
            Integer inputResult;

            inputResult = getValue(Integer.class, getDataTypeRequestText(),
                    v -> v>=0 && v<DATA_TYPES_AMOUNT, DATA_TYPES_INVALID_TEXT);

            switch (inputResult) {
                case 0 -> strategy = new CarStrategy();
                case 1 -> strategy = new BookStrategy();
                case 2 -> strategy = new KorneplodStrategy();
                case null, default -> {
                    System.out.println("Не удалось выбрать тип данных, сессия будет перезапущена!");
                    continue;
                }
            }

            inputResult = getValue(Integer.class, "Введите размер массива данных:",
                    v -> v > 2, DATA_SIZE_INVALID_TEXT);

            if(inputResult == null) {
                System.out.println("Не удалось выбрать тип данных, сессия будет перезапущена!");
                continue;
            }
            else
                dataAmount = inputResult;


            inputResult = getValue(Integer.class, getInputTypeRequestText(),
                    v -> v>=0 && v<3, "Значение должно быть от 0 до 2");

            switch (inputResult) {
                case 0 -> strategy.collectInputData(dataAmount);
                case 1 -> strategy.collectRandomData(dataAmount);
                case 2 -> {
                    String fileName = getValue(String.class, "Введите имя файла:");
                    if(fileName != null)
                        strategy.collectDataFromFile(fileName, dataAmount);
                }
                case null, default -> {
                    System.out.println("Не удалось выбрать тип данных, сессия будет перезапущена!");
                    continue;
                }
            }

            createNewSession = false;
        } while (createNewSession);
    }
}
