package ru.eventflow.fca;

import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.renderers.VertexLabelAsShapeRenderer;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ChainedTransformer;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class LatticeVisualizer {

//    private static final String XML = "/prefixes.graphml";
    private static final String XML = "/grassmanian.graphml";

    public static void main(String[] args) {

        try {
            InputStream is = LatticeVisualizer.class.getResourceAsStream(XML);
            DirectedGraph<Node, Edge> graph = GraphReader.buildGraph(is);

            Layout<Node, Edge> layout = new DAGLayout<>(graph);
//            Layout<Node, Edge> layout = new StaticLayout<>(graph);
            Dimension preferredSize = new Dimension(800, 600);
            final VisualizationModel<Node, Edge> visualizationModel = new DefaultVisualizationModel<>(layout, preferredSize);
            VisualizationViewer<Node, Edge> vv = new VisualizationViewer<>(visualizationModel, preferredSize);

            final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();

            vv.setGraphMouse(graphMouse);

            JComboBox modeBox = graphMouse.getModeComboBox();
            modeBox.addItemListener(graphMouse.getModeListener());
            graphMouse.setMode(ModalGraphMouse.Mode.PICKING);


            // this class will provide both label drawing and vertex shapes
            VertexLabelAsShapeRenderer<Node, Edge> vlasr = new VertexLabelAsShapeRenderer<>(vv.getRenderContext());

            // customize the render context
            vv.getRenderContext().setVertexLabelTransformer(
                    // this chains together Transformers so that the html tags are prepended to the toString method output
                    new ChainedTransformer<Node, String>(new Transformer[]{
                            new Transformer<Node, String>() {
                                @Override
                                public String transform(Node node) {
                                    return node.getExtent() + "<br/>" +
                                            (node.getIntent().length() == 0 ? "&nbsp;" : node.getIntent());
                                }
                            },
                            new Transformer<String, String>() {
                                public String transform(String input) {
                                    return "<html><center><font size=\"2\">" + input + "</font></html>";
                                }
                            }}));
            vv.getRenderContext().setVertexShapeTransformer(vlasr);

            // customize the renderer
            vv.getRenderer().setVertexLabelRenderer(vlasr);

            vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());

            JFrame jf = new JFrame();
            jf.getContentPane().add(new GraphZoomScrollPane(vv));

            jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jf.pack();
            jf.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

