package com.msoroka.assets;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.*;
import java.awt.*;

public class Graph {
    private Task[] tasks;

    public Graph(Task[] tasks) {
        this.tasks = tasks;

        createGraph();
    }

    private void createGraph() {
        File imgFile = new File("graph.png");

        try {
            imgFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DefaultDirectedGraph<String, DefaultEdge> g =
                new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);

        for (Task task : this.tasks) {
            g.addVertex(task.getName());
        }

        for (Task task : tasks) {
            if (!task.getPrevious().isEmpty()) {
                for (String parent : task.getPrevious()) {
                    g.addEdge(parent, task.getName());
                }
            }
        }

        try {
            graphToFile(g);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void graphToFile(DefaultDirectedGraph<String, DefaultEdge> g) throws IOException {

        JGraphXAdapter<String, DefaultEdge> graphAdapter =
                new JGraphXAdapter<String, DefaultEdge>(g);
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graphAdapter);

        graphAdapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_NOLABEL, "1");

        layout.setIntraCellSpacing(50);
        layout.setInterRankCellSpacing(40);
        layout.setOrientation(SwingConstants.NORTH);

        layout.execute(graphAdapter.getDefaultParent());


        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("graph.png");
        ImageIO.write(image, "PNG", imgFile);
    }
}