package strategy;

import data.entities.Book;
import data.search.BinarySearch;
import view.BookFieldEnum;
import view.ViewRepresentationEnum;

import java.util.Comparator;

public class BookStrategy extends AbstractStrategy<Book> implements Strategy {
    private BinarySearch<Book> binarySearch = new BinarySearch<>();

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
        if (!(field instanceof BookFieldEnum)) {
            return;
        }
        BookFieldEnum bookFieldEnum = (BookFieldEnum) field;

        if (bookFieldEnum == BookFieldEnum.PAGES ) {
            if (!(fieldValue instanceof Integer)) {
                return;
            }
            Book searchingBook = new Book.BookBuilder()
                    .setPages((Integer) fieldValue)
                    .build();
            Comparator<Book> comparator = Comparator.comparing(Book::getPages);
            Book result = binarySearch.findByField(rawData, searchingBook, comparator);
            System.out.println(result);
            return;
        }
        if (bookFieldEnum == BookFieldEnum.AUTHOR) {
            if (!(fieldValue instanceof String)) {
                return;
            }
            Book searchingBook = new Book.BookBuilder()
                    .setAuthor((String) fieldValue)
                    .build();
            Comparator<Book> comparator = Comparator.comparing(Book::getAuthor);
            Book result = binarySearch.findByField(rawData, searchingBook, comparator);
            System.out.println(result);
            return;
        }
        if (bookFieldEnum == BookFieldEnum.TITLE) {
            if (!(fieldValue instanceof String)) {
                return;
            }
            Book searvhingBook = new Book.BookBuilder().setTitle((String) fieldValue).build();
            Comparator<Book> comparator = Comparator.comparing(Book::getTitle);
            Book result = binarySearch.findByField(rawData, searvhingBook, comparator);
            System.out.println(result);
            return;
        }

    }

    @Override
    public void showResultsData() {

    }
}
