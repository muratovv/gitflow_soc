package git;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

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

    public List<DiffEntry> makeDiff(RevCommit current, RevCommit old) throws IOException {
        List<DiffEntry> scanned = formatter.scan(old, current);
        return scanned;
    }

}
