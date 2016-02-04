package ru.eventflow.fca;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class FCATest {

    private FCA<String, Integer> fca;

    @Before
    public void setUp() {
        FormalContext<String, Integer> context = new FormalContext<>();
        context.put("x1", Arrays.asList(1, 2, 3));
        context.put("x2", Arrays.asList(1, 3));
        context.put("x3", Arrays.asList(2, 3));
        context.put("x4", Arrays.asList(1));
        fca = new FCA<>(context);
    }

    @Test
    public void testLexicographicOrder() {
        final List<Integer> Y = Arrays.asList(0, 1, 2, 3, 4, 5);
        // {2} <_1 {1}
        assertTrue(FCA.lt(Arrays.asList(1), Arrays.asList(0), Y, 0));
        // {3, 6} <_4 {3, 4, 5}
        assertTrue(FCA.lt(Arrays.asList(2, 5), Arrays.asList(2, 3, 4), Y, 3));
        // {3, 4, 5} <_4 {3, 6}
        assertFalse(FCA.lt(Arrays.asList(2, 3, 4), Arrays.asList(5), Y, 3));
    }

    @Test
    public void testAdjoints() {
        FormalContext<Integer, Integer> ctx = new FormalContext<>();
        ctx.put(0, Arrays.asList(0, 1, 3));
        ctx.put(1, Arrays.asList(0, 1, 2, 3));
        ctx.put(2, Arrays.asList(1));

        FCA<Integer, Integer> fca = new FCA<>(ctx);

        // [x1, x2]
        List r1 = fca.leftAdjoint(Arrays.asList(0, 1));
        assertEquals(2, r1.size());
        assertEquals(0, r1.get(0));
        assertEquals(1, r1.get(1));

        // [0, 1, 3]
        List r2 = fca.rightAdjoint(Arrays.asList(0, 1));
        assertEquals(3, r2.size());
        assertEquals(0, r2.get(0));
        assertEquals(1, r2.get(1));
        assertEquals(3, r2.get(2));

        // all attributes
        List r3 = fca.rightAdjoint(Collections.<Integer>emptyList());
        assertEquals(ctx.getAttributes().size(), r3.size());
    }

    @Test
    public void testSuccessor() {
        // [3]
        List s1 = fca.successor(new ArrayList<Integer>(0));
        assertEquals(1, s1.size());
        assertEquals(3, s1.get(0));

        // [2,3]
        List s2 = fca.successor(Collections.singletonList(3));
        assertEquals(2, s2.size());
        assertEquals(2, s2.get(0));
        assertEquals(3, s2.get(1));

        // [1]
        List s3 = fca.successor(Arrays.asList(2, 3));
        assertEquals(1, s3.size());
        assertEquals(1, s3.get(0));

        // [1, 3]
        List s4 = fca.successor(Collections.singletonList(1));
        assertEquals(2, s4.size());
        assertEquals(1, s4.get(0));
        assertEquals(3, s4.get(1));

        // [1, 2, 3]
        List s5 = fca.successor(Arrays.asList(1, 3));
        assertEquals(3, s5.size());
        assertEquals(1, s5.get(0));
        assertEquals(2, s5.get(1));
        assertEquals(3, s5.get(2));
    }

    // [<[x1, x2, x3, x4], []>, <[x1, x2, x3], [3]>, <[x1, x3], [2, 3]>, <[x1, x2, x4], [1]>, <[x1, x2], [1, 3]>, <[x1], [1, 2, 3]>]
    @Test
    public void testFCA() {
        List<FormalConcept<String, Integer>> lattice = fca.getConcepts();
        assertEquals(6, lattice.size());
        System.out.println(lattice);
    }

    @Test
    public void testAnimals() {
        FormalContext<String, String> ctx = new FormalContext<>();
        String a = "needs water to live";
        String b = "lives in water";
        String c = "lives on land";
        String d = "needs chlorophyll to produce food";
        String e = "two seed leaves";
        String f = "one seed leaf";
        String g = "can move around";
        String h = "has limbs";
        String i = "suckles its offspring";
        ctx.put("leech", Arrays.asList(a, b, g));
        ctx.put("bream", Arrays.asList(a, b, g, h));
        ctx.put("frog", Arrays.asList(a, b, c, g, h));
        ctx.put("dog", Arrays.asList(a, c, g, h, i));
        ctx.put("spike-weed", Arrays.asList(a, b, d, f));
        ctx.put("reed", Arrays.asList(a, b, c, d, f));
        ctx.put("bean", Arrays.asList(a, c, d, e));
        ctx.put("maize", Arrays.asList(a, c, d, f));

        FCA<String, String> fca = new FCA<>(ctx);
        List<FormalConcept<String, String>> lattice = fca.getConcepts();
        assertEquals(19, lattice.size());
        System.out.println(lattice);
    }

}
