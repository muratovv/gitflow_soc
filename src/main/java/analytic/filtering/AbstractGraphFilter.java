package analytic.filtering;

import analytic.graphs.AuthorEdge;
import analytic.graphs.SilentAuthorGraphRepr;
import git.Author;
import org.eclipse.collections.api.set.ImmutableSet;
import org.jgrapht.graph.SimpleWeightedGraph;

/**
 * @author @muratovv
 * @date 07.04.17
 */
public abstract class AbstractGraphFilter extends DefaultEdgeFilter {

    protected SimpleWeightedGraph<Author, AuthorEdge> graph = null;

    public AbstractGraphFilter(ImmutableSet<AuthorEdge> edges) {
        super(edges);
        this.graph = makeGraph();
    }

    private SimpleWeightedGraph<Author, AuthorEdge> makeGraph() {
        SimpleWeightedGraph<Author, AuthorEdge> graph = new SilentAuthorGraphRepr<>(AuthorEdge.class);
        edges.forEach(edge -> graph.addEdge(edge.source(), edge.dest(), edge));
        return graph;
    }

    public SimpleWeightedGraph<Author, AuthorEdge> getGraph() {
        return graph;
    }
}
