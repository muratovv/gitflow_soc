package analytic.graphs;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.util.UnionFind;

import java.util.*;

/**
 * @author @muratovv
 * @date 12.04.17
 */
public class MaxSpanningTree<V, E> implements SpanningTreeAlgorithm<E> {

    private final Graph<V, E> graph;

    public MaxSpanningTree(Graph<V, E> graph) {

        this.graph = graph;
    }

    @Override
    public SpanningTree<E> getSpanningTree() {
        UnionFind<V> forest   = new UnionFind<>(this.graph.vertexSet());
        ArrayList<E> allEdges = new ArrayList<>(this.graph.edgeSet());
        allEdges.sort(Comparator.comparing(edge2 -> (this.graph.getEdgeWeight(edge2))));
        Collections.reverse(allEdges);
        double spanningTreeCost = 0.0D;
        Set<E> edgeList         = new HashSet<>();

        for (E edge : allEdges) {
            V source = this.graph.getEdgeSource(edge);
            V target = this.graph.getEdgeTarget(edge);
            if (!forest.find(source).equals(forest.find(target))) {
                forest.union(source, target);
                edgeList.add(edge);
                spanningTreeCost += this.graph.getEdgeWeight(edge);
            }
        }

        return new SpanningTreeImpl<>(edgeList, spanningTreeCost);
    }
}
