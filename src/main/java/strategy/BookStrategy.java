package strategy;

import data.entities.Book;
import data.util.BookUtil;
import data.util.ParityChecker;
import data.util.Validate;
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
            Validate<String> authorValidator = v -> v.length() > 5;
            Validate<String> titleValidator = v -> v.length() > 3;
            Validate<Integer> pagesValidator = v -> v > 1;
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
                        if (authorValidator.isValid(author) && titleValidator.isValid(title)
                                && pagesValidator.isValid(pages)) {
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
                bufferedWriter.write(book.getPages());
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
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    private Comparator<Book> getFieldComparator(BookFieldEnum sortField) {
        switch (sortField) {
            case AUTHOR -> {
                return new BookUtil.BookAuthorComparator();
            }
            case TITLE -> {
                return new BookUtil.BookTitleComparator();
            }
            case PAGES -> {
                return new BookUtil.BookPagesComparator();
            }
            case ALL -> {
                return new BookUtil.BookGeneralComparator();
            }
        }
        return null;
    }

    private ParityChecker<Book> getFieldParityChecker(BookFieldEnum sortField) {
        if (sortField.equals(BookFieldEnum.PAGES)) return new BookUtil.BookPagesParityCheker();
        return null;
    }


    @Override
    public boolean search() {
        BookFieldEnum searchField = ConsoleUtil.getSearchField();

        if (searchField == null) {
            System.out.println("No search field found");
            return false;
        }
        Book searchValue = getSearchValue(searchField);
        if (searchValue != null) {
            System.out.println("что-то пошло не так");
            return false;
        }
        Comparator<Book> comparator = getFieldComparator(searchField);
        if (comparator == null) {

            System.out.println("Компаратор не найден!");
            return false;
        }
        List<Book> sortedData = this.sortAlgorithm.sort(this.rawData, comparator);
        Book result = this.searchAlgorithm.findByField(sortedData, searchValue, comparator);

        if (result == null) {
            return false;
        }
        this.processedData.clear();
        this.processedData.add(result);
        return true;

    }


    private Book getSearchValue(BookFieldEnum searchField) {
        Book.BookBuilder builder = new Book.BookBuilder();
        switch (searchField) {
            case AUTHOR -> {
                String author = ConsoleUtil.getAuthorField();
                if (author == null) {
                    return null;
                }
                return builder.setAuthor(author).build();
            }
            case TITLE -> {
                String title = ConsoleUtil.getTitleField();
                if (title == null) {
                    return null;
                }
                return builder.setTitle(title).build();
            }
            case PAGES -> {
                Integer pages = ConsoleUtil.getPagesField();
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

        public static BookFieldEnum getSearchField() {
            return null;
        }

        public static String getAuthorField() {
            return null;
        }

        public static String getTitleField() {
            return null;
        }

        public static Integer getPagesField() {
            return null;
        }
    }
}