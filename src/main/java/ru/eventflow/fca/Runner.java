package ru.eventflow.fca;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Runner {

    public static final String FILENAME = "/prefixes.csv";

    public Runner() {
    }

    public static void main(String[] args) throws IOException {
        Runner runner = new Runner();
        runner.run();
    }

    public void run() throws IOException {
        InputStreamReader in = new InputStreamReader(getClass().getResourceAsStream(FILENAME));
        CSVParser parser = new CSVParser(in, CSVFormat.RFC4180);
        List<CSVRecord> list = parser.getRecords();
        CSVRecord header = list.get(0);

        FormalContext<String, String> ctx = new FormalContext<>();

        for (int j = 1; j < list.size(); j++) {
            CSVRecord record = list.get(j);
            List<String> attributes = new ArrayList<>();
            for (int i = 1; i < record.size(); i++) {
                if (!record.get(i).equals("")) {
                    attributes.add(header.get(i));
                }
            }
            ctx.put(record.get(0), attributes);
        }

        FCA<String, String> fca = new FCA<>(ctx);
        DiagramBuilder<String, String> diagramBuilder = new DiagramBuilder<>(fca);
        List<FormalConcept<String, String>> lattice = diagramBuilder.buildHasseDiagram();

        System.out.format("%d objects\n", list.size() - 1);
        System.out.format("%d attributes\n", header.size() - 1);
        System.out.format("%d formal concepts\n\n", lattice.size());

        String xml = GraphMLWriter.serialize(lattice);
        System.out.println(xml);
    }
}
