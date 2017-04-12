package analytic.graphs;

import org.jgrapht.graph.SimpleWeightedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a {@link SimpleWeightedGraph} with non-exception behavior, when add Edge with absent Vertex
 *
 * @param <V>
 * @param <E>
 */
public class SilentAuthorGraphRepr<V, E> extends SimpleWeightedGraph<V, E> {
    private static final Logger logger = LoggerFactory.getLogger(SilentAuthorGraphRepr.class);

    public SilentAuthorGraphRepr(Class<? extends E> edgeClass) {
        super(edgeClass);
    }

    @Override
    public boolean addEdge(V sourceVertex, V targetVertex, E e) {
        try {
            if (!equals(sourceVertex, targetVertex)) {
                addVertexIfNotExists(sourceVertex);
                addVertexIfNotExists(targetVertex);
                return super.addEdge(sourceVertex, targetVertex, e);
            }
        } catch (Exception exc) {
            logger.error(String.format("Failed on (%s, %s) edge", sourceVertex, targetVertex), exc);
        }
        return false;
    }

    private void addVertexIfNotExists(V v) {
        if (!containsVertex(v)) addVertex(v);
    }

    private static <V> boolean equals(V sourceVertex, V targetVertex) {
        return sourceVertex.equals(targetVertex);
    }
}
