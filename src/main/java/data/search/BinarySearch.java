package data.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BinarySearch<T> implements SearchAlgorithm<T> {
    @Override
    public T findByField(List<T> data, T fieldValue, Comparator<T> comparator) {

        data.sort(comparator);

        int left = 0;
        int right = data.size() - 1;

      T result = null;

        while (left <= right) {
             int mid = (left + right) / 2;
            T midItem = data.get(mid);

            int compareResult = comparator.compare(midItem, fieldValue);

            if (compareResult == 0) {
                result = midItem;
                int i = mid - 1;
                while (i >= 0 && comparator.compare(data.get(i), (T) fieldValue) == 0) {
                    result = data.get(i);
                    i--;
                }
                i = mid + 1;
                while (i < data.size() && comparator.compare(data.get(i), (T) fieldValue) == 0) {
                    result = data.get(i);
                    i++;
                }
                break;
            } else if (compareResult < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }
}
