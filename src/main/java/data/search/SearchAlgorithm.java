package data.search;

import java.util.Comparator;
import java.util.List;

public interface SearchAlgorithm<T> {
    T findByField(List<T> data,  T fieldValue, Comparator<T> comparator);
}