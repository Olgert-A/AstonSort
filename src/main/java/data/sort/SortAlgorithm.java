package data.sort;

import data.comparator.ParityChecker;

import java.util.Comparator;
import java.util.List;

public interface SortAlgorithm<T> {
    List<T> sort(List<T> data, Comparator<T> comparator);
    List<T> sortEvenValues(List<T> data, Comparator<T> comparator, ParityChecker<T> parityChecker);
}
