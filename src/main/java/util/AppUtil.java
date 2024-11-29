package util;

import data.search.BinarySearch;
import data.search.SearchAlgorithm;
import data.sort.MergeSort;
import data.sort.SortAlgorithm;

public class AppUtil {
    public static <T> SortAlgorithm<T> getSortAlgorithm() {
        return new MergeSort<>();
    }

    public static <T> SearchAlgorithm<T> getSearchAlgorithm() {
        return new BinarySearch<>();
    }

}
