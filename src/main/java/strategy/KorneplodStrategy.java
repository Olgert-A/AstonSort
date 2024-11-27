package strategy;

import data.entities.Korneplod;
import data.search.BinarySearch;
import view.KorneplodFieldEnum;
import view.ViewRepresentationEnum;

import java.util.Comparator;

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
        if (korneplodFieldEnum == KorneplodFieldEnum.TYPE ){
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
        if (korneplodFieldEnum == KorneplodFieldEnum.COLOR){
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
        if (korneplodFieldEnum == KorneplodFieldEnum.WEIGHT){
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
    public void showResultsData() {

    }
}
