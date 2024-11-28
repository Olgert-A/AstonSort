package strategy;

import data.entities.Book;
import data.util.BookUtil;
import data.util.ParityChecker;
import data.util.Validate;
import util.enums.BookFieldEnum;
import util.enums.SortTypeEnum;

import java.util.Comparator;
import java.util.Objects;

import static util.ConsoleUtil.getValue;

import java.io.*;
import java.util.List;

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
            Validate <Integer> pagesValidator = v -> v > 1;
            String author = null, title = null;
            int pages = 0;
            boolean startFlag = false;

            while((line = bufferedReader.readLine()) != null && counter < amount) {
                if (line.trim().startsWith("[")){
                    startFlag = true;
                    author = null;
                    title = null;
                    pages = 0;
                }

                if (line.trim().endsWith("]")) {
                    startFlag = false;
                    if (author != null && title != null & pages != 0) {
                        if (authorValidator.isValid(author) && titleValidator.isValid(title)
                            && pagesValidator.isValid(pages)){
                            Book book = new Book.BookBuilder()
                                    .setAuthor(author)
                                    .setTitle(title)
                                    .setPages(pages)
                                    .build();
                            rawData.add(book);
                            counter++;
                        }
                        else {
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
        }
        catch (IOException ex) {
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
            for (Book book:booksList) {
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
    public void showCollectedData() {
        System.out.println("Исходные данные:");
        for (var book : this.rawData)
            System.out.println(book);
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
