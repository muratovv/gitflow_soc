package analytic;

import git.Author;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import util.Strings;

import java.util.Locale;

/**
 * @author @muratovv
 * @date 05.04.17
 */
public class GraphCSV {
    private Analytic analytic = null;
    private Filter   filter   = value -> value > 0;

    public static String separator = ";";

    public GraphCSV(Analytic analytic) {
        this.analytic = analytic;
    }

    public String makeCosineBlobCSV() {
        StringBuilder builder = new StringBuilder();
        builder.append(generateMetaInformation())
                .append(makeHead());
        getEdges().forEach(edge -> {
            String format = generateCSVrow(edge);
            builder.append(format);
        });
        return builder.toString();
    }

    private static String generateCSVrow(Edge<Author, Author, Double> edge) {
        String doubleInStrRepr = String.format(Locale.GERMAN, "%f", edge.tag);
        return String.format("%s%s %s%s %s\n",
                generateName(edge.source), separator, generateName(edge.dest), separator, doubleInStrRepr);
    }

    public ImmutableList<Edge<Author, Author, Double>> getEdges() {
        MutableList<Edge<Author, Author, Double>> edges = Lists.mutable.empty();
        int                                       size  = analytic.getAuthors().size();
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                Author a             = analytic.getAuthors().get(i);
                Author b             = analytic.getAuthors().get(j);
                double cosineMeasure = analytic.cosine(a, b);
                if (filter.apply(cosineMeasure)) {
                    edges.add(Edge.construct(a, b, cosineMeasure));
                }
            }
        }
        return edges.toImmutable();
    }

    private static String generateName(Author author) {
        return Strings.wrapWithQuotes(author.name());
    }

    private static String makeHead() {
        return String.format("From%s To%s Weight\n", separator, separator);
    }

    private static String generateComment(Object key, Object value) {
        return String.format(";%s => %s\n", key, value);
    }

    private String generateMetaInformation() {
        return generateComment("authors", analytic.getAuthors().size())
                + generateComment("commits", analytic.getAllCommits().size());
    }

    public void setFilterFuntion(Filter filter) {
        this.filter = filter;
    }

    public interface Filter {
        boolean apply(Double value);
    }

    /**
     * Data class for edge
     *
     * @param <S> source vertex
     * @param <D> dest vertex
     * @param <T> weight (or tag) of edge
     */
    public static class Edge<S, D, T> {
        S source;
        D dest;
        T tag;

        public S getSource() {
            return source;
        }

        public D getDest() {
            return dest;
        }

        public T getTag() {
            return tag;
        }

        public Edge(S source, D dest, T tag) {
            this.source = source;
            this.dest = dest;
            this.tag = tag;
        }

        public static <S, D, T> Edge<S, D, T> construct(S source, D dest, T tag) {
            return new Edge<>(source, dest, tag);
        }
    }
}
