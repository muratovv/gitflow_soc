package analytic;

import analytic.graphs.AuthorEdge;
import analytic.graphs.Edge;
import git.Author;
import org.eclipse.collections.api.set.ImmutableSet;
import util.Strings;

import java.util.Locale;

/**
 * Class generate and write csv
 *
 * @author @muratovv
 * @date 05.04.17
 */
public class GraphReportCSV {

    private ImmutableSet<AuthorEdge> graph;

    public static String separator = ";";

    public GraphReportCSV(ImmutableSet<AuthorEdge> graph) {
        this.graph = graph;
    }

    public String makeCosineBlobCSV() {
        StringBuilder builder = new StringBuilder();
        builder.append(getInternalComments())
                .append(makeHead());
        graph.forEach(edge -> {
            String format = generateRowOfCSV(edge);
            builder.append(format);
        });
        return builder.toString();
    }

    private static String generateRowOfCSV(Edge<Author, Double> edge) {
        String doubleInStrRepr = String.format(Locale.GERMAN, "%f", edge.tag());
        return String.format("%s%s %s%s %s\n",
                generateName(edge.source()), separator, generateName(edge.dest()), separator, doubleInStrRepr);
    }

    private static String generateName(Author author) {
        return Strings.wrapWithQuotes(author.name());
    }

    private static String makeHead() {
        return String.format("From%s To%s Weight\n", separator, separator);
    }

    private String getInternalComments() {
        return generateComment("connections", graph.size());
    }

    private static String generateComment(Object key, Object value) {
        return String.format(";%s => %s\n", key, value);
    }

}
