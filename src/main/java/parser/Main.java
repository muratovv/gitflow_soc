package parser;

import analytic.Analytic;
import analytic.GraphCSV;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import git.Commit;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import util.FilesUtil;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        String       reportPath     = config.getString("root.report_folder");
        String       rootRepos      = config.getString("root.root_repos_path");
        if (rootRepos != null) {
            allRepositoriesFlow(Paths.get(rootRepos), Paths.get(reportPath, "inside"));
        } else {
            repositoryFlow(Paths.get(repositoryPath), Paths.get(reportPath));
        }

    }


    private static void allRepositoriesFlow(Path reposFolder, Path reportPath) throws IOException {
        DirectoryStream<Path> repos =
                Files.newDirectoryStream(reposFolder, file -> Files.isDirectory(file));
        repos.forEach(path -> {
            try {
                logger.info("=====||==========||==========||=====\n\n");
                repositoryFlow(path, reportPath);
                logger.info("\n\n");
            } catch (IOException | GitAPIException e) {
                logger.error("Error on path {}", path);
                logger.trace("Occurred exception", e);
            }
        });
    }

    private static void repositoryFlow(Path repoPath, Path reportPath)
            throws IOException, GitAPIException {
        String repoName = getRepoName(repoPath);

        List<Commit> commits = retrieveCommits(repoPath, repoName);

        Analytic analytic = computeAnalytic(commits);

        createReport(reportPath, repoName, analytic);
    }

    private static List<Commit> retrieveCommits(Path repoPath, String repoName)
            throws IOException, GitAPIException {
        logger.info("Open repository {}", repoName);
        Git git = Git.open(repoPath.toFile());
        ParserFlow.init(git);
        logger.info("Retrieve information...");
        List<Commit> commits = ParserFlow.getCommits(git);
        logger.info("Commits retrieved: {}", commits.size());
        return commits;
    }

    private static Analytic computeAnalytic(List<Commit> commits) {
        logger.info("Construct analytic model...");
        Analytic analytic = new Analytic(commits);
        logger.info("Authors in project: {}", analytic.getAuthors().size());
        return analytic;
    }

    private static void createReport(Path reportPath, String repoName, Analytic analytic) {
        logger.info("Write graph to csv");
        GraphCSV graphCSV = new GraphCSV(analytic);
        boolean written = FilesUtil.writeFile(
                reportPath.resolve(generateReportName(repoName)),
                graphCSV.makeCosineCSV());
        logger.info("Writing in file: {}", written ? "successful" : "failed");
    }

    /**
     * @return name of repository folder
     */
    private static String getRepoName(Path repoPath) {
        return repoPath.getName(repoPath.getNameCount() - 1).toString();
    }

    /**
     * Generate name of report file
     */
    private static String generateReportName(String repoName) {
        return repoName + ".csv";
    }

}
