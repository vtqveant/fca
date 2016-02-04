package ru.eventflow.fca;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ListUtilsTest {

    public static final int LIMIT = 100;

    private static <T> List<T> slowIntersect(List<T> A, List<T> B) {
        List<T> u = new ArrayList<>(A);
        u.retainAll(B);
        return u;
    }

    @Test
    public void testRetainAll() {
        List<Integer> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            l1.add(Math.round((float) Math.random() * 100));
            l2.add(Math.round((float) Math.random() * 100));
        }
        Collections.sort(l1);
        Collections.sort(l2);
        System.out.println("intersect by retainAll() vs. manual intersection of sorted lists...");

        List<List<Integer>> r1 = new ArrayList<>(LIMIT);
        List<List<Integer>> r2 = new ArrayList<>(LIMIT);

        long t1 = System.currentTimeMillis();
        for (int i = 0; i < LIMIT; i++) {
            r1.add(slowIntersect(l1, l2));
        }
        long t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);

        long t3 = System.currentTimeMillis();
        for (int i = 0; i < LIMIT; i++) {
            r2.add(ListUtils.intersect(l1, l2));
        }
        long t4 = System.currentTimeMillis();
        System.out.println(t4 - t3);

        for (int i = 0; i < LIMIT; i++) {
            if (r1.get(i).size() != r2.get(i).size() || !r1.get(i).containsAll(r2.get(i))) {
                System.out.println(l1);
                System.out.println(l2);
                System.out.println(r1.get(i));
                System.out.println(r2.get(i));
                fail();
            }
        }
    }

    @Test
    public void testManualIntersect() {
        List<Integer> l1 = Arrays.asList(0, 1, 1, 2, 3, 4, 5, 6);
        List<Integer> l2 = Arrays.asList(1, 2, 3, 5);
        List<Integer> res = ListUtils.intersect(l1, l2);
        System.out.println(res); // [1, 1, 2, 3, 5]
    }

    @Test
    public void testContainsAll() {
        List<Integer> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            l1.add(Math.round((float) Math.random() * 100));
            l2.add(Math.round((float) Math.random() * 100));
        }
        Collections.sort(l1);
        Collections.sort(l2);
        System.out.println("containsAll() vs. manual check of sorted lists...");

        List<Boolean> r1 = new ArrayList<>(LIMIT);
        List<Boolean> r2 = new ArrayList<>(LIMIT);

        long t1 = System.currentTimeMillis();
        for (int i = 0; i < LIMIT; i++) {
            r1.add(l1.containsAll(l2));
        }
        long t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);

        long t3 = System.currentTimeMillis();
        for (int i = 0; i < LIMIT; i++) {
            r2.add(ListUtils.containsAll(l1, l2));
        }
        long t4 = System.currentTimeMillis();
        System.out.println(t4 - t3);

        for (int i = 0; i < LIMIT; i++) {
            if (r1.get(i) != r2.get(i)) {
                System.out.println(l1);
                System.out.println(l2);
                System.out.println(r1.get(i));
                System.out.println(r2.get(i));
                fail();
            }
        }
    }

    @Test
    public void testManualContainsAll() {
        List<Integer> l1 = Arrays.asList(0, 1, 1, 2, 3, 4, 5, 6);
        List<Integer> l2 = Arrays.asList(1, 3, 2, 4);
        boolean res = ListUtils.containsAll(l1, l2);
        assertTrue(res);
        boolean res2 = ListUtils.containsAll(l2, l1);
        assertFalse(res2);
    }

    @Test
    public void testEquality() {
        List<Integer> l1 = Arrays.asList(0, 1, 1, 2, 3, 4, 5, 6);
        List<Integer> l2 = Arrays.asList(1, 3, 2, 0, 4);
        assertFalse(ListUtils.eq(l1, l2));

        List<Integer> l3 = Arrays.asList(0, 1, 1, 2, 0, 3, 4, 5, 6);
        List<Integer> l4 = Arrays.asList(0, 1, 1, 2, 0, 3, 4, 5, 6);
        assertTrue(ListUtils.eq(l3, l4));
    }

}
