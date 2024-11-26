package data.search;

import java.util.Comparator;
import java.util.List;

public interface SearchAlgorithm<T> {
    List<T> findByField(List<T> data, T searchCondition, Comparator<T> comparator);
}