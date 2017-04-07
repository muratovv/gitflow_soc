package analytic.filtering;

import analytic.graphs.AuthorEdge;
import org.eclipse.collections.api.list.ImmutableList;

/**
 * @author @muratovv
 * @date 07.04.17
 */
public abstract class AbstractEdgeFilter {
    protected ImmutableList<AuthorEdge> edges = null;

    public AbstractEdgeFilter(ImmutableList<AuthorEdge> edges) {
        this.edges = edges;
    }

    public final ImmutableList<AuthorEdge> edges() {
        return edges;
    }

    /**
     * Create filter with new filtered collection
     */
    public abstract AbstractEdgeFilter filter();
}
