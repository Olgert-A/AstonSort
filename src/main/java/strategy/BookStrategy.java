package strategy;

import data.entities.Book;
import view.ViewRepresentationEnum;

public class BookStrategy extends AbstractStrategy<Book> implements Strategy {
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
    public void searchByField(ViewRepresentationEnum field, Number fieldValue) {

    }

    @Override
    public void showResultsData() {

    }
}
