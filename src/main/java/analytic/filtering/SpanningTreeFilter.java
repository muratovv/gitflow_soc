package analytic.filtering;

import analytic.graphs.AuthorEdge;
import analytic.graphs.MaxSpanningTree;
import org.eclipse.collections.api.set.ImmutableSet;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author @muratovv
 * @date 11.04.17
 */
public class SpanningTreeFilter extends AbstractGraphFilter {
    private static final Logger                                         logger       =
            LoggerFactory.getLogger(SpanningTreeFilter.class);
    private              SpanningTreeAlgorithm.SpanningTree<AuthorEdge> spanningTree = null;

    public SpanningTreeFilter(ImmutableSet<AuthorEdge> edges) {
        super(edges);
        this.spanningTree = makeSpanningTree();
    }

    private SpanningTreeAlgorithm.SpanningTree<AuthorEdge> makeSpanningTree() {
        SpanningTreeAlgorithm.SpanningTree<AuthorEdge> spanningTree =
                new MaxSpanningTree<>(graph).getSpanningTree();
        logger.info("Spanning tree weight {}", spanningTree.getWeight());
        return spanningTree;
    }

    public boolean inSpanningTree(AuthorEdge edge) {
        return spanningTree.getEdges().contains(edge);
    }

    @Override
    public boolean filter(AuthorEdge edge) {
        return super.filter(edge) && inSpanningTree(edge);
    }

}
