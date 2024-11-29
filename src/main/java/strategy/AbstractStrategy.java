package strategy;

import data.search.SearchAlgorithm;
import data.sort.SortAlgorithm;
import data.util.ParityChecker;
import util.AppUtil;
import util.enums.SortTypeEnum;

import java.io.IOException;
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

    @Override
    public void showCollectedData() {
        System.out.println("Исходные данные:");
        for (var book : this.rawData)
            System.out.println(book);
    }

    @Override
    public void showResults() {
        System.out.println("Результат:");
        for (var book : this.processedData)
            System.out.println(book);
    }

    void sortByField(SortTypeEnum sortType,
                     Comparator<T> comparator,
                     ParityChecker<T> parityChecker) throws IOException {
        switch (sortType) {
            case SORT -> this.processedData = this.sortAlgorithm.sort(this.rawData, comparator);
            case SORTEVENVALUES -> {
                if (parityChecker == null)
                    throw new IOException("Нельзя использовать сортировку четных полей для выбранного поля");

                this.processedData =  this.sortAlgorithm.sortEvenValues(this.rawData, comparator, parityChecker);
            }
        }
    }
}
