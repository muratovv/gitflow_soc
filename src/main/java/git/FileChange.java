package git;

import java.util.Collection;

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
    private Collection<String> aliases;

    /**
     * Number of insertions on one change
     */
    private long insertions;

    /**
     * Number of deletions on one change
     */
    private long deletions;


    public static FileChange parseDiff(String diff) {
        // TODO: implement parseDiff
        throw new UnsupportedOperationException();
    }

}
