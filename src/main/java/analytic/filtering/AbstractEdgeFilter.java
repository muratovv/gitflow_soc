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

    public final ImmutableSet<AuthorEdge> getInitialGraph() {
        return edges;
    }


    protected abstract boolean filter(AuthorEdge edge);

    public ImmutableSet<AuthorEdge> apply() {
        MutableSet<AuthorEdge> filtered = Sets.mutable.empty();
        for (AuthorEdge edge : edges) {
            if (filter(edge)) filtered.add(edge);
        }
        return filtered.toImmutable();
    }
}
