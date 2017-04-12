package analytic.filtering;

import analytic.graphs.AuthorEdge;
import org.eclipse.collections.api.set.ImmutableSet;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.util.UnionFind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author @muratovv
 * @date 11.04.17
 */
public class SpanningTreeFilter extends AbstractGraphFilter {
    private static final Logger                                         logger       = LoggerFactory
            .getLogger(SpanningTreeFilter.class);
    private              SpanningTreeAlgorithm.SpanningTree<AuthorEdge> spanningTree = null;

    public SpanningTreeFilter(ImmutableSet<AuthorEdge> edges) {
        super(edges);
        this.spanningTree = makeSpanningTree();
    }

    private SpanningTreeAlgorithm.SpanningTree<AuthorEdge> makeSpanningTree() {
        SpanningTreeAlgorithm.SpanningTree<AuthorEdge> spanningTree = new MaxSpanningTree<>(graph).getSpanningTree();
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

    private static class MaxSpanningTree<V, E> implements SpanningTreeAlgorithm<E> {

        private final Graph<V, E> graph;

        public MaxSpanningTree(Graph<V, E> graph) {

            this.graph = graph;
        }

        @Override
        public SpanningTreeAlgorithm.SpanningTree<E> getSpanningTree() {
            UnionFind<V> forest   = new UnionFind<>(this.graph.vertexSet());
            ArrayList<E> allEdges = new ArrayList<>(this.graph.edgeSet());
            allEdges.sort(Comparator.comparing(edge2 -> Double.valueOf(this.graph.getEdgeWeight(edge2))));
            Collections.reverse(allEdges);
            double   spanningTreeCost = 0.0D;
            Set<E>   edgeList         = new HashSet();
            Iterator var6             = allEdges.iterator();

            while (var6.hasNext()) {
                E edge   = ((E) var6.next());
                V source = this.graph.getEdgeSource(edge);
                V target = this.graph.getEdgeTarget(edge);
                if (!forest.find(source).equals(forest.find(target))) {
                    forest.union(source, target);
                    edgeList.add(edge);
                    spanningTreeCost += this.graph.getEdgeWeight(edge);
                }
            }

            return new SpanningTreeAlgorithm.SpanningTreeImpl(edgeList, spanningTreeCost);
        }
    }

}
