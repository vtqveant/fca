package ru.eventflow.fca;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LatticeTest {

    private FCA<Integer, String> fca;
    private DiagramBuilder<Integer, String> diagramBuilder;

    @Before
    public void setUp() {
        FormalContext<Integer, String> context = new FormalContext<>();
        context.put(1, Arrays.asList("a", "c", "f", "h"));
        context.put(2, Arrays.asList("a", "c", "g", "i"));
        context.put(3, Arrays.asList("a", "d", "g", "i"));
        context.put(4, Arrays.asList("b", "c", "f", "h"));
        context.put(5, Arrays.asList("b", "e", "g"));
        fca = new FCA<>(context);

        diagramBuilder = new DiagramBuilder<>(fca);
    }

    @Test
    public void testSort() {
        List<FormalConcept<Integer, String>> concepts = fca.getConcepts();
        System.out.println(concepts);

        List<FormalConcept<Integer, String>> sorted = diagramBuilder.sort(fca.getConcepts());
        System.out.println(sorted);
    }

    @Test
    public void testDiagramBuilder() {
        DiagramBuilder<Integer, String> diagramBuilder = new DiagramBuilder<>(fca);
        List<FormalConcept<Integer, String>> lattice = diagramBuilder.buildHasseDiagram();

        String xml = GraphMLWriter.serialize(lattice);
        System.out.println(xml);
    }

    @Test
    public void testAnimalsDiagramBuilder() {
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
        DiagramBuilder<String, String> diagramBuilder = new DiagramBuilder<>(fca);
        List<FormalConcept<String, String>> lattice = diagramBuilder.buildHasseDiagram();

        String xml = GraphMLWriter.serialize(lattice);
        System.out.println(xml);
    }
}
