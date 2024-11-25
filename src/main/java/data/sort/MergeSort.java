package data.sort;

import data.comparator.ParityChecker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSort implements SortAlgorithm {

    @Override
    public List sort(List data, Comparator comparator) {
        if (data == null) return null;
        if (data.size() < 2) return data;
        int mid = data.size() / 2;
        List left = new ArrayList(data.subList(0, mid));//List.copyOf(data.subList(0, mid));
        List right = new ArrayList(data.subList(mid, data.size()));

        left = sort(left, comparator);
        right = sort(right, comparator);
        return merge(left, right, comparator);
    }

    public List merge(List a, List b, Comparator comparator) {
        int pA = 0;
        int pB = 0;
        int size = a.size() + b.size();
        int k = 0;
        List res = new ArrayList();

        while (size > k + 1) {
            if (pA < a.size() && pB < b.size()) {
                if (comparator.compare(a.get(pA), b.get(pB)) == 0) {
                    res.add(a.get(pA++));
                    res.add(b.get(pB++));
                }
                if (comparator.compare(a.get(pA), b.get(pB)) > 0) {
                    res.add(a.get(pA++));
                } else res.add(b.get(pB++));
            }
            k++;
        }

        while (pA < a.size()) {
            res.add(a.get(pA++));
        }

        while (pB < b.size()) {
            res.add(b.get(pB++));
        }

        return res;
    }

    @Override
    public List sortEvenValues(List data, Comparator comparator, ParityChecker parityChecker) {
        return List.of();
    }
}
