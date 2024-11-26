package data.sort;

import data.comparator.ParityChecker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSort<T> implements SortAlgorithm<T> {

    @Override
    public List<T> sort(List<T> data, Comparator<T> comparator) {
        return sortEvenValues(data, comparator, null);
    }

    @Override
    public List<T> sortEvenValues(List<T> data, Comparator<T> comparator, ParityChecker<T> parityChecker) {
        if (data == null) return new ArrayList<>();
        if (data.size() < 2) return new ArrayList<>(data);
        int mid = data.size() / 2;
        List<T> left = new ArrayList<>(data.subList(0, mid));
        List<T> right = new ArrayList<>(data.subList(mid, data.size()));

        left = sort(left, comparator);
        right = sort(right, comparator);
        return merge(left, right, comparator, parityChecker);
    }

    private List<T> merge(List<T> left, List<T> right, Comparator<T> comparator, ParityChecker<T> parityChecker) {
        int leftPointer = 0;
        int rightPointer = 0;
        int leftLen = left.size();
        int rightLen = right.size();
        boolean parityCheckerFlag = parityChecker != null;
        List<T> res = new ArrayList<>();
        List<T> resImposter = new ArrayList<>();

        while (leftPointer < leftLen && rightPointer < rightLen) {
            T e1 = left.get(leftPointer);
            T e2 = right.get(rightPointer);
            if (parityCheckerFlag) {
                if (!parityChecker.isEven(e1)) {
                    res.set(leftPointer++, e1);
                    e1 = left.get(leftPointer);
                }
                if (!parityChecker.isEven(e2)) {
                    resImposter.set(rightPointer++, e2);
                    e2 = right.get(rightPointer);
                }
            }
            int compare = comparator.compare(e1, e2);
            if (compare == 0) {
                res.add(e1);
                leftPointer++;
                res.add(e2);
                rightPointer++;
                continue;
            }
            if (compare > 0) {
                res.add(e1);
                leftPointer++;
            } else {
                res.add(e2);
                rightPointer++;
            }
        }

        res.addAll(resImposter);

        while (leftPointer < leftLen) {
            res.add(left.get(leftPointer++));
        }

        while (rightPointer < rightLen) {
            res.add(right.get(rightPointer++));
        }
        return res;
    }
}
