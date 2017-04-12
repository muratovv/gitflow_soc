package analytic.filtering;

import analytic.graphs.AuthorEdge;
import analytic.graphs.Edge;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.set.ImmutableSet;

import java.util.Comparator;

/**
 * @author @muratovv
 * @date 11.04.17
 */
public class TopWeightedFilter extends AbstractGraphFilter {

    private double                    ratioEdgesToNode = 0;
    private ThresholdFilter           thresholdFilter  = null;
    private ImmutableList<AuthorEdge> sortedEdges      = null;

    public TopWeightedFilter(ImmutableSet<AuthorEdge> graph, double ratioEdgesToNode) {
        super(graph);
        sortedEdges = makeSortedListOfEdges();
        setRatioEdgesToNode(ratioEdgesToNode);
    }

    public TopWeightedFilter(ImmutableSet<AuthorEdge> edges) {
        this(edges, 1);
    }

    public void setRatioEdgesToNode(double ratioEdgesToNode) {
        this.ratioEdgesToNode = ratioEdgesToNode;
        thresholdFilter = new ThresholdFilter(edges, generateThreshold());
    }

    private double generateThreshold() {
        return getStatistics(getNumberOfEdges());
    }

    private int getNumberOfEdges() {
        return ((int) (ratioEdgesToNode * getGraph().vertexSet().size()));
    }

    private double getStatistics(int position) {
        if (position < 0 || position >= sortedEdges.size()) return 0.0;
        return sortedEdges.get(position).tag();
    }

    @Override
    public boolean filter(AuthorEdge edge) {
        return thresholdFilter.filter(edge);
    }

    private ImmutableList<AuthorEdge> makeSortedListOfEdges() {
        return edges.toSortedList(Comparator.comparingDouble(Edge::tag)).reverseThis().toImmutable();
    }
}
