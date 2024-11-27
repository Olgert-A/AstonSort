package data.sort;

import data.comparator.ParityChecker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSort<T> implements SortAlgorithm<T> {

    @Override
    public List<T> sort(List<T> data, Comparator<T> comparator) {
        if (data == null) return new ArrayList<>();
        if (data.size() < 2) return new ArrayList<>(data);
        int mid = data.size() / 2;
        List<T> left = new ArrayList<>(data.subList(0, mid));
        List<T> right = new ArrayList<>(data.subList(mid, data.size()));

        left = sort(left, comparator);
        right = sort(right, comparator);
        return merge(left, right, comparator);
    }

    @Override
    public List<T> sortEvenValues(List<T> data, Comparator<T> comparator, ParityChecker<T> parityChecker) {
        List<Integer> evenPosition = new ArrayList<>();
        List<T> elemsToSort = new ArrayList<>();
        List<T> result = new ArrayList<>(data);

        data.forEach(elem -> {
            if (parityChecker.isEven(elem)) {
                elemsToSort.add(elem);
                evenPosition.add(data.indexOf(elem));
            }
        });

        List<T> sorted = sort(elemsToSort, comparator);

        for (int i = 0; i < evenPosition.size(); i++) {
            result.set(evenPosition.get(i), sorted.get(i));
        }
        return result;
    }

    private List<T> merge(List<T> left, List<T> right, Comparator<T> comparator) {
        int leftPointer = 0;
        int rightPointer = 0;
        int leftLen = left.size();
        int rightLen = right.size();
        List<T> res = new ArrayList<>();

        while (leftPointer < leftLen && rightPointer < rightLen) {
            T e1 = left.get(leftPointer);
            T e2 = right.get(rightPointer);
            int compare = comparator.compare(e1, e2);
            if (compare == 0) {
                res.add(e1);
                leftPointer++;
                res.add(e2);
                rightPointer++;
                continue;
            }
            if (compare < 0) {
                res.add(e1);
                leftPointer++;
            } else {
                res.add(e2);
                rightPointer++;
            }
        }

        while (leftPointer < leftLen) {
            res.add(left.get(leftPointer++));
        }

        while (rightPointer < rightLen) {
            res.add(right.get(rightPointer++));
        }
        return res;
    }
}
