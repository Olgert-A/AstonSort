package strategy;

import data.entities.Car;
import view.ViewRepresentationEnum;

public class CarStrategy extends AbstractStrategy<Car> implements Strategy {
    @Override
    public void collectInputData(int amount) {

    }

    @Override
    public void collectRandomData(int amount) {

    }

    @Override
    public boolean collectDataFromFile(String name, int amount) {

        return false;
    }

    @Override
    public void saveResultsToFile(String name) {

    }

    @Override
    public void sortBy(ViewRepresentationEnum field, boolean sortOnlyEven) {

    }

    @Override
    public void sortByAllFields(boolean sortOnlyEven) {

    }

    @Override
    public void searchByField(ViewRepresentationEnum field, Number fieldValue) {

    }

    @Override
    public void showResultsData() {

    }
}
