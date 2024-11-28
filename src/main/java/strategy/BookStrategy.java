package strategy;

import data.entities.Book;
import data.search.BinarySearch;
import view.BookFieldEnum;
import view.ViewRepresentationEnum;

import java.util.Comparator;
import java.util.List;


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
    public boolean search (){
        BookFieldEnum searchField = BookConsoleUtil.getSearchField();

        if (searchField == null){
            System.out.println("No search field found");
            return false;
        }
        Book searchValue =  getSearchValue(searchField);
        if (searchValue != null){
            System.out.println( "что-то пошло не так");
            return false;
        }
        Comparator <Book> comparator = getFieldComparator(searchField);
        if (comparator == null){

            System.out.println("Компаратор не найден!");
            return false;
        }
        List <Book>  sortedData = this.sortAlgorithm(this.rawData, comparator);
        Book result = this.searchAlgorithm.findByField(sortedData, searchValue, comparator);

        if (result == null){
            return false;
        }
        this.processedData.clear();
        this.processedData.add(result);
        return true;

    }

    private Comparator <Book> getFieldComparator(BookFieldEnum field) {

        switch (field) {
            case AUTHOR ->{
                return Comparator.comparing(Book::getAuthor);
            }
            case TITLE ->{
                return Comparator.comparing(Book::getTitle);
            }
            case PAGES ->{
                return Comparator.comparing(Book::getPages);
            }
             default ->
            {
                return null;
            }
        }

    }
    private Book getSearchValue (BookFieldEnum searchField) {
        Book.BookBuilder builder = new Book.BookBuilder();
        switch (searchField) {
            case AUTHOR ->{
                String author = BookConsoleUtil.getAuthorField();
                if (author == null) {
                    return null;
                }
                return builder.setAuthor(author).build();
            }
            case TITLE ->{
                String title = BookConsoleUtil.getTitleField();
                if (title == null) {
                    return null;
                }
                return builder.setTitle(title).build();
            }
            case PAGES ->{
                Integer pages = BookConsoleUtil.getPagesField();
                if (pages == null) {
                    return null;
                }
                return builder.setPages(pages).build();
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


    private List<Book> sortAlgorithm(List<Book> rawData, Comparator<Book> comparator) {
    }


    @Override
    public void showResultsData() {

    }
    public static class BookConsoleUtil {
        public static BookFieldEnum getSearchField(){
            return null;
        }
        public static  String getAuthorField(){
            return null;
        }
       public static  String getTitleField(){
            return null;
       }
       public static  Integer getPagesField(){
            return null;
       }
    }
}
