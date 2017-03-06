package git;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;

import java.util.*;
import java.util.stream.Collectors;

import static util.Require.require;

/**
 * Class represents changes making in one file
 *
 * @author @muratovv
 * @date 27.02.17
 */
public class FileChange {

    /**
     * Aliases is name of file with renaming
     */
    private Set<String> aliases    = new HashSet<>();
    /**
     * Number of insertions on one change
     */
    private long        insertions = 0;
    /**
     * Number of deletions on one change
     */
    private long        deletions  = 0;

    public FileChange(String name, long insertions, long deletions) {
        setAlias(name);
        require(() -> insertions >= 0, "Insertions must be non negative");
        require(() -> deletions >= 0, "Deletions must be non negative");
        this.insertions = insertions;
        this.deletions = deletions;
    }

    /**
     * If file has two names (move or rename files) new name should be inserts with this method
     *
     * @param alias new name of file
     */
    public void setAlias(String alias) {
        aliases.add(alias);
    }

    /**
     * Parse raw input of diff tool
     *
     * @param name    of file
     * @param rawDiff raw text of diff tool output
     *
     * @return constructed {@link FileChange}
     */
    public static FileChange parseDiff(String name, String rawDiff) {
        InsertionDeletionPair numbers = parseRaw(rawDiff);
        return new FileChange(name, numbers.inserts, numbers.deletes);
    }

    private static InsertionDeletionPair parseRaw(String raw) {
        List<String> split = new ArrayList<>(Arrays.asList(raw.split("\n")));
        split = removeFirstFourLines(split);
        split = removeAtStartsLines(split);
        return countModifications(split);
    }

    private static List<String> removeFirstFourLines(List<String> split) {
        return split.stream().skip(4).collect(Collectors.toList());
    }

    private static List<String> removeAtStartsLines(List<String> split) {
        ArrayList<String> result = new ArrayList<>();
        for (String s : split) {
            if (!s.startsWith("@@")) result.add(s);
        }
        return result;
    }

    private static InsertionDeletionPair countModifications(List<String> split) {
        long inserts = 0;
        long deletes = 0;
        for (String s : split) {
            switch (s.charAt(0)) {
                case '-':
                    deletes++;
                    break;
                case '+':
                    inserts++;
                    break;
            }
        }
        return new InsertionDeletionPair(inserts, deletes);
    }

    public Collection<String> aliases() {
        return aliases;
    }

    public long insertions() {
        return insertions;
    }

    public long deletions() {
        return deletions;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("aliases", Joiner.on(',').join(aliases))
                .add("+", insertions)
                .add("-", deletions)
                .toString();
    }

    private static class InsertionDeletionPair {
        private long inserts;
        private long deletes;

        private InsertionDeletionPair(long inserts, long deletes) {
            this.inserts = inserts;
            this.deletes = deletes;
        }
    }
}
