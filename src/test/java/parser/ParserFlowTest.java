package parser;

import com.google.common.base.Joiner;
import git.Commit;
import org.eclipse.jgit.api.Git;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * @author @muratovv
 * @date 06.03.17
 */
public class ParserFlowTest {
    @Test
    public void getCommits() throws Exception {
        Git git = Git.open(new File("test_repo"));
        ParserFlow.init(git);
        List<Commit> commits = ParserFlow.getCommits(git);
        System.out.println(Joiner.on('\n').join(commits));
    }
}