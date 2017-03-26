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

    /**
     * Default prefixes for finding
     */
    public static Map<String, CommitType> prefixes = new HashMap<String, CommitType>() {
        private Map<String, CommitType> inflateDefault() {
            put("release/", CommitType.RELEASE);
            put("hotfix/", CommitType.HOTFIX);
            put("feature/", CommitType.FEATURE);
            put("bugfix/", CommitType.BUGFIX);
            put("support/", CommitType.SUPPORT);
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
        parseMessage();
    }

    public static GitFlowInfo parse(String commitMessage) {

        try {
            return new GitFlowInfo(commitMessage);
        } catch (NotFoundGitflowException ex) {
            return null;
        }
    }

    private void parseMessage() {
        for (String s : prefixes.keySet()) {
            if (message.contains(s)) {
                type = prefixes.get(s);
                name = getName();
                if (name == null) throw new NotFoundGitflowException();
                return;
            }
        }
        throw new NotFoundGitflowException();
    }

    private String getName() {
        try {
            String[] split = message.split("/")[1].split("\'");
            return split[0].trim();
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("flow")
                .omitNullValues()
                .add("type", type)
                .add("name", name)
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

    public static class NotFoundGitflowException extends RuntimeException {

    }
}
