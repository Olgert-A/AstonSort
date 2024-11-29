package strategy;

import data.entities.Book;
import data.util.BookUtil;
import data.util.ParityChecker;
import util.enums.BookFieldEnum;
import util.enums.SortTypeEnum;

import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static util.ConsoleUtil.getValue;


public class BookStrategy extends AbstractStrategy<Book> implements Strategy {

    @Override
    public boolean collectInputData(int amount) {
        int booksCount = 0;

        do {
            String author, title;
            int pages;

            System.out.println("\nЗаполните книгу №" + booksCount);
            try {
                author = ConsoleUtil.getAuthorField();
                title = ConsoleUtil.getTitleField();
                pages = ConsoleUtil.getPagesField();
            } catch (IOException e) {
                System.out.println(e.getMessage() + " Заполнение книги начнётся с начала");
                continue;
            }

            Book book = new Book.BookBuilder()
                    .setAuthor(author)
                    .setTitle(title)
                    .setPages(pages)
                    .build();

            this.rawData.add(book);
        } while (++booksCount < amount);

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

        while (rawData.size() < amount) {
            author = authorList.get(random.nextInt(authorList.size()));
            title = titleList.get(random.nextInt(titleList.size()));
            pages = random.nextInt(1000);

            if (new BookUtil.BookAuthorValidator().isValid(author) &&
                    new BookUtil.BookTitleValidator().isValid(title) &&
                    new BookUtil.BookPagesValidator().isValid(pages)) {
                Book book = new Book.BookBuilder().setAuthor(author).setTitle(title).setPages(pages).build();
                this.rawData.add(book);
            }
        }
        return true;
    }

    @Override
    public boolean collectDataFromFile(String name, int amount) {
        try (FileReader fileReader = new FileReader(name);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            line = bufferedReader.readLine();
            if (line.isEmpty()) {
                while (line.isEmpty()) {
                    line = bufferedReader.readLine();
                }
            }
            if (!(line.equals("Books")) || line == null) {
                System.out.println("Invalid file");
                return false;
            }

            int counter = 0;
            String author = null, title = null;
            int pages = 0;
            boolean startFlag = false;

            while ((line = bufferedReader.readLine()) != null && counter < amount) {
                if (line.trim().startsWith("[")) {
                    startFlag = true;
                    author = null;
                    title = null;
                    pages = 0;
                }

                if (line.trim().endsWith("]")) {
                    startFlag = false;
                    if (author != null && title != null & pages != 0) {
                        if (new BookUtil.BookAuthorValidator().isValid(author) &&
                                new BookUtil.BookTitleValidator().isValid(title) &&
                                new BookUtil.BookPagesValidator().isValid(pages)) {
                            Book book = new Book.BookBuilder()
                                    .setAuthor(author)
                                    .setTitle(title)
                                    .setPages(pages)
                                    .build();
                            rawData.add(book);
                            counter++;
                        } else {
                            System.out.println("Были прочитаны невалидные данные. Сущность не будет записана в файл.");
                        }
                    }
                }

                String[] splitLine = line.split(":");

                if (startFlag && splitLine.length > 1) {
                    switch (splitLine[0].trim()) {
                        case "Author" -> {
                            author = splitLine[1].trim();
                        }
                        case "Title" -> {
                            title = splitLine[1].trim();
                        }
                        case "Pages" -> {
                            pages = Integer.valueOf(splitLine[1].trim(), 10);
                        }
                    }
                }
            }
            bufferedReader.close();
            if (counter < amount) {
                System.out.println("Файл закончился раньше, чем массив заполнился");
            }
            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }


    @Override
    public boolean saveResultsToFile(String name) {
        try (FileWriter fileWriter = new FileWriter(name);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            FileReader fileReader = new FileReader(name);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            if (processedData.isEmpty()) {
                System.out.println("Нечего записывать в файл.");
                return false;
            }
            String line = bufferedReader.readLine();
            if (line == null) {
                bufferedReader.close();
                bufferedWriter.write("Books");
                bufferedWriter.newLine();
            }
            bufferedReader.close();
            List<Book> booksList = processedData;
            for (Book book : booksList) {
                bufferedWriter.write("[");
                bufferedWriter.newLine();
                bufferedWriter.write("Author: ");
                bufferedWriter.write(book.getAuthor());
                bufferedWriter.newLine();
                bufferedWriter.write("Title: ");
                bufferedWriter.write(book.getTitle());
                bufferedWriter.newLine();
                bufferedWriter.write("Pages: ");
                bufferedWriter.write(String.valueOf(book.getPages()));
                bufferedWriter.newLine();
                bufferedWriter.write("]");
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean sort(SortTypeEnum sortType) {
        try {
            BookFieldEnum sortField = ConsoleUtil.getSortField();
            sortByField(sortType, getFieldComparator(sortField), getFieldParityChecker(sortField));
        } catch (IOException e) {
            System.out.println(e.getMessage() + " Сортировка будет прекращена.");
            return false;
        }

        return true;
    }

    private Comparator<Book> getFieldComparator(BookFieldEnum sortField) {
        return switch (sortField) {
            case AUTHOR -> new BookUtil.BookAuthorComparator();
            case TITLE -> new BookUtil.BookTitleComparator();
            case PAGES -> new BookUtil.BookPagesComparator();
            case ALL -> new BookUtil.BookGeneralComparator();
            case null, default -> throw new IllegalArgumentException("Невозможно выбрать компаратор для данного поля.");
        };
    }

    private ParityChecker<Book> getFieldParityChecker(BookFieldEnum sortField) {
        if (sortField.equals(BookFieldEnum.PAGES)) return new BookUtil.BookPagesParityCheker();
        return null;
    }


    @Override
    public boolean search() {
        BookFieldEnum searchField;
        Book searchValue;
        Comparator<Book> comparator;

        try {
            searchField = ConsoleUtil.getSearchField();
            searchValue = getSearchValue(searchField);
            comparator = getFieldComparator(searchField);
        } catch (IOException e) {
            System.out.println(e.getMessage() + " Поиск будет прекращён.");
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }

        List<Book> sortedData = this.sortAlgorithm.sort(this.rawData, comparator);
        Book result = this.searchAlgorithm.findByField(sortedData, searchValue, comparator);

        if (result == null)
            return false;

        this.processedData.clear();
        this.processedData.add(result);
        return true;

    }


    private Book getSearchValue(BookFieldEnum searchField) throws IOException {
        Book.BookBuilder builder;

        builder = switch (searchField) {
            case AUTHOR -> {
                String author = ConsoleUtil.getAuthorField();
                yield new Book.BookBuilder().setAuthor(author);
            }
            case TITLE -> {
                String title = ConsoleUtil.getTitleField();
                yield new Book.BookBuilder().setTitle(title);
            }
            case PAGES -> {
                Integer pages = ConsoleUtil.getPagesField();
                yield new Book.BookBuilder().setPages(pages);
            }
            case ALL -> throw new IOException("Поиск возможен только по одиночному полю.");
            case null, default -> null;
        };

        if (builder == null)
            throw new IOException("Корректное поле для поиска не было выбрано.");

        return builder.build();
    }


    private static class ConsoleUtil {
        public static BookFieldEnum getSortField() throws IOException {
            return getBookField("сортировки");
        }

        public static BookFieldEnum getSearchField() throws IOException {
            return getBookField("поиска");
        }

        public static String getAuthorField() throws IOException {
            String author = getValue(String.class, BookFieldEnum.AUTHOR.getLocaleName(),
                    new BookUtil.BookAuthorValidator(), BookUtil.AUTHOR_INVALID_TEXT);

            if (author == null)
                throw new IOException("Не удалось считать автора.");

            return author;
        }

        public static String getTitleField() throws IOException {
            String title = getValue(String.class, BookFieldEnum.TITLE.getLocaleName(),
                    new BookUtil.BookTitleValidator(), BookUtil.TITLE_INVALID_TEXT);

            if (title == null)
                throw new IOException("Не удалось считать название книги.");

            return title;
        }

        public static Integer getPagesField() throws IOException {
            Integer pages = getValue(Integer.class, BookFieldEnum.PAGES.getLocaleName(),
                    Objects::nonNull, "Количество страниц неверное");

            if (pages == null)
                throw new IOException("Не удалось считать количество страниц.");

            return pages;
        }

        private static BookFieldEnum getBookField(String fieldUsage) throws IOException {
            BookFieldEnum[] values = BookFieldEnum.values();
            final int minOrdinal = values[0].ordinal();
            final int maxOrdinal = values[values.length - 1].ordinal();

            StringBuilder requestTextBuilder = new StringBuilder("Выберите поля " + fieldUsage + ":");
            for (var field : BookFieldEnum.values())
                requestTextBuilder.append("\n").append(field.getOrdinalLocaleName());

            Integer userInput = getValue(Integer.class, requestTextBuilder.toString(),
                    v -> v >= minOrdinal && v <= maxOrdinal, util.ConsoleUtil.CHOICE_INVALID_TEXT);

            if (userInput == null)
                throw new IOException("Не удалось выбрать поля " + fieldUsage + ".");

            return BookFieldEnum.values()[userInput];
        }
    }
}