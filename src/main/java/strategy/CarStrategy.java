package strategy;

import data.entities.Car;

public class CarStrategy extends AbstractStrategy<Car> implements Strategy {
    @Override
    public boolean collectInputData(int amount) {
        return false;
    }

    @Override
    public boolean collectRandomData(int amount) {
        return false;
    }

    @Override
    public boolean collectDataFromFile(String name, int amount) {
        return false;
    }

    @Override
    public void saveResultsToFile(String name) {

    }

    @Override
    public boolean sort() {
        return false;
    }

    @Override
    public boolean search() {
        return false;
    }

    @Override
    public void showResults() {

    }
}
