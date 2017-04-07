package analytic.filtering;

import analytic.graphs.AuthorEdge;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.factory.Lists;

/**
 * @author @muratovv
 * @date 07.04.17
 */
public abstract class CombinedFilter extends AbstractEdgeFilter {
    protected ImmutableList<AbstractEdgeFilter> internalFilters = null;

    public CombinedFilter(ImmutableSet<AuthorEdge> edges, AbstractEdgeFilter... filters) {
        super(edges);
        internalFilters = Lists.immutable.of(filters);
    }
}
