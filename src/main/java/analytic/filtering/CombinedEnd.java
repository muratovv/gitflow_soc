package analytic.filtering;

import analytic.graphs.AuthorEdge;
import org.eclipse.collections.api.set.ImmutableSet;

/**
 * @author @muratovv
 * @date 07.04.17
 */
public class CombinedEnd extends CombinedFilter {
    public CombinedEnd(ImmutableSet<AuthorEdge> edges, AbstractEdgeFilter... filters) {
        super(edges, filters);
    }

    @Override
    protected boolean filter(AuthorEdge edge) {
        for (AbstractEdgeFilter f : internalFilters) {
            if (!f.filter(edge)) return false;
        }
        return true;
    }
}
