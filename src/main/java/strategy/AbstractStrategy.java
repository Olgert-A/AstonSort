package strategy;

import data.entities.Book;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStrategy implements Strategy {
    private List<Book> rawData;
    private List<Book> processedData;

    public AbstractStrategy() {
        this.rawData = new ArrayList<>();
        this.processedData = new ArrayList<>();
    }
}
