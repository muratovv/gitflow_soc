package git;

import com.google.common.base.MoreObjects;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.jgit.diff.DiffEntry;

/**
 * @author @muratovv
 * @date 04.04.17
 */
public class CollapsedFileChange extends FileChange {

    private int numberOfCommits = 0;

    public CollapsedFileChange(String name, DiffEntry.ChangeType type, long insertions, long deletions,
                               int numberOfCommits) {
        super(name, type, insertions, deletions);
        this.numberOfCommits = numberOfCommits;
    }

    public CollapsedFileChange(MutableSet<String> aliases, long insertions, long deletions,
                               int numberOfCommits) {
        super(aliases, insertions, deletions);
        this.numberOfCommits = numberOfCommits;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(super.toString())
                .add("Commits", numberOfCommits)
                .toString();
    }

    public int collapsedCommits() {
        return numberOfCommits;
    }
}
