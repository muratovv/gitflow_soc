package parser;

import com.google.common.base.MoreObjects;
import org.eclipse.jgit.revwalk.RevCommit;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author @muratovv
 * @date 20.02.17
 */
public class Committed {
    String        author;
    LocalDateTime time;

    public Committed(RevCommit commit) {
        author = commit.getAuthorIdent().getName();
        long timeOfCommit = commit.getAuthorIdent().getWhen().getTime();
        this.time = getLocalDateTime(timeOfCommit);


    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("author", author)
                .add("time", time)
                .toString();
    }

    private static LocalDateTime getLocalDateTime(long timeOfCommit) {
        return Instant.ofEpochMilli(timeOfCommit).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
