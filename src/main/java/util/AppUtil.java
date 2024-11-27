package util;

import data.search.SearchAlgorithm;
import data.sort.SortAlgorithm;
import data.util.ParityChecker;

import java.util.Comparator;
import java.util.List;

public class AppUtil {
    public static <T> SortAlgorithm<T> getSortAlgorithm() {
        return new SortAlgorithm<T>() {
            @Override
            public List<T> sort(List<T> data, Comparator<T> comparator) {
                return List.of();
            }

            @Override
            public List<T> sortEvenValues(List<T> data, Comparator<T> comparator, ParityChecker<T> parityChecker) {
                return List.of();
            }
        };
    }

    public static <T> SearchAlgorithm<T> getSearchAlgorithm() {
        return (data, searchCondition, comparator) -> List.of();
    }

}
