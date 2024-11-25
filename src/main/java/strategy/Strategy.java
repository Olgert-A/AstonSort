package strategy;

import view.ViewRepresentationEnum;

public interface Strategy {
    void collectInputData(int amount);
    void collectRandomData(int amount);
    boolean collectDataFromFile(String name, int amount);

    void saveResultsToFile(String name);
    void showResultsData();

    void sortBy(ViewRepresentationEnum field, boolean sortOnlyEven);
    void sortByAllFields(boolean sortOnlyEven);
    void searchByField(ViewRepresentationEnum field, Number fieldValue);
}
