package git;

import com.google.common.base.MoreObjects;

/**
 * Class contains information related to gitflow methodology
 *
 * @author @muratovv
 * @date 08.03.17
 */
public class GitFlowInfo {

    /**
     * Commit message
     */
    private String message;

    private GitFlowInfo(String commitMessage) {
        this.message = commitMessage;
    }

    public static GitFlowInfo parse(String commitMessage) {
        return new GitFlowInfo(commitMessage);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("message", message)
                .toString();
    }
}
