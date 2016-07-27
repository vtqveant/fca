package ru.eventflow.fca;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FormalConcept<O, A> {

    public static long ID;
    private long id = ID++;

    private List<O> extent;
    private List<A> intent;
    private Set<FormalConcept<O, A>> covers = new HashSet<>();

    public FormalConcept(List<O> extent, List<A> intent) {
        this.extent = extent;
        this.intent = intent;
    }

    public List<O> getExtent() {
        return extent;
    }

    public List<A> getIntent() {
        return intent;
    }

    public void addCover(FormalConcept<O, A> cover) {
        covers.add(cover);
    }

    public Set<FormalConcept<O, A>> getCovers() {
        return covers;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "<" + extent + ", " + intent + ">";
    }
}
