package analytic;

import git.Author;
import git.Commit;
import git.FileChange;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;
import org.eclipse.collections.impl.factory.Maps;
import util.ListsTransforms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author @muratovv
 * @date 03.04.17
 */
public class Analytic {

    private List<Commit> allCommits = new ArrayList<>();

    public Analytic(List<Commit> allCommits) {
        this.allCommits = allCommits;
    }

    public MutableMap<Author, Collection<FileChange>> getCollapsedChangedByAuthor() {
        MutableMap<Author, Collection<FileChange>> destMap = Maps.mutable.empty();
        ImmutableListMultimap<Author, Collection<FileChange>> byAuthor = ListsTransforms
                .convert(allCommits)
                .groupBy(Commit::author)
                .collectValues(Commit::changes);

        for (Author author : byAuthor.keySet()) {
            ImmutableListMultimap<Collection<String>, FileChange> changes = byAuthor
                    .get(author)
                    .flatCollect(fileChanges -> fileChanges)
                    .groupBy(FileChange::aliases);
            List<FileChange> changesOfAuthor = new ArrayList<>();
            for (Collection<String> aliases : changes.keySet()) {
                ImmutableList<FileChange> fileChanges = changes.get(aliases);
                FileChange                collapsed   = FileChange.collapse(fileChanges);
                changesOfAuthor.add(collapsed);
            }
            destMap.put(author, changesOfAuthor);
        }
        return destMap;
    }
}
