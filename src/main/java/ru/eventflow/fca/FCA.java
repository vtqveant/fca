package ru.eventflow.fca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Next Closure algorithm (Ganter 1987) for Formal Concept Analysis
 * <p/>
 * s. Belohlavek, 2008. Introduction to Formal Concept Analysis.
 */
public class FCA<O, A extends Comparable<A>> {

    private FormalContext<O, A> context;

    public FCA(FormalContext<O, A> contex) {
        this.context = contex;

        // intersect requires sorted input
        for (Map.Entry<O, List<A>> entry : contex.entrySet()) {
            Collections.sort(entry.getValue());
        }
    }

    /**
     * Y is a set of attributes
     * A <_i B if $y_i \in B \setminus A and A \cap \{y_1, \dots, y_{i-1} \} \eq A \cap \{y_1, \dots, y_{i-1} \}$
     *
     * @param A subset of Y
     * @param B subset of Y
     * @param i an index of the i-th element of Y
     */
    public static <T extends Comparable<T>> boolean lt(List<T> A, List<T> B, List<T> Y, int i) {
        if (!B.contains(Y.get(i)) || A.contains(Y.get(i))) {
            return false;
        }
        final List<T> sublist = Y.subList(0, i);
        List<T> a = ListUtils.intersect(A, sublist);
        List<T> b = ListUtils.intersect(B, sublist);
        return (a.size() == b.size() && a.containsAll(b));
    }

    public List<FormalConcept<O, A>> getConcepts() {
        List<FormalConcept<O, A>> lattice = new ArrayList<>();

        List<A> A = closure(Collections.<A>emptyList());
        lattice.add(new FormalConcept<>(leftAdjoint(A), A));

        while (A.size() != context.getAttributes().size()) {
            A = successor(A);
            lattice.add(new FormalConcept<>(leftAdjoint(A), A));
        }
        return lattice;
    }

    /**
     * A + y_i := closure((A \cap {y1, ... y_{i-1}) \cup {y_i}})
     * A+ := A + y_i s.t. A < A+ (i.e. i is the greatest one with A <_i A + y_i)
     */
    public List<A> successor(List<A> attributes) {
        List<A> Y = context.getAttributes();
        for (int i = Y.size(); i > 0; i--) {
            List<A> a1 = ListUtils.intersect(attributes, Y.subList(0, i - 1));
            a1.add(Y.get(i - 1));
            List<A> next = closure(a1);
            if (lt(attributes, next, Y, i - 1)) {
                return next;
            }
        }
        return Collections.emptyList();
    }

    /**
     * for a set $A \subset X$ of objects compute its upper adjoint,
     * i.e. the set of attributes common to all these objects.
     */
    public List<A> rightAdjoint(List<O> objects) {
        if (objects.size() == 0) {
            return context.getAttributes();
        }

        List<A> u = new ArrayList<>(context.get(objects.get(0)));
        for (int i = 1; i < objects.size(); i++) {
            u = ListUtils.intersect(u, context.get(objects.get(i)));
        }
        return u;
    }

    /**
     * for a set $B \subset Y$ of attributes compute its lower adjoint,
     * i.e. the set of objects that have these attributes.
     * <p/>
     * <O, A> -- O for objects, A for attributes
     */
    public List<O> leftAdjoint(List<A> attributes) {
        List<O> u = new ArrayList<>();
        for (Map.Entry<O, List<A>> entry : context.entrySet()) {
            if (ListUtils.containsAll(entry.getValue(), attributes)) {
                u.add(entry.getKey());
            }
        }
        return u;
    }

    private List<A> closure(List<A> B) {
        return rightAdjoint(leftAdjoint(B));
    }

    public FormalContext<O, A> getContext() {
        return context;
    }
}
