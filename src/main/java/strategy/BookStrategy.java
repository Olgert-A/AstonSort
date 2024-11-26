package strategy;

import data.entities.Book;
import view.BookFieldEnum;

import java.util.Objects;

import static view.ConsoleUtil.getValue;

public class BookStrategy extends AbstractStrategy<Book> implements Strategy {
    @Override
    public boolean collectInputData(int amount) {
        return false;
    }

    @Override
    public boolean collectRandomData(int amount) {
        return false;
    }

    @Override
    public boolean collectDataFromFile(String name, int amount) {
        return false;
    }

    @Override
    public void saveResultsToFile(String name) {

    }

    @Override
    public boolean sort() {
        return false;
    }

    @Override
    public boolean search() {
        return false;
    }

    @Override
    public void showResults() {

    }
}
