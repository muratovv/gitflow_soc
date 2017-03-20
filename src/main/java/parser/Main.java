package parser;

import com.google.common.base.Joiner;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import git.Commit;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author @muratovv
 * @date 20.02.17
 */
public class Main {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException, GitAPIException {
        final Config config         = ConfigFactory.load("run.conf");
        String       repositoryPath = config.getString("root.repo_path");
        logger.info("Target repo: {}", repositoryPath);
        Git git = Git.open(new File(repositoryPath));
        ParserFlow.init(git);
        List<Commit> commits = ParserFlow.getCommits(git);
        logger.info(Joiner.on('\n').join(commits));
    }

}
