package strategy;

import data.entities.Book;
import data.util.ParityChecker;
import util.enums.BookFieldEnum;
import util.enums.SortTypeEnum;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static util.ConsoleUtil.getSortType;
import static util.ConsoleUtil.getValue;

public class BookStrategy extends AbstractStrategy<Book> implements Strategy {

    @Override
    public boolean collectInputData(int amount) {
        int booksCount = 0;

        do {
            String author, title;
            int pages;
            String strUserInput;
            Integer intUserInput;

            System.out.println("\nЗаполните книгу №" + booksCount);

            strUserInput = getValue(String.class, BookFieldEnum.AUTHOR.getLocaleName(),
                    Objects::nonNull, "Автор неверный");

            if (strUserInput == null) {
                System.out.println("Не удалось считать автора, ввод объекта будет пропущен");
                continue;
            } else
                author = strUserInput;

            strUserInput = getValue(String.class, BookFieldEnum.TITLE.getLocaleName(),
                    Objects::nonNull, "Название неверное");

            if (strUserInput == null) {
                System.out.println("Не удалось считать название книги, ввод объекта будет пропущен");
                continue;
            } else
                title = strUserInput;

            intUserInput = getValue(Integer.class, BookFieldEnum.PAGES.getLocaleName(),
                    Objects::nonNull, "Количество страниц неверное");

            if (intUserInput == null) {
                System.out.println("Не удалось считать количество страниц, ввод объекта будет пропущен");
                continue;
            } else
                pages = intUserInput;

            Book book = new Book.BookBuilder()
                    .setAuthor(author)
                    .setTitle(title)
                    .setPages(pages)
                    .build();

            this.rawData.add(book);
            booksCount++;
        } while (booksCount < amount);

        return true;
    }

    @Override
    public boolean collectRandomData(int amount) {
        List<String> authorList = List.of("Samuel", "Winston", "Michael", "Oliver", "Andy", "David", "Jason", "Max");
        List<String> titleList = List.of("Some book", "Another book", "Best book", "Selling book", "Misery book");

        Random random = new Random();

        String author;
        String title;
        int pages;

        for (int i = 0; i < amount; i++) {
            author = authorList.get(random.nextInt(authorList.size()));
            title = titleList.get(random.nextInt(titleList.size()));
            pages = random.nextInt(1000);

            Book book = new Book.BookBuilder().setAuthor(author).setTitle(title).setPages(pages).build();
            this.rawData.add(book);
        }

        return true;
    }

    @Override
    public boolean collectDataFromFile(String name, int amount) {
        return false;
    }

    @Override
    public boolean saveResultsToFile(String name) {
        return false;
    }

    @Override
    public void showCollectedData() {
        System.out.println("Исходные данные:");
        for (var book : this.rawData)
            System.out.println(book);
    }

    @Override
    public boolean sort() {
        try {
            BookFieldEnum sortField = ConsoleUtil.getSortField();
            SortTypeEnum sortType = getSortType();
            sortByField(sortType, getFieldComparator(sortField), getFieldParityChecker(sortField));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    private Comparator<Book> getFieldComparator(BookFieldEnum sortField) {
        switch (sortField) {
            case AUTHOR -> {
                return Comparator.comparing(Book::getAuthor);
            }
            case TITLE -> {
                return Comparator.comparing(Book::getTitle);
            }
            case PAGES -> {
                return Comparator.comparing(Book::getPages);

            }
            case ALL -> {
                return Comparator.comparing(Book::getAuthor)
                        .thenComparing(Book::getTitle).thenComparing(Book::getPages);
            }
        }
        return null;
    }

    private ParityChecker<Book> getFieldParityChecker(BookFieldEnum sortField) {
        if (sortField.equals(BookFieldEnum.PAGES)) return obj -> obj.getPages() % 2 == 0;
        return null;
    }

    @Override
    public boolean search() {
        String strUserInput;
        Integer intUserInput;
        BookFieldEnum searchField;
        Comparator<Book> comparator = null;
        Book.BookBuilder searchDummy = new Book.BookBuilder();

        StringBuilder requestTextBuilder = new StringBuilder("\nВыберите поля поиска:");
        int fieldAmount = BookFieldEnum.values().length;
        for (var field : BookFieldEnum.values())
            if (field != BookFieldEnum.ALL)
                requestTextBuilder.append("\n").append(field.getOrdinalLocaleName());

        intUserInput = getValue(Integer.class, requestTextBuilder.toString(),
                v -> v >= 0 && v < fieldAmount, "Значение должно быть от 0 до 2");

        if (intUserInput == null) {
            System.out.println("Не удалось выбрать поля сортировки, операция будет прервана!");
            return false;
        } else
            searchField = BookFieldEnum.values()[intUserInput];

        switch (searchField) {
            case AUTHOR -> {
                comparator = Comparator.comparing(Book::getAuthor);

                strUserInput = getValue(String.class, searchField.getLocaleName(), Objects::nonNull, "");
                if (strUserInput == null) {
                    System.out.println("Не удалось ввести поле, операция будет прервана!");
                    return false;
                }
                searchDummy.setAuthor(strUserInput);
            }
            case TITLE -> {
                comparator = Comparator.comparing(Book::getTitle);

                strUserInput = getValue(String.class, searchField.getLocaleName(), Objects::nonNull, "");
                if (strUserInput == null) {
                    System.out.println("Не удалось ввести поле, операция будет прервана!");
                    return false;
                } else
                    searchDummy.setTitle(strUserInput);
            }
            case PAGES -> {
                comparator = Comparator.comparing(Book::getPages);

                intUserInput = getValue(Integer.class, searchField.getLocaleName(), Objects::nonNull, "");
                if (intUserInput == null) {
                    System.out.println("Не удалось ввести поле, операция будет прервана!");
                    return false;
                } else
                    searchDummy.setPages(intUserInput);
            }
        }

        List<Book> sorted = sortAlgorithm.sort(this.rawData, comparator);
        Book found = this.searchAlgorithm.findByField(sorted, searchDummy.build(), comparator);
        if (found == null) {
            System.out.println("Не найдено");
            return false;
        }

        this.processedData.clear();
        this.processedData.add(found);
        return true;
    }

    @Override
    public void showResults() {
        System.out.println("Результат:");
        for (var book : this.processedData)
            System.out.println(book);
    }

    private static class ConsoleUtil {

        public static BookFieldEnum getSortField() throws Exception {
            BookFieldEnum sortField;
            StringBuilder requestTextBuilder = new StringBuilder("\nВыберите поля сортировки:");
            int fieldAmount = BookFieldEnum.values().length;
            for (var field : BookFieldEnum.values())
                requestTextBuilder.append("\n").append(field.getOrdinalLocaleName());

            Integer intUserInput = getValue(Integer.class, requestTextBuilder.toString(),
                    v -> v >= 0 && v < fieldAmount, "Значение должно быть от 0 до " + (fieldAmount - 1));

            if (intUserInput == null) {
                System.out.println("Не удалось выбрать поля сортировки, операция будет прервана!");
                throw new Exception("return false");
            } else
                sortField = BookFieldEnum.values()[intUserInput];
            return sortField;
        }
    }
}
