package analytic.filtering;

import analytic.graphs.AuthorEdge;
import org.eclipse.collections.api.list.ImmutableList;

/**
 * @author @muratovv
 * @date 07.04.17
 */
public class DefaultEdgeFilter extends AbstractEdgeFilter {

    public DefaultEdgeFilter(ImmutableList<AuthorEdge> edges) {
        super(edges);
    }

    @Override
    public AbstractEdgeFilter filter() {
        return this;
    }
}
