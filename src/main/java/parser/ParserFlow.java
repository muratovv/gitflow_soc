package parser;

import git.Commit;
import git.Differ;
import javafx.util.Pair;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import util.Lists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main class for streaming parsing of repository
 *
 * @author @muratovv
 * @date 27.02.17
 */
public class ParserFlow {

    /**
     * Init module for start working
     *
     * @throws GitAPIException
     */
    public static void init(Git git) throws GitAPIException {
        Differ.init(git.getRepository());
    }


    /**
     * Parse current git brunch and make collection of {@link Commit}
     *
     * @param git repository for retrieve
     *
     * @return parsed commits
     *
     * @throws GitAPIException
     */
    public static ArrayList<Commit> getCommits(Git git) throws GitAPIException {
        List<RevCommit>                  revCommits      = retrieveRevisions(git);
        List<Pair<RevCommit, RevCommit>> revisionsByPair = Lists.zipOverlappedPairs(revCommits);
        ArrayList<Commit> commits = revisionsByPair.stream().map(p -> {
            try {
                return Commit.parse(p.getKey(), p.getValue());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toCollection(ArrayList::new));
        return commits;
    }

    /**
     * @param git root object of JGit
     *
     * @return commits starts from current HEAD to initial commit
     * <p>
     * Note: commits are reversed relatively history
     *
     * @throws GitAPIException
     */
    private static List<RevCommit> retrieveRevisions(Git git) throws GitAPIException {
        Iterator<RevCommit> source = git.log().call().iterator();
        List<RevCommit>     target = new ArrayList<>();
        source.forEachRemaining(target::add);
        return target;
    }
}
