package util;

import data.search.SearchAlgorithm;
import data.sort.SortAlgorithm;
import data.util.ParityChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppUtil {
    public static <T> SortAlgorithm<T> getSortAlgorithm() {
        return new SortAlgorithm<T>() {
            @Override
            public List<T> sort(List<T> data, Comparator<T> comparator) {
                List<T> result = new ArrayList<>(data);
                result.sort(comparator);
                return result;
            }

            @Override
            public List<T> sortEvenValues(List<T> data, Comparator<T> comparator, ParityChecker<T> parityChecker) {
                List<T> even = new ArrayList<>();
                List<Integer> evenIndexes = new ArrayList<>();

                for (int i = 0; i < data.size(); i++) {
                    T obj = data.get(i);
                    if(parityChecker.isEven(obj)) {
                        even.add(obj);
                        evenIndexes.add(i);
                    }
                }

                List<T> sortedEven = sort(even, comparator);
                List<T> result = new ArrayList<>(data);

                for (int i = 0; i < sortedEven.size(); i++) {
                    T obj = sortedEven.get(i);
                    int setIndex = evenIndexes.get(i);
                    result.set(setIndex,obj);
                }

                return result;
            }
        };
    }

    public static <T> SearchAlgorithm<T> getSearchAlgorithm() {
        return (data, searchCondition, comparator) -> {
            int index = Collections.binarySearch(data, searchCondition, comparator);
            T found = null;
            if(index>=0)
                found = data.get(index);

            return found;
        };
    }

}
