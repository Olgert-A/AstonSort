package data.search;

import java.util.Comparator;
import java.util.List;

public class BinarySearch<T> implements SearchAlgorithm<T> {
    @Override
    public T findByField(List<T> data, T fieldValue, Comparator<T> comparator) {

        data.sort(comparator);

        int left = 0;
        int right = data.size() - 1;

        while (left <= right) {
             int mid = (left + right) / 2;
            T midItem = data.get(mid);

            int compareResult = comparator.compare(midItem, fieldValue);

            if (compareResult == 0) {
                return midItem;
            } else if (compareResult < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return null;
    }
}
