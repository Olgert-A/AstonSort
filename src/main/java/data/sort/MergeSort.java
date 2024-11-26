package data.sort;

import data.comparator.ParityChecker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSort<T> implements SortAlgorithm<T> {

    @Override
    public List<T> sort(List<T> data, Comparator<T> comparator) {
        if (data == null) return null;
        if (data.size() < 2) return data;
        int mid = data.size() / 2;
        List<T> left = new ArrayList<>(data.subList(0, mid));
        List<T> right = new ArrayList<>(data.subList(mid, data.size()));

        left = sort(left, comparator);
        right = sort(right, comparator);
        return merge(left, right, comparator);
    }

    private List<T> merge(List<T> a, List<T> b, Comparator<T> comparator) {
        int pA = 0;
        int pB = 0;
        int al = a.size();
        int bl = b.size();
        List<T> res = new ArrayList<>();

        while (pA < al && pB < bl) {
            T e1 = a.get(pA);
            T e2 = b.get(pB);
            int compare = comparator.compare(e1, e2);
            if (compare == 0) {
                res.add(a.get(pA++));
                res.add(b.get(pB++));
                continue;
            }
            if (compare > 0) {
                res.add(a.get(pA++));
            } else res.add(b.get(pB++));
        }

        while (pA < al) {
            res.add(a.get(pA++));
        }

        while (pB < bl) {
            res.add(b.get(pB++));
        }

        return res;
    }

    @Override
    public List<T> sortEvenValues(List<T> data, Comparator<T> comparator, ParityChecker<T> parityChecker) {
        return List.of();
    }
}
