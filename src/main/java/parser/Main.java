package parser;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author @muratovv
 * @date 20.02.17
 */
public class Main {
    public static void main(String[] args) throws GitAPIException, IOException {
        System.out.println("Hello world");
        Git                 git     = Git.open(new File("test_repo"));
        Iterable<RevCommit> commits = git.log().call();
        for (RevCommit commit : commits) {
            System.out.println(new Committed(commit));
        }

        ByteArrayOutputStream out       = new ByteArrayOutputStream();
        DiffFormatter         formatter = new DiffFormatter(out);
        formatter.setRepository(git.getRepository());
        Iterator<RevCommit> history = git.log().call().iterator();
        RevCommit           new_commit    = history.next();
        RevCommit           old_commit    = history.next();

        formatter.format(old_commit, new_commit);
        List<DiffEntry>     entries = formatter.scan(old_commit, new_commit);
        formatter.flush();
        System.out.println(out.toString());
        for (DiffEntry entry : entries) {
            

        }

    }
}
