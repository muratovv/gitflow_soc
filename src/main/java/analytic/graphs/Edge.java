package analytic.graphs;

import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * Data class for edge
 *
 * @param <V> type of vertex
 * @param <T> weight (or tag) of edge
 */
public class Edge<V, T> extends DefaultWeightedEdge implements Cloneable {
    protected V source;
    protected V dest;
    protected T tag;

    public V source() {
        return source;
    }

    public V dest() {
        return dest;
    }

    public T tag() {
        return tag;
    }

    public Edge(V source, V dest, T tag) {
        this.source = source;
        this.dest = dest;
        this.tag = tag;
    }

    public static <V, T> Edge<V, T> make(V source, V dest, T tag) {
        return new Edge<>(source, dest, tag);
    }

    public static <E extends Edge<V, T>, V, T> E swap(E edge) {
        E clone = ((E) edge.clone());
        clone.dest = edge.source();
        clone.source = edge.dest();
        return clone;
    }
}
