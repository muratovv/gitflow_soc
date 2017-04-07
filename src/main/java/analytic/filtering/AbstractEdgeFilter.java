package analytic.filtering;

import analytic.graphs.AuthorEdge;
import org.eclipse.collections.api.set.ImmutableSet;

/**
 * @author @muratovv
 * @date 07.04.17
 */
public abstract class AbstractEdgeFilter {
    protected ImmutableSet<AuthorEdge> edges = null;

    public AbstractEdgeFilter(ImmutableSet<AuthorEdge> edges) {
        this.edges = edges;
    }

    public final ImmutableSet<AuthorEdge> edges() {
        return edges;
    }

    /**
     * Create filter with new filtered collection
     */
    public AbstractEdgeFilter collect() {
        return new DefaultEdgeFilter(edges.select(this::filter));
    }

    protected abstract boolean filter(AuthorEdge edge);
}
