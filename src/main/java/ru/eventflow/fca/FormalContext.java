package ru.eventflow.fca;

import java.util.*;

public class FormalContext<O, A extends Comparable> extends HashMap<O, List<A>> {

    private List<A> attributes;

    public FormalContext() {
        super();
    }

    public List<A> getAttributes() {
        if (attributes == null) {
            Set<A> s = new HashSet<>();
            for (List<A> value : values()) {
                s.addAll(value);
            }
            attributes = new ArrayList<>(s);
            Collections.sort(attributes);
        }
        return attributes;
    }
}
