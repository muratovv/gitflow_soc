package parser;

import analytic.Analytic;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import git.Author;
import git.Commit;
import org.eclipse.collections.api.list.ImmutableList;
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
        //        logger.info(Joiner.on('\n').join(commits));

        Analytic              analytic = new Analytic(commits);
        ImmutableList<Author> authors  = analytic.getAuthors();
        logger.info(authors.toString());
        logger.info("*--------||--------*\n\n");

        for (int i = 0; i < authors.size(); i++) {
            for (int j = i; j < authors.size(); j++) {
                Author authorA = authors.get(i);
                Author authorB = authors.get(j);
                if (!authorA.equals(authorB)) {
                    logger.info(String.format(
                            "Cos(%s, %s) = %s",
                            authorA.name(), authorB.name(), analytic.cosine(authorA, authorB)));
                }

            }
        }


    }

}
