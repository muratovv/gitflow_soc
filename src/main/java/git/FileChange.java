package git;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.jgit.diff.DiffEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
    private MutableSet<String> aliases = Sets.mutable.empty();
    /**
     * Type of changing file, such as modification, deletion, and so on
     */
    private DiffEntry.ChangeType changeType;
    /**
     * Number of insertions on one change
     */
    private long insertions = 0;
    /**
     * Number of deletions on one change
     */
    private long deletions  = 0;

    public FileChange(String name, DiffEntry.ChangeType type, long insertions, long deletions) {
        setAlias(name);
        this.changeType = type;
        require(() -> insertions >= 0, "Insertions must be non negative");
        require(() -> deletions >= 0, "Deletions must be non negative");
        this.insertions = insertions;
        this.deletions = deletions;
    }

    /**
     * Protected constructor for {@link FileChange#collapse(FileChange, FileChange)} method
     */
    protected FileChange(MutableSet<String> aliases, long insertions, long deletions) {
        this.aliases = aliases;
        this.changeType = DiffEntry.ChangeType.MODIFY;
        require(() -> insertions >= 0, "Insertions must be non negative");
        require(() -> deletions >= 0, "Deletions must be non negative");
        this.insertions = insertions;
        this.deletions = deletions;

    }

    /**
     * Merge collection of {@link FileChange}
     */
    public static CollapsedFileChange collapse(Iterable<FileChange> changes) {
        MutableSet<String> aliases    = Sets.mutable.empty();
        int                insertions = 0;
        int                deletions  = 0;
        int                commits    = 0;
        for (FileChange change : changes) {
            aliases = aliases.union(change.aliases);
            insertions += change.insertions;
            deletions += change.deletions;
            commits++;
        }
        return new CollapsedFileChange(aliases, insertions, deletions, commits);
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
    public static FileChange parseDiff(String name, DiffEntry.ChangeType type, String rawDiff) {
        InsertionDeletionPair numbers = parseRaw(rawDiff);
        return new FileChange(name, type, numbers.inserts, numbers.deletes);
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
        MoreObjects.ToStringHelper aliases = MoreObjects.toStringHelper("Diff")
                .omitNullValues()
                .add("aliases", Joiner.on(',').join(this.aliases))
                .addValue(String.format("[%s]", changeType.toString().substring(0, 3)));

        if (insertions > 0)
            aliases.add("+", insertions);
        if (deletions > 0)
            aliases.add("-", deletions);

        return aliases.toString();
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
