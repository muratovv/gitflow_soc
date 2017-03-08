package git;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import javafx.util.Pair;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static util.Strings.wrapWithQuotes;

/**
 * Class represents one commit in repository
 *
 * @author @muratovv
 * @date 27.02.17
 */
public class Commit implements Comparable<Commit> {
    /**
     * Author of commit
     */
    private Author                 author;
    /**
     * Changes happen in commit
     */
    private Collection<FileChange> changes;
    /**
     * Time of commit
     */
    private LocalDateTime          timeOfCommit;

    /**
     * Gitflow information related to commit
     */
    private GitFlowInfo info;

    private Commit(Author author, LocalDateTime timeOfCommit, Collection<FileChange> changes, GitFlowInfo info) {
        this.author = author;
        this.timeOfCommit = timeOfCommit;
        this.changes = changes;
        this.info = info;
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
        return new Commit(author, time, changes, GitFlowInfo.parse(current.getFullMessage()));
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
            String               raw   = Differ.singleton().getRawDiffEntry(entry);
            Pair<String, String> names = getFileNames(entry);

            FileChange fileDiff = FileChange.parseDiff(names.getValue(), entry.getChangeType(), raw);

            if (names.getKey() != null) {
                fileDiff.setAlias(names.getKey());
            }
            result.add(fileDiff);
        }

        return result;
    }

    /**
     * Retrieve information about path of file
     *
     * @param entry target object for retrieve path
     *
     * @return (old_path, new_path); also old_path may be null
     */
    private static Pair<String, String> getFileNames(DiffEntry entry) {
        String               newName    = null;
        String               oldName    = null;
        DiffEntry.ChangeType changeType = entry.getChangeType();
        switch (changeType) {
            case ADD:
                newName = entry.getNewPath();
                break;
            case MODIFY:
                newName = entry.getNewPath();
                break;
            case DELETE:
                newName = entry.getOldPath();
                break;
            case RENAME:
                newName = entry.getNewPath();
                oldName = entry.getOldPath();
                break;
            case COPY:
                newName = entry.getNewPath();
                break;
        }
        return new Pair<>(oldName, newName);
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

    public Author author() {
        return author;
    }

    public Collection<FileChange> changes() {
        return changes;
    }

    public LocalDateTime timeOfCommit() {
        return timeOfCommit;
    }

    public GitFlowInfo info() {
        return info;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("author", wrapWithQuotes(author.name()))
                .add("time", timeOfCommit)
                .add("msg", wrapWithQuotes(info.message()))
                .addValue(Joiner.on(',').join(changes))
                .toString();
    }

}
