package git;

import com.google.common.base.MoreObjects;

import java.util.HashMap;
import java.util.Map;

/**
 * Class contains information related to gitflow methodology
 *
 * @author @muratovv
 * @date 08.03.17
 */
public class GitFlowInfo {

    public static Map<CommitType, String> prefixes = new HashMap<CommitType, String>() {
        private Map<CommitType, String> inflateDefault() {
            put(CommitType.RELEASE, "release/");
            put(CommitType.HOTFIX, "hotfix/");
            put(CommitType.FEATURE, "feature/");
            put(CommitType.BUGFIX, "bugfix/");
            put(CommitType.SUPPORT, "support/");
            return this;
        }
    }.inflateDefault();
    /**
     * Commit message
     */
    private String     message;
    /**
     * Type of gitflow commit
     */
    private CommitType type;
    /**
     * Name related to {@link CommitType}
     */
    private String     name;

    private GitFlowInfo(String commitMessage) {
        this.message = commitMessage;
    }

    public static GitFlowInfo parse(String commitMessage) {

        return new GitFlowInfo(commitMessage);
    }

    private void parseMessage() {

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("flow")
                .add("message", message)
                .toString();
    }

    public String message() {
        return message;
    }

    public enum CommitType {
        RELEASE,
        HOTFIX,
        FEATURE,
        BUGFIX,
        SUPPORT
    }
}
