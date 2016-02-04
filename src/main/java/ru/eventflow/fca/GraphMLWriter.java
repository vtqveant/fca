package ru.eventflow.fca;

import java.util.List;

public class GraphMLWriter {

    private static final String ATTR_EXTENT = "<key id=\"extent\" for=\"node\" attr.name=\"extent\" attr.type=\"string\"/>\n";
    private static final String ATTR_INTENT = "<key id=\"intent\" for=\"node\" attr.name=\"intent\" attr.type=\"string\"/>\n";

    private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "    xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">\n" +
            ATTR_EXTENT +
            ATTR_INTENT +
            "<graph id=\"G\" edgedefault=\"directed\">\n";

    private static final String FOOTER = "</graph>\n</graphml>";

    public static <O, A extends Comparable<A>> String serialize(List<FormalConcept<O, A>> concepts) {
        StringBuilder sb = new StringBuilder();
        sb.append(HEADER);

        StringBuilder sb2 = new StringBuilder();

        for (FormalConcept<O, A> concept : concepts) {
            sb.append("<node id=\"");
            sb.append(concept.getId());
            sb.append("\">\n");
            sb.append("    <data key=\"extent\">");
            sb.append(join(concept.getExtent()));
            sb.append("</data>\n");
            sb.append("    <data key=\"intent\">");
            sb.append(join(concept.getIntent()));
            sb.append("</data>\n");
            sb.append("</node>\n");

            for (FormalConcept<O, A> to : concept.getCovers()) {
                sb2.append("<edge source=\"");
                sb2.append(concept.getId());
                sb2.append("\" target=\"");
                sb2.append(to.getId());
                sb2.append("\"/>\n");
            }
        }

        sb.append(sb2);
        sb.append(FOOTER);
        return sb.toString();
    }

    private static <T> String join(List<T> strings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            sb.append(strings.get(i));
            if (i < strings.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
