package strategy;

import data.entities.Car;
import data.entities.Korneplod;
import data.search.BinarySearch;
import view.CarFieldEnum;
import view.KorneplodFieldEnum;
import view.ViewRepresentationEnum;

import java.time.Year;
import java.util.Comparator;
import java.util.List;

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
    public boolean search (){
        CarFieldEnum searchField = CarStrategy.CarConsoleUtil.getSearchField();

        if (searchField == null){
            System.out.println("No search field found");
            return false;
        }
        Car searchValue = getSearchValue(searchField);
        if (searchValue != null){
            System.out.println( "что-то пошло не так");
            return false;
        }
        Comparator <Car> comparator = getFieldComparator(searchField);
        if (comparator == null){

            System.out.println("Компаратор не найден!");
            return false;
        }
        List<Car> sortedData = this.sortAlgorithm(this.rawData, comparator);
        Car result = this.searchAlgorithm.findByField(sortedData, searchValue, comparator);

        if (result == null){
            return false;
        }
        this.processedData.clear();
        this.processedData.add(result);
        return true;

    }

    private List<Korneplod> sortAlgorithm(List<Korneplod> rawData, Comparator<Korneplod> comparator) {
        return null;
    }

    private Comparator<Car> getFieldComparator(CarFieldEnum field) {

        switch (field) {
            case MODEL ->{
                return Comparator.comparing(Car::getModel);
            }
            case POWER ->{
                return Comparator.comparing(Car::getPower);
            }
            case YEAR ->{
                return Comparator.comparing(Car::getProductionYear);
            }
            default ->
            {
                return null;
            }
        }

    }
    private Car getSearchValue (CarFieldEnum searchField) {
       Car.CarBuilder builder = new Car.CarBuilder();
        switch (searchField) {
            case MODEL->{
                String model = CarConsoleUtil.getModel();
                if (model == null) {
                    return null;
                }
                return builder.setModel(model).build();
            }
            case POWER ->{
                Integer power = CarConsoleUtil.getPower();
                if (power == null) {
                    return null;
                }
                return builder.setPower(power).build();
            }
            case YEAR ->{

                Integer year  = CarStrategy.CarConsoleUtil.getProductionYear();
                if (year == null) {
                    return null;
                }
                return builder.setProductionYear(year).build();
            }
            case ALL -> {
                System.out.println("нельзя искать всё!");
                return null;
            }
            default -> {
                return null;
            }
        }

    }
    @Override
    public void showResultsData() {

    }
    public static class CarConsoleUtil {
        public static CarFieldEnum getSearchField() {
            return null;
        }
        public static String getModel() {
            return null;
        }
        public static Integer getProductionYear() {
            return null;
        }
        public static Integer getPower() {
            return null;
        }
    }
}
