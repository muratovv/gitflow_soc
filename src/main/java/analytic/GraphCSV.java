package analytic;

import git.Author;
import util.Strings;

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

    public String makeCosineCSV() {
        StringBuilder builder = new StringBuilder();
        builder.append(makeHead());
        int size = analytic.getAuthors().size();
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                Author a             = analytic.getAuthors().get(i);
                Author b             = analytic.getAuthors().get(j);
                double cosineMeasure = analytic.cosine(a, b);
                if (filter.apply(cosineMeasure)) {
                    builder.append(String.format("%s%s %s%s %s\n",
                            generateName(a), separator, generateName(b), separator, cosineMeasure));
                }
            }
        }
        return builder.toString();
    }

    private static String generateName(Author author) {
        return Strings.wrapWithQuotes(author.name());
    }

    private static String makeHead() {
        return String.format("From%s To%s Weight\n", separator, separator);
    }

    public void setFilterFuntion(Filter filter) {
        this.filter = filter;
    }

    public interface Filter {
        boolean apply(Double value);
    }
}
