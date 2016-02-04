package ru.eventflow.fca;

import java.util.*;

/**
 * Valtchev, P., Missaoui, R., & Lebrun, P. (2000). A fast algorithm for building the Hasse diagram of a Galois lattice.
 */
public class DiagramBuilder<O, A extends Comparable<A>> {

    private List<FormalConcept<O, A>> concepts;
    private int maxIntentSize;
    private Map<Intent, FormalConcept<O, A>> generators = new HashMap<>();

    public DiagramBuilder(FCA<O, A> fca) {
        this.maxIntentSize = fca.getContext().getAttributes().size();
        this.concepts = sort(fca.getConcepts());
    }

    public List<FormalConcept<O, A>> buildHasseDiagram() {
        List<FormalConcept<O, A>> border = new ArrayList<>();
        border.add(concepts.get(0));

        for (int i = 1; i < concepts.size(); i++) {
            List<Intent> intents = new ArrayList<>();

            FormalConcept<O, A> c_i = concepts.get(i);
            for (FormalConcept<O, A> c_bar : border) {
                Intent intent = new Intent(ListUtils.intersect(c_bar.getIntent(), c_i.getIntent()));
                intents.add(intent);
                generators.put(intent, c_bar);
            }

            List<Intent> coverIntents = maxima(intents);
            for (Intent y : coverIntents) {
                FormalConcept<O, A> c_hat = findConcept(y);
                c_i.addCover(c_hat);
            }

            border.removeAll(c_i.getCovers());
            border.add(c_i);
        }

        return concepts;
    }

    /**
     * linear-time sorting of the concept set by the length of the intent
     */
    public List<FormalConcept<O, A>> sort(List<FormalConcept<O, A>> concepts) {
        List<Set<FormalConcept<O, A>>> buckets = new ArrayList<>(maxIntentSize + 1);
        for (int i = 0; i < maxIntentSize + 1; i++) {
            buckets.add(i, new HashSet<FormalConcept<O, A>>());
        }
        for (FormalConcept<O, A> c : concepts) {
            buckets.get(c.getIntent().size()).add(c);
        }

        LinkedList<FormalConcept<O, A>> result = new LinkedList<>();
        for (int i = 0; i < maxIntentSize + 1; i++) {
            for (FormalConcept<O, A> c : buckets.get(i)) {
                result.add(c);
            }
        }
        return result;
    }

    private List<Intent> maxima(List<Intent> intents) {
        Collections.sort(intents, new Comparator<Intent>() {
            @Override
            public int compare(Intent i1, Intent i2) {
                return i2.getAttributes().size() - i1.getAttributes().size();
            }
        });

        List<Intent> maximalIntents = new ArrayList<>();
        for (Intent intent : intents) {
            boolean isMin = true;
            for (Intent i : maximalIntents) {
                isMin = isMin && !ListUtils.containsAll(i.getAttributes(), intent.getAttributes());
            }
            if (isMin) {
                maximalIntents.add(intent);
            }
        }
        return maximalIntents;
    }

    private FormalConcept<O, A> findConcept(Intent y) {
        FormalConcept<O, A> c = generators.get(y);
        while (!ListUtils.eq(c.getIntent(), y.getAttributes())) {
            Iterator<FormalConcept<O, A>> it = c.getCovers().iterator();
            FormalConcept<O, A> next = it.next();
            while (!ListUtils.containsAll(next.getIntent(), y.getAttributes())) {
                next = it.next();
            }
            c = next;
        }
        return c;
    }

    private class Intent {
        private List<A> attributes;

        public Intent(List<A> attributes) {
            this.attributes = attributes;
        }

        public List<A> getAttributes() {
            return attributes;
        }

        @Override
        public String toString() {
            return attributes.toString();
        }
    }

}
