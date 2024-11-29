package util;

import data.search.BinarySearch;
import data.search.SearchAlgorithm;
import data.sort.MergeSort;
import data.sort.SortAlgorithm;
import data.util.ParityChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppUtil {
    public static <T> SortAlgorithm<T> getSortAlgorithm() {
        return new MergeSort<>();
    }

    public static <T> SearchAlgorithm<T> getSearchAlgorithm() {
        return new BinarySearch<>();
    }

}
