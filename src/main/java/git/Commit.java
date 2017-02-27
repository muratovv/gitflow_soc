package git;

import org.eclipse.jgit.revwalk.RevCommit;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author @muratovv
 * @date 27.02.17
 */
public class Commit {
    private Author                 author;
    private Collection<FileChange> changes;
    private LocalDateTime          timeOfCommit;

    /**
     * Parse {@link RevCommit} into {@link Commit} structure
     *
     * @param current commit, fill general commit inforamtion
     * @param old     diff with {@code current} for extract differences
     *
     * @return filled {@link Commit} structure
     */
    public static Commit parse(RevCommit current, RevCommit old) {
        // TODO: implement parse
        throw new UnsupportedOperationException();
    }
}
