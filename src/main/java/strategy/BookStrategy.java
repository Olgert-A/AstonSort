package strategy;

import data.entities.Book;
import data.validate.BookValidator;
import data.validate.Validator;
import view.ViewRepresentationEnum;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookStrategy extends AbstractStrategy<Book> implements Strategy {
    @Override
    public void collectInputData(int amount) {

    }

    @Override
    public void collectRandomData(int amount) {

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
            Validator<String> authorValidator = v -> v.length() > 5;
            Validator<String> titleValidator = v -> v.length() > 3;
            Validator <Integer> pagesValidator = v -> v > 1;
            String author = null, title = null;
            int pages = 0;
            boolean startFlag = false, endFlag = false;

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
                            String intLine = splitLine[1].substring(0, splitLine[1].length() - 1).trim();
                            pages = Integer.valueOf(intLine, 10);
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
    public static void main(String[] args) {
        String fileName = "C:\\Users\\reeze\\IdeaProjects\\newAstonProject\\input.txt";
        BookStrategy bookStrategy = new BookStrategy();
        bookStrategy.collectDataFromFile(fileName, 4);
        System.out.println(bookStrategy.rawData);
        bookStrategy.saveResultsToFile("example.txt");

    }
}
