package ru.eventflow.fca;

import java.util.ArrayList;
import java.util.List;

/**
 * Fast list operations, require sorted input
 */
public class ListUtils {

    public static <T extends Comparable<T>> List<T> intersect(List<T> A, List<T> B) {
        List<T> u = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < A.size() && j < B.size()) {
            int z = A.get(i).compareTo(B.get(j));
            if (z < 0) {
                i++;
            } else if (z > 0) {
                j++;
            } else {
                u.add(A.get(i));
                i++;
            }
        }
        return u;
    }

    public static <T extends Comparable<T>> boolean containsAll(List<T> A, List<T> B) {
        int i = 0;
        int j = 0;
        int size = 0;
        while (i < A.size() && j < B.size()) {
            int z = A.get(i).compareTo(B.get(j));
            if (z < 0) {
                i++;
            } else if (z > 0) {
                j++; // we can speed up further by just returning false here
                // if we assume that all attributes are unique
                // return false;
            } else {
                size++;
                i++;
            }
        }
        return size == B.size();
    }

    public static <T extends Comparable<T>> boolean eq(List<T> A, List<T> B) {
        if (A.size() != B.size()) return false;
        for (int i = 0; i < A.size(); i++) {
            if (!A.get(i).equals(B.get(i))) {
                return false;
            }
        }
        return true;
    }

}
