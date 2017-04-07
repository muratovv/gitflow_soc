package analytic.graphs;

import org.jgrapht.graph.SimpleWeightedGraph;

/**
 * This is a {@link SimpleWeightedGraph} with non-exception behavior, when add Edge with absent Vertex
 *
 * @param <V>
 * @param <E>
 */
public class SilentAuthorGraphRepr<V, E> extends SimpleWeightedGraph<V, E> {

    public SilentAuthorGraphRepr(Class<? extends E> edgeClass) {
        super(edgeClass);
    }

    @Override
    public boolean addEdge(V sourceVertex, V targetVertex, E e) {
        addVertexIfNotExists(sourceVertex);
        addVertexIfNotExists(targetVertex);
        return super.addEdge(sourceVertex, targetVertex, e);
    }

    private void addVertexIfNotExists(V v) {
        if (!containsVertex(v)) addVertex(v);
    }
}
