package strategy;

import util.enums.SortTypeEnum;

public interface Strategy {
    boolean collectInputData(int amount);
    boolean collectRandomData(int amount);
    boolean collectDataFromFile(String name, int amount);

    boolean saveResultsToFile(String name);
    void showCollectedData();
    void showResults();

    boolean sort(SortTypeEnum sortType);
    boolean search();
}
