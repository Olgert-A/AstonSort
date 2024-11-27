package strategy;

import view.ViewRepresentationEnum;

public interface Strategy {
    void collectInputData(int amount);
    void collectRandomData(int amount);
    void collectDataFromFile(String name, int amount);

    void saveResultsToFile(String name);
    void showResultsData();

    void sortBy(ViewRepresentationEnum field, boolean sortOnlyEven);
    void sortByAllFields(boolean sortOnlyEven);
     <T> void searchByField(ViewRepresentationEnum field, T fieldValue);
}
