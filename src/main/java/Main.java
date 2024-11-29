import strategy.BookStrategy;
import strategy.CarStrategy;
import strategy.KorneplodStrategy;
import strategy.Strategy;
import util.ConsoleUtil;
import util.enums.DataProcessEnum;
import util.enums.EntityEnum;
import util.enums.InputTypeEnum;
import util.enums.SortTypeEnum;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        boolean isSessionContinued;

        do {
            try {
                System.out.println("\nНачата новая сессия!\n");

                EntityEnum dataType = ConsoleUtil.getDataType();
                Strategy strategy = getEntityStrategy(dataType);
                Integer dataSize = ConsoleUtil.getDataSize();
                InputTypeEnum inputType = ConsoleUtil.getInputType();
                collectData(inputType, dataSize, strategy);
                strategy.showCollectedData();

                boolean isDataProcessing = true;
                do {
                    DataProcessEnum dataProcessType = ConsoleUtil.getDataProcessType();

                    switch (dataProcessType) {
                        case SORT -> sort(strategy);
                        case SEARCH -> search(strategy);
                        case NOTHING -> isDataProcessing = false;
                    }
                } while (isDataProcessing);
            }
            catch (IOException e) {
                System.out.println(e.getMessage() + " Сессия будет перезапущена!");
            }

            isSessionContinued = ConsoleUtil.shouldRestartSession();

        } while (isSessionContinued);
    }

    private static Strategy getEntityStrategy(EntityEnum entity) {
        Strategy strategy = switch (entity) {
            case CAR -> new CarStrategy();
            case BOOK -> new BookStrategy();
            case KORNEPLOD -> new KorneplodStrategy();
            case null, default -> null;
        };

        if(strategy == null)
            throw new IllegalArgumentException("Не удалось выбрать стратегию для выбранного типа данных.");

        return strategy;
    }

    private static void collectData(InputTypeEnum inputType, Integer dataAmount, Strategy strategy) throws IOException {
        boolean result = switch (inputType) {
            case MANUAL -> strategy.collectInputData(dataAmount);
            case RANDOM -> strategy.collectRandomData(dataAmount);
            case FILE -> {
                String fileName = ConsoleUtil.getFileName();
                yield strategy.collectDataFromFile(fileName, dataAmount);
                }
            case null, default -> throw new IllegalArgumentException("Не удалось обработать выбранный тип данных. ");
        };

        if(!result)
            throw new IOException("Не удалось получить данные.");

    }

    private static void saveResults(Strategy strategy) {
        try {
            String fileName = ConsoleUtil.getFileName();
            if(strategy.saveResultsToFile(fileName))
                System.out.println("Сохранение успешно завершено!");
            else
                System.out.println("Не удалось сохранить в файл");
        } catch (IOException e) {
            System.out.println("Не удалось прочитать имя файла, запись в файл будет пропущена");
        }
    }

    private static void sort(Strategy strategy) {
        try {
            SortTypeEnum sortType = ConsoleUtil.getSortType();

            if(strategy.sort(sortType)) {
                strategy.showResults();

                boolean shouldSaveResults = ConsoleUtil.shouldSaveToFile();

                if(shouldSaveResults)
                    saveResults(strategy);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void search(Strategy strategy) {
        if(strategy.search()) {
            strategy.showResults();

            boolean shouldSaveResults = ConsoleUtil.shouldSaveToFile();

            if(shouldSaveResults)
                saveResults(strategy);
        }
        else
            System.out.println("Элементов не найдено.");
    }
}
