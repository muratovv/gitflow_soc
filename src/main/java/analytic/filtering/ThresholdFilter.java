package analytic.filtering;

import analytic.graphs.AuthorEdge;
import org.eclipse.collections.api.set.ImmutableSet;

/**
 * Filter getInitialGraph by threshold
 *
 * @author @muratovv
 * @date 07.04.17
 */
public class ThresholdFilter extends DefaultEdgeFilter {

    private double threshold = 0;

    public ThresholdFilter(ImmutableSet<AuthorEdge> edges, double threshold) {
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
