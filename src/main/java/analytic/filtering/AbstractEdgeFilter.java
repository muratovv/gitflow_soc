package analytic.filtering;

import analytic.graphs.AuthorEdge;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Sets;

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

    public static AbstractEdgeFilter combineFilters(ImmutableSet<AuthorEdge> targetCollection,
                                                    AbstractEdgeFilter... filters) {
        MutableSet<AuthorEdge> newEdges = Sets.mutable.empty();
        for (AuthorEdge edge : targetCollection) {
            boolean getEdge = true;
            for (AbstractEdgeFilter filter : filters) {
                if (!filter.filter(edge)) {
                    getEdge = false;
                    break;
                }
            }
            if (getEdge) newEdges.add(edge);
        }
        return new DefaultEdgeFilter(newEdges.toImmutable());
    }
}
