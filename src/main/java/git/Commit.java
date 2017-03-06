package git;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author @muratovv
 * @date 27.02.17
 */
public class Commit implements Comparable<Commit> {
    private Author                 author;
    private Collection<FileChange> changes;
    private LocalDateTime          timeOfCommit;

    public Commit(Author author, LocalDateTime timeOfCommit, Collection<FileChange> changes) {
        this.author = author;
        this.timeOfCommit = timeOfCommit;
        this.changes = changes;
    }

    /**
     * Parse {@link RevCommit} into {@link Commit} structure
     * <p>
     * Used default {@link Differ}
     *
     * @param current commit, fill general commit inforamtion
     * @param old     diff with {@code current} for extract differences
     *
     * @return filled {@link Commit} structure
     */
    public static Commit parse(RevCommit current, RevCommit old) throws IOException {
        Author                 author  = Author.parse(current.getAuthorIdent());
        LocalDateTime          time    = retrieveTimeOfCommit(current);
        Collection<FileChange> changes = retrieveChanges(current, old);
        return new Commit(author, time, changes);
    }

    /**
     * Retrieve dataTime from {@code commit} via {@code commit.getAuthorIdent()}
     */
    private static LocalDateTime retrieveTimeOfCommit(RevCommit commit) {
        long   timeInMillis = commit.getAuthorIdent().getWhen().getTime();
        ZoneId timeZone     = commit.getAuthorIdent().getTimeZone().toZoneId();
        return Instant.ofEpochMilli(timeInMillis).atZone(timeZone).toLocalDateTime();
    }

    private static Collection<FileChange> retrieveChanges(RevCommit current, RevCommit old) throws IOException {
        ArrayList<FileChange> result = new ArrayList<>();

        List<DiffEntry> diffEntries = Differ.singleton().makeDiff(current, old);
        for (DiffEntry entry : diffEntries) {
            String raw = Differ.singleton().getRawDiffEntry(entry);

            FileChange fileDiff = FileChange.parseDiff(entry.getNewPath(), entry.getChangeType(), raw);
            if (entry.getChangeType() == DiffEntry.ChangeType.RENAME) {
                fileDiff.setAlias(entry.getOldPath());
            }
            result.add(fileDiff);
        }

        return result;
    }

    /**
     * Compare via {@link LocalDateTime} fields
     *
     * @param right commit for comparing
     *
     * @return
     */
    @Override
    public int compareTo(Commit right) {
        return this.timeOfCommit.compareTo(
                right.timeOfCommit);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("author", author.name())
                .add("timeOfCommit", timeOfCommit)
                .addValue(Joiner.on(',').join(changes))
                .toString();
    }
}
