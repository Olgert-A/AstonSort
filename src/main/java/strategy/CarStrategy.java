package strategy;

import data.entities.Book;
import data.entities.Car;
import data.search.BinarySearch;
import view.CarFieldEnum;
import view.ViewRepresentationEnum;

import java.util.Comparator;

public class CarStrategy extends AbstractStrategy<Car> implements Strategy {
    private BinarySearch<Car> binarySearch = new BinarySearch<>();
    @Override
    public void collectInputData(int amount) {

    }

    @Override
    public void collectRandomData(int amount) {

    }

    @Override
    public void collectDataFromFile(String name, int amount) {

    }

    @Override
    public void saveResultsToFile(String name) {

    }

    @Override
    public void sortBy(ViewRepresentationEnum field, boolean sortOnlyEven) {

    }

    @Override
    public  void sortByAllFields(boolean sortOnlyEven) {

    }

    @Override
    public <T> void searchByField(ViewRepresentationEnum field, T fieldValue) {
        if (!(field instanceof CarFieldEnum)) {
            return;
        }
        CarFieldEnum carFieldEnum = (CarFieldEnum) fieldValue;
        if (carFieldEnum == CarFieldEnum.MODEL) {
            if (!(fieldValue instanceof String)){
                return;
            }
            Car searchingCar = new Car.CarBuilder()
                    .setModel((String) fieldValue)
                    .build();
            Comparator<Car> comparator = Comparator.comparing(Car::getModel);
            Car result = binarySearch.findByField(rawData, searchingCar, comparator);
            System.out.println(result);
            return;
        }
        if (carFieldEnum == CarFieldEnum.POWER) {
            if (!(fieldValue instanceof Integer)){
                return;
            }
            Car searchingCar = new Car.CarBuilder()
                    .setPower(((Integer) fieldValue))
                    .build();
            Comparator<Car> comparator = Comparator.comparing(Car::getPower);
            Car result = binarySearch.findByField(rawData, searchingCar, comparator);
            System.out.println(result);
            return;
        }
        if (carFieldEnum == CarFieldEnum.YEAR) {
            if (!(fieldValue instanceof Integer)){
                return;
            }
            Car searchingCar = new Car.CarBuilder()
                    .setProductionYear((Integer) fieldValue)
                    .build();
            Comparator<Car> comparator = Comparator.comparing(Car::getProductionYear);
            Car result = binarySearch.findByField(rawData, searchingCar, comparator);
            System.out.println(result);
            return;
        }

    }

    @Override
    public void showResultsData() {

    }
}
