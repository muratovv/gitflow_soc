package analytic.graphs;

import git.Author;

/**
 * Special case of {@link Edge} aimed on author graph
 *
 * @author @muratovv
 * @date 07.04.17
 */
public class AuthorEdge extends Edge<Author, Double> {

    public AuthorEdge(Author source, Author dest, Double tag) {
        super(source, dest, tag);
    }

    @Override
    protected double getWeight() {
        return tag();
    }

    public static AuthorEdge make(Author source, Author dest, Double tag) {
        return new AuthorEdge(source, dest, tag);
    }
}
