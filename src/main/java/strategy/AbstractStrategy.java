package strategy;

import data.search.SearchAlgorithm;
import data.sort.SortAlgorithm;
import data.util.ParityChecker;
import util.AppUtil;
import util.enums.SortTypeEnum;

import java.util.ArrayList;
import java.util.Comparator;
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

    void sortByField(SortTypeEnum sortType, Comparator<T> comparator, ParityChecker<T> parityChecker) throws Exception {
        switch (sortType) {
            case SORT -> this.processedData = this.sortAlgorithm.sort(this.rawData, comparator);
            case SORTEVENVALUES -> {
                if (parityChecker == null) {
                    System.out.println("Нельзя использовать сортировку четных полей для выбранного поля");
                    throw new Exception("AbstractStrategy.sortByField()");
                } else
                    this.processedData = this.sortAlgorithm.sortEvenValues(this.rawData, comparator, parityChecker);
            }
        }
    }
}
