package strategy;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStrategy<T> implements Strategy {
    private List<T> rawData;
    private List<T> processedData;

    public AbstractStrategy() {
        this.rawData = new ArrayList<>();
        this.processedData = new ArrayList<>();
    }
}
