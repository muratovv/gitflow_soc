package git;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static util.Require.require;

/**
 * @author @muratovv
 * @date 27.02.17
 */
public class Differ {
    /**
     * Singleton {@link Differ}
     */
    private static Differ        differ;
    private        DiffFormatter formatter;
    private ByteArrayOutputStream stream = new ByteArrayOutputStream();

    public Differ(Repository repository) {
        formatter = new DiffFormatter(stream);
        formatter.setRepository(repository);
    }

    /**
     * @return {@link Differ} singleton instance
     */
    public static Differ singleton() {
        if (differ == null)
            throw new NullPointerException("differ is null");
        return differ;
    }

    /**
     * Init instance with {@code repository}
     *
     * @param repository JGit repository
     */
    public static void init(Repository repository) {
        differ = new Differ(repository);
    }

    /**
     * Get string representation of {@link DiffEntry} object
     *
     * @param entry source object
     *
     * @return string representation
     *
     * @throws IOException
     */
    public String getRawDiffEntry(DiffEntry entry) throws IOException {
        stream.reset();
        formatter.format(entry);
        String raw = stream.toString();
        stream.reset();
        return raw;
    }

    /**
     * Retrieve differences from two near commits
     *
     * @param current commit for retrieve
     * @param prev    previous commit
     *
     * @return list of differences
     *
     * @throws IOException
     */
    public List<DiffEntry> makeDiff(RevCommit current, RevCommit prev) throws IOException {
        require(() -> current.getAuthorIdent().getWhen().after(prev.getAuthorIdent().getWhen()),
                String.format("Commits must be in strong order: current(%s) <= prev(%s)",
                        current.getFullMessage(), prev.getFullMessage()));
        List<DiffEntry> scanned = formatter.scan(prev, current);
        return scanned;
    }

}
