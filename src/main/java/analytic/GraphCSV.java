package analytic;

import analytic.graphs.Edge;
import git.Author;
import util.Strings;

import java.util.Locale;

/**
 * @author @muratovv
 * @date 05.04.17
 */
public class GraphCSV {
    private Analytic analytic = null;

    public static String separator = ";";

    public GraphCSV(Analytic analytic) {
        this.analytic = analytic;
    }

    public String makeCosineBlobCSV() {
        StringBuilder builder = new StringBuilder();
        builder.append(generateMetaInformation())
                .append(makeHead());
        analytic.getEdges().forEach(edge -> {
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

    private static String generateComment(Object key, Object value) {
        return String.format(";%s => %s\n", key, value);
    }

    private String generateMetaInformation() {
        return generateComment("authors", analytic.getAuthors().size())
                + generateComment("commits", analytic.getAllCommits().size());
    }
}
