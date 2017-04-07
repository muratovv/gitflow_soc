package analytic.filtering;

import analytic.graphs.AuthorEdge;
import analytic.graphs.SilentAuthorGraphRepr;
import git.Author;
import org.eclipse.collections.api.set.ImmutableSet;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.graph.SimpleWeightedGraph;

/**
 * @author @muratovv
 * @date 07.04.17
 */
public class GraphReprFiltering extends DefaultEdgeFilter {


    private SimpleWeightedGraph<Author, AuthorEdge>        graph        = null;
    private SpanningTreeAlgorithm.SpanningTree<AuthorEdge> spanningTree = null;

    public GraphReprFiltering(ImmutableSet<AuthorEdge> edges) {
        super(edges);
        this.graph = makeGraph();
        this.spanningTree = makeSpanningTree();
    }

    @Override
    public boolean filter(AuthorEdge edge) {
        return super.filter(edge) && inSpanningTree(edge);
    }

    private SimpleWeightedGraph<Author, AuthorEdge> makeGraph() {
        SimpleWeightedGraph<Author, AuthorEdge> graph = new SilentAuthorGraphRepr<>(AuthorEdge.class);
        edges.forEach(edge -> graph.addEdge(edge.source(), edge.dest(), edge));
        return graph;
    }

    private SpanningTreeAlgorithm.SpanningTree<AuthorEdge> makeSpanningTree() {
        KruskalMinimumSpanningTree<Author, AuthorEdge> treeWrapper = new KruskalMinimumSpanningTree<>(graph);
        return treeWrapper.getSpanningTree();
    }

    public boolean inSpanningTree(AuthorEdge edge) {
        return spanningTree.getEdges().contains(edge);
    }

}