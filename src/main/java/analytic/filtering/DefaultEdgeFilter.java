package analytic.filtering;

import analytic.graphs.AuthorEdge;
import org.eclipse.collections.api.set.ImmutableSet;

/**
 * @author @muratovv
 * @date 07.04.17
 */
public class DefaultEdgeFilter extends AbstractEdgeFilter {

    public DefaultEdgeFilter(ImmutableSet<AuthorEdge> edges) {
        super(edges);
    }

    @Override
    public boolean filter(AuthorEdge edge) {
        return edge != null;
    }

}
