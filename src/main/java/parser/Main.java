package parser;

import analytic.Analytic;
import analytic.GraphReportCSV;
import analytic.filtering.*;
import analytic.graphs.AuthorEdge;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import git.Commit;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import util.FilesUtil;
import util.ListsTransforms;

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


        ImmutableList<Commit> immutableCommits = ListsTransforms
                .convert(retrieveCommits(repoPath, repoName));

        commitsFlow(immutableCommits, repoName, reportPath.resolve("normal"));
        commitsFlow(immutableCommits.select(commit -> commit.info() != null), repoName, reportPath.resolve("gitflow"));
    }

    private static void commitsFlow(ImmutableList<Commit> commits, String repoName, Path reportPath) {
        logger.info("Commits for work {}", commits.size());
        Analytic analytic = computeAnalytic(commits);
        ImmutableSet<AuthorEdge> graph = applyFilter(new ThresholdFilter(analytic.getFullGraph(),
                0));

        ImmutableSet<AuthorEdge> baseline      = applyFilter(new DefaultEdgeFilter(graph));
        ImmutableSet<AuthorEdge> spanningTree  = applyFilter(spanninTreeFilter(graph));
        ImmutableSet<AuthorEdge> autoThreshold = applyFilter(autoDetectionFilter(graph));
        logger.info(String.format("Edges - baseline %s vs spanning tree %s vs auto %s", baseline.size(),
                spanningTree.size(), autoThreshold.size()));

        createReport(reportPath, repoName + "-baseline", baseline);
        createReport(reportPath, repoName + "-spanningTree", spanningTree);
        createReport(reportPath, repoName + "-autoDetection", autoThreshold);
    }

    private static ImmutableSet<AuthorEdge> applyFilter(AbstractEdgeFilter filter) {
        return filter.apply();
    }

    private static List<Commit> retrieveCommits(Path repoPath, String repoName)
            throws IOException, GitAPIException {
        logger.info("Open repository {}", repoName);
        Git git = Git.open(repoPath.toFile());
        ParserFlow.init(git);
        logger.info("Retrieve information...");
        return ParserFlow.getCommits(git);
    }

    private static Analytic computeAnalytic(ImmutableList<Commit> commits) {
        logger.info("Construct analytic model...");
        Analytic analytic = new Analytic(commits);
        logger.info("Authors in project: {}", analytic.getAuthors().size());
        return analytic;
    }

    private static void createReport(Path reportPath, String repoName, ImmutableSet<AuthorEdge> graph) {
        GraphReportCSV graphReportCSV = new GraphReportCSV(graph);
        boolean written = FilesUtil.writeFile(
                reportPath.resolve(generateReportName(repoName)),
                graphReportCSV.makeCosineBlobCSV());
        logger.info("Writing in {}: {}", repoName, written ? "successful" : "failed");
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

    private static AbstractEdgeFilter spanninTreeFilter(ImmutableSet<AuthorEdge> graph) {
        return new SpanningTreeFilter(graph);
    }

    private static AbstractEdgeFilter autoDetectionFilter(ImmutableSet<AuthorEdge> graph) {
        return new Or(graph, new SpanningTreeFilter(graph), new TopWeightedFilter(graph));
    }
}
