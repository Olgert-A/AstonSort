package strategy;

import data.search.SearchAlgorithm;
import data.sort.SortAlgorithm;
import util.AppUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStrategy<T> implements Strategy {
    protected List<T> rawData;
    protected List<T> processedData;
    protected SortAlgorithm<T> sortAlgorithm;
    protected SearchAlgorithm<T> searchAlgorithm;

    public AbstractStrategy() {
        this.rawData = new ArrayList<>();
        this.processedData = new ArrayList<>();
        this.sortAlgorithm = AppUtil.getSortAlgorithm();
        this.searchAlgorithm = AppUtil.getSearchAlgorithm();
    }
}
