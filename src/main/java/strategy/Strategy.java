package strategy;

public interface Strategy {
    boolean collectInputData(int amount);
    boolean collectRandomData(int amount);
    boolean collectDataFromFile(String name, int amount);

    void saveResultsToFile(String name);
    void showResults();

    boolean sort();
    boolean search();
}
