package analytic.filtering;

import analytic.graphs.AuthorEdge;
import org.eclipse.collections.api.set.ImmutableSet;

/**
 * Filter edges by threshold
 *
 * @author @muratovv
 * @date 07.04.17
 */
public class ThreshloldFilter extends DefaultEdgeFilter {

    private double threshold = 0;

    public ThreshloldFilter(ImmutableSet<AuthorEdge> edges, double threshold) {
        super(edges);
        this.threshold = threshold;
    }

    public void setThreshlold(double d) {
        this.threshold = d;
    }

    @Override
    public boolean filter(AuthorEdge edge) {
        return super.filter(edge) && edge.tag() > threshold;
    }
}
