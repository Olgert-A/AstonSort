package data.util;

import data.entities.Book;
import data.validate.Validate;

import java.util.Comparator;

public class BookUtil {

    public static class BookAuthorComparator implements Comparator<Book> {
        @Override
        public int compare(Book o1, Book o2) {
            return o1.getAuthor().compareTo(o2.getAuthor());
        }
    }

    public static class BookPagesComparator implements Comparator<Book> {
        @Override
        public int compare(Book o1, Book o2) {
            if (o1.getPages()< o2.getPages()){
                return -1;
            }

            if (o1.getPages() == o2.getPages()){
                return 0;
            }
            return 1;
        }
    }

    public static class BookTitleComparator implements Comparator<Book> {
        @Override
        public int compare(Book o1, Book o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    }

    public static class BookGeneralComparator implements Comparator<Book> {
        @Override
        public int compare(Book o1, Book o2) {
            return Comparator.comparing(Book::getAuthor)
                    .thenComparing(Book::getTitle)
                    .thenComparingInt(Book::getPages)
                    .compare(o1, o2);
        }
    }

    public static class BookPagesParityCheker implements ParityChecker<Book> {
        @Override
        public boolean isEven(Book obj) {
            return obj != null && obj.getPages() % 2 == 0;
        }
    }

    public static class BookAuthorValidator implements Validate<Book>{
        @Override
        public boolean isValid(Book obj) {
            return obj.getAuthor() != null && !obj.getAuthor().trim().isEmpty() && obj.getAuthor().length() >= 3;
        }
    }

    public static class BookTitleValidator implements Validate<Book>{
        @Override
        public boolean isValid(Book obj) {
            return obj.getTitle() != null && !obj.getTitle().trim().isEmpty();
        }
    }

    public static class BookPagesValidator implements Validate<Book>{
        @Override
        public boolean isValid(Book obj) {
            return obj.getPages() > 0 && obj.getPages() <=3000;
        }
    }

    public static final String AUTHOR_INVALID_TEXT = "Автор не нулевое значение и не пустая строка, символы должны быть больше или равно 3";
    public static final String TITLE_INVALID_TEXT = "Название не нулевое значение и не пустая строка";
    public static final String PAGES_INVALID_TEXT = "Страницы должны быть больше 0 и меньше или равно 3000";
}
