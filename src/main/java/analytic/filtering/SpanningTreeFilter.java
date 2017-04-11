package analytic.filtering;

import analytic.graphs.AuthorEdge;
import git.Author;
import org.eclipse.collections.api.set.ImmutableSet;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;

/**
 * @author @muratovv
 * @date 11.04.17
 */
public class SpanningTreeFilter extends AbstractGraphFilter {
    private SpanningTreeAlgorithm.SpanningTree<AuthorEdge> spanningTree = null;

    public SpanningTreeFilter(ImmutableSet<AuthorEdge> edges) {
        super(edges);
        this.spanningTree = makeSpanningTree();
    }

    private SpanningTreeAlgorithm.SpanningTree<AuthorEdge> makeSpanningTree() {
        KruskalMinimumSpanningTree<Author, AuthorEdge> treeWrapper = new KruskalMinimumSpanningTree<>(graph);
        return treeWrapper.getSpanningTree();
    }

    public boolean inSpanningTree(AuthorEdge edge) {
        return spanningTree.getEdges().contains(edge);
    }

    @Override
    public boolean filter(AuthorEdge edge) {
        return super.filter(edge) && inSpanningTree(edge);
    }

}
