package strategy;

import view.ViewRepresentationEnum;

public interface Strategy {
    void collectInputData(int amount);
    void collectRandomData(int amount);
    void collectDataFromFile(String name, int amount);

    void saveResultsToFile(String name);
    void showResultsData();

    void sortBy(ViewRepresentationEnum field);
    void sortByAllFields();
    void searchByField(ViewRepresentationEnum field, Number fieldValue);
}
