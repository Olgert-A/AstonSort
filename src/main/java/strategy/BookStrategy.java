package strategy;

import data.entities.Book;
import data.validate.BookValidator;
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
            if (!(line.equals("Books"))) {
                System.out.println("Invalid file");
                return false;
            }

            List<String> parsedName = new ArrayList<>();
            List<String> parsedTitle = new ArrayList<>();
            List <Integer> parsedPages = new ArrayList<>();
            int counter = 0;
            BookValidator bookValidator = new BookValidator() {
                @Override
                public boolean isAuthorValid(String author) {
                    return author.length() > 5;
                }

                @Override
                public boolean isTitleValid(String title) {
                    return title.length() > 3;
                }

                @Override
                public boolean isPagesValid(int pages) {
                    return pages > 1;
                }
            };
            while((line = bufferedReader.readLine()) != null && counter < amount) {
                String[] splitLine = line.split(":");
                switch (splitLine[0]) {
                    case "[Author" -> {
                        if (bookValidator.isAuthorValid(splitLine[1].trim()))
                        parsedName.add(splitLine[1].trim());
                        break;
                    }
                    case "Title" -> {
                        if (bookValidator.isTitleValid(splitLine[1].trim()))
                            parsedTitle.add(splitLine[1].trim());
                        break;                        }
                    case "Pages" -> {
                        String intLine = splitLine[1].substring(0, splitLine[1].length() - 1).trim();
                        if (bookValidator.isPagesValid(Integer.valueOf(intLine, 10))) {
                            parsedPages.add(Integer.valueOf(intLine, 10));
                            counter++;}
                        break;
                    }
                }
            }

            if (counter < amount) {
                System.out.println("Файл закончился раньше, чем массив заполнился");
                amount = counter;
            }
            bufferedReader.close();
            for(int i = 0; i < amount; i++) {
                Book.BookBuilder bookBuilder = new Book.BookBuilder();
                rawData.add(bookBuilder.setAuthor(parsedName.get(i))
                        .setTitle(parsedTitle.get(i))
                        .setPages(parsedPages.get(i))
                        .build());
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
                bufferedWriter.write("[Author: ");
                bufferedWriter.write(book.getAuthor());
                bufferedWriter.newLine();
                bufferedWriter.write("Title: ");
                bufferedWriter.write(book.getTitle());
                bufferedWriter.newLine();
                bufferedWriter.write("Pages: ");
                bufferedWriter.write(String.valueOf(book.getPages()));
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
/*    public static void main(String[] args) {
        String fileName = "C:\\Users\\reeze\\IdeaProjects\\newAstonProject\\input.txt";
        BookStrategy bookStrategy = new BookStrategy();
        bookStrategy.collectDataFromFile(fileName, 3);
        System.out.println(bookStrategy.rawData);
        bookStrategy.saveResultsToFile("example.txt");

    }*/
}
