package strategy;

import data.entities.Book;
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
        try (FileReader fileReader = new FileReader(name)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
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
            while((line = bufferedReader.readLine()) != null && counter < amount) {
                String[] splitLine = line.split(":");
                if (line.startsWith("[Author")) {
                    parsedName.add(splitLine[1].trim());
                } else if (line.startsWith("Title")) {
                    parsedTitle.add(splitLine[1].trim());
                } else if (line.startsWith("Pages")) {
                    if (splitLine[1].endsWith("]")) {
                        String intLine = splitLine[1].substring(0, splitLine[1].length()-1).trim();
                        parsedPages.add(Integer.valueOf(intLine,10));
                        counter++;
                    }
                } else {
                    continue;}
            }

            if (counter < amount) {
                System.out.println("Файл закончился раньше, чем массив заполнился");
                amount = counter;
            }
            bufferedReader.close();
            for(int i = 0; i < amount; i++) {
                //BookValidatorImpl bvi = new BookValidatorImpl();
                // if (!(bvi.isAuthorValid(parsedName.get(i)){
                    //System.out.println("Invalid author name");
                    //continue;}
                    //else if (!(... и так далее

                Book.BookBuilder bookBuilder = new Book.BookBuilder();
                rawData.add(bookBuilder.setAuthor(parsedName.get(i))
                        .setTitle(parsedTitle.get(i))
                        .setPages(parsedPages.get(i))
                        .build());
            }
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }


    @Override
    public boolean saveResultsToFile(String name) {
        try (FileWriter fileWriter = new FileWriter(name)) {
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            if (processedData.isEmpty()) {
                System.out.println("Нечего записывать в файл.");
                return false;
            }

           // if (fileWriter.)
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
        bookStrategy.collectDataFromFile(fileName, 3);
        System.out.println(bookStrategy.rawData);
    }
}
