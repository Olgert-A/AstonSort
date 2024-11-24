package data.search;

import java.util.Comparator;
import java.util.List;

public interface SearchAlgorithm<T> {
    List<T> findByField(Number fieldValue, Comparator<T> comparator);
}