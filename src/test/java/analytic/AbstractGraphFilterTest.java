package analytic;

import analytic.filtering.SpanningTreeFilter;
import analytic.graphs.AuthorEdge;
import analytic.graphs.Edge;
import git.Author;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.factory.Sets;
import org.junit.Test;

import static analytic.graphs.AuthorEdge.make;
import static org.junit.Assert.*;

/**
 * @author @muratovv
 * @date 07.04.17
 */
public class AbstractGraphFilterTest {
    @Test
    public void spanningTreeTest() throws Exception {
        Author                   V1     = Author.make("a", "a");
        Author                   V2     = Author.make("b", "b");
        Author                   V3     = Author.make("c", "c");
        Author                   V4     = Author.make("d", "d");
        AuthorEdge               E12    = make(V1, V2, 1.);
        AuthorEdge               E23    = make(V2, V3, 2.);
        AuthorEdge               E34    = make(V3, V4, 3.);
        AuthorEdge               E41    = make(V1, V4, 4.);
        ImmutableSet<AuthorEdge> edges  = Sets.immutable.of(E12, E23, E34, E41);
        SpanningTreeFilter       filter = new SpanningTreeFilter(edges);


        assertFalse(filter.inSpanningTree(E41));
        assertFalse("Check swapped edge", filter.inSpanningTree(Edge.swap(E41)));

        assertTrue(filter.inSpanningTree(E12));
        assertTrue("Check swapped edge", filter.inSpanningTree(E12));
        assertTrue(filter.inSpanningTree(E23));
        assertTrue(filter.inSpanningTree(E34));

        assertEquals(3, filter.apply().size());


    }

}