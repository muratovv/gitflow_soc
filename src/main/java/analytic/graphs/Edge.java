package analytic.graphs;

/**
 * Data class for edge
 *
 * @param <V> type of vertex
 * @param <T> weight (or tag) of edge
 */
public class Edge<V, T> implements Cloneable {
    V source;
    V dest;
    T tag;

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
        try {
            E clone = ((E) edge.clone());
            clone.dest = edge.source();
            clone.source = edge.dest();
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
