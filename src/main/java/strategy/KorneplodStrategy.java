package strategy;


import data.entities.Korneplod;
import data.search.BinarySearch;
import view.KorneplodFieldEnum;
import view.ViewRepresentationEnum;
import java.util.Comparator;
import java.util.List;

import static view.KorneplodFieldEnum.*;


public class KorneplodStrategy extends AbstractStrategy<Korneplod> implements Strategy {
    private BinarySearch<Korneplod> binarySearch = new BinarySearch<>();
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
    public void sortByAllFields(boolean sortOnlyEven) {

    }

    @Override
    public <T> void searchByField(ViewRepresentationEnum field, T fieldValue) {
      if (!(field instanceof Korneplod)){
          return;
      }
        KorneplodFieldEnum korneplodFieldEnum = (KorneplodFieldEnum) field;
        if (korneplodFieldEnum == TYPE ){
            if (!(fieldValue instanceof String)){
                return;
            }
            Korneplod searchingKorneplod = new Korneplod.KorneplodBuilder()
                    .setType((String)fieldValue)
                    .build();
            Comparator <Korneplod> comparator = Comparator.comparing(Korneplod::getType);
            Korneplod result = binarySearch.findByField(rawData, searchingKorneplod, comparator);
            System.out.println(result);
            return;
        }
        if (korneplodFieldEnum == COLOR){
            if (!(fieldValue instanceof String)){
                return;
            }
            Korneplod searchingKorneplod = new Korneplod.KorneplodBuilder()
                    .setColor((String)fieldValue)
                    .build();
            Comparator <Korneplod> comparator = Comparator.comparing(Korneplod::getColor);
            Korneplod result = binarySearch.findByField(rawData, searchingKorneplod, comparator);
            System.out.println(result);
            return;
        }
        if (korneplodFieldEnum == WEIGHT){
            if (!(fieldValue instanceof Integer)){
                return;
            }
            Korneplod searchingKorneplod = new Korneplod.KorneplodBuilder()
                    .setWeight((Integer)fieldValue)
                    .build();
            Comparator <Korneplod> comparator = Comparator.comparing(Korneplod::getWeight);
            Korneplod result = binarySearch.findByField(rawData, searchingKorneplod, comparator);
            System.out.println(result);
            return;

        }
    }




      @Override
    public boolean search (){
        KorneplodFieldEnum searchField = KorneplodStrategy.KorneplodConsoleUtil.getSearchField();

        if (searchField == null){
            System.out.println("No search field found");
            return false;
        }
        Korneplod searchValue =  getSearchValue(searchField);
        if (searchValue != null){
            System.out.println( "что-то пошло не так");
            return false;
        }
        Comparator <Korneplod> comparator = getFieldComparator(searchField);
        if (comparator == null){

            System.out.println("Компаратор не найден!");
            return false;
        }
        List<Korneplod> sortedData = this.sortAlgorithm(this.rawData, comparator);
        Korneplod result = this.searchAlgorithm.findByField(sortedData, searchValue, comparator);

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

    private Comparator <Korneplod> getFieldComparator(KorneplodFieldEnum field) {

        switch (field) {
            case TYPE ->{
                return Comparator.comparing(Korneplod::getType);
            }
            case COLOR ->{
                return Comparator.comparing(Korneplod::getColor);
            }
            case WEIGHT ->{
                return Comparator.comparing(Korneplod::getWeight);
            }
            default ->
            {
                return null;
            }
        }

    }
    private Korneplod getSearchValue (KorneplodFieldEnum searchField) {
        Korneplod.KorneplodBuilder builder = new Korneplod.KorneplodBuilder();
        switch (searchField) {
            case TYPE ->{
                String type = KorneplodStrategy.KorneplodConsoleUtil.getType();
                if (type == null) {
                    return null;
                }
                return builder.setType(type).build();
            }
            case COLOR ->{
                String color = KorneplodStrategy.KorneplodConsoleUtil.getColor();
                if (color == null) {
                    return null;
                }
                return builder.setColor(color).build();
            }
            case WEIGHT ->{
                Double weight = KorneplodStrategy.KorneplodConsoleUtil.getWeight();
                if (weight == null) {
                    return null;
                }
                return builder.setWeight(weight).build();
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
    public static class KorneplodConsoleUtil {
        public static KorneplodFieldEnum getSearchField() {
            return null;
        }
        public static String getType(){
            return null;
        }
        public static Double getWeight(){
            return null;
        }
        public static String getColor(){
            return null;
        }

    }
}
