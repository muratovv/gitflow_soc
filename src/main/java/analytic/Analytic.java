package analytic;


import git.Author;
import git.CollapsedFileChange;
import git.Commit;
import git.FileChange;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.factory.Lists;
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

    /**
     * Source commits data for analysis
     */
    private ImmutableList<Commit> allCommits = null;

    /**
     * Collapsed Commits by author
     */
    private ImmutableMap<Author, ImmutableMap<String, CollapsedFileChange>> commits = null;

    /**
     * TF measure coefficient attached to all files in commits
     */
    private ImmutableMap<String, Double> commitCoefficient = null;

    /**
     * All files changed in source data
     */
    private ImmutableSet<String> files = null;

    public Analytic(List<Commit> allCommits) {
        this.allCommits = ListsTransforms.convert(allCommits);
        inflateStructures();
    }

    /**
     * Inflate all structures for compute cosine measure
     */
    private void inflateStructures() {
        files = inflateFiles();
        commits = inflateCommits();
        commitCoefficient = inflateCoefficient();
    }

    /**
     * Create a map with {@link CollapsedFileChange}s of all commits of {@link Author}
     */
    private ImmutableMap<Author, Collection<CollapsedFileChange>> getCollapsedChangedByAuthor() {
        MutableMap<Author, Collection<CollapsedFileChange>> destMap = Maps.mutable.empty();
        ImmutableListMultimap<Author, Collection<FileChange>> byAuthor =
                allCommits
                .groupBy(Commit::author)
                .collectValues(Commit::changes);

        for (Author author : byAuthor.keySet()) {
            ImmutableListMultimap<Collection<String>, FileChange> changes = byAuthor
                    .get(author)
                    .flatCollect(fileChanges -> fileChanges)
                    .groupBy(FileChange::aliases);
            List<CollapsedFileChange> changesOfAuthor = new ArrayList<>();
            for (Collection<String> aliases : changes.keySet()) {
                ImmutableList<FileChange> fileChanges = changes.get(aliases);
                CollapsedFileChange       collapsed   = FileChange.collapse(fileChanges);
                changesOfAuthor.add(collapsed);
            }
            destMap.put(author, changesOfAuthor);
        }
        return destMap.toImmutable();
    }


    /**
     * Structure is separations by author, where to him attached all changes, by file.
     *
     * @return Method inflate structure {@link Author} => {File path => {@link CollapsedFileChange}}
     */
    private ImmutableMap<Author, ImmutableMap<String, CollapsedFileChange>> inflateCommits() {
        return getCollapsedChangedByAuthor()
                .collectValues((author, collapsedFileChanges) ->
                        ListsTransforms.convert(new ArrayList<>(collapsedFileChanges))
                                .groupByUniqueKey(change ->
                                        change.aliases().iterator().next())); // TODO care about only one alias

    }

    /**
     * TF measure {@link Double} - number of all {@link Author}s
     * divide by number of authors commit to those file
     *
     * @return Method inflate structure File_path => TF measure
     */
    private ImmutableMap<String, Double> inflateCoefficient() {
        int N = commits.size();
        return files.groupByUniqueKey(__ -> __)
                .collectValues((s, __) -> {
                    int freq = howMuchCommitIn(s);
                    if (freq == 0) freq = N;
                    return Math.log(N / freq);
                });
    }

    private int howMuchCommitIn(String file) {
        final int[] counter = {0};
        commits.forEachValue(collapsedFileChanges -> {
            if (collapsedFileChanges.get(file) != null) counter[0]++;
        });
        return counter[0];
    }

    /**
     * @return method inflate set with all files, that changes in source data
     */
    private ImmutableSet<String> inflateFiles() {
        return allCommits
                .collect(Commit::changes)
                .flatCollect(__ -> __)
                .collect(FileChange::aliases)
                .flatCollect(aliases -> aliases)
                .toSet()
                .toImmutable();
    }

    /**
     * Cosine measure of authors. Weight of author based on number of commits with some file
     *
     * @return double in range [0, 1]. 1 if totally same, 0 if they don't have similarities.
     */
    public double cosine(Author a, Author b) {
        double numerator    = 0.;
        double denominatorA = 0;
        double denominatorB = 0;

        for (String file : files) {
            double aWeight = weightByCommit(a, file);
            double bWeight = weightByCommit(b, file);
            numerator += aWeight * bWeight;
            denominatorA += aWeight * aWeight;
            denominatorB += bWeight * bWeight;
        }
        double denominator = Math.sqrt(denominatorA * denominatorB);
        if (denominator == 0) return 0.;
        return numerator / denominator;
    }


    /**
     * Compute impact of {@link Author} based on number of commits, that change those file
     *
     * @return weight of author
     */
    private double weightByCommit(Author author, String file) {
        CollapsedFileChange changes = commits.get(author).get(file);
        if (changes == null) return 0.;
        return changes.collapsedCommits() * commitCoefficient.get(file);
    }

    /**
     * Getter of all authors
     */
    public ImmutableList<Author> getAuthors() {
        return Lists.immutable.ofAll(() -> commits.keysView().iterator());
    }

    public ImmutableMap<Author, ImmutableMap<String, CollapsedFileChange>> getCommits() {
        return commits;
    }

    public ImmutableMap<String, Double> getCommitCoefficient() {
        return commitCoefficient;
    }

    public ImmutableSet<String> getFiles() {
        return files;
    }

    public ImmutableList<Commit> getAllCommits() {
        return allCommits;
    }
}
