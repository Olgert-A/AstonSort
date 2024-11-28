package strategy;

public interface Strategy {
    boolean collectInputData(int amount);
    boolean collectRandomData(int amount);
    boolean collectDataFromFile(String name, int amount);

    boolean saveResultsToFile(String name);
    void showCollectedData();
    void showResults();

    boolean sort();
    boolean search();
}
