package git;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.jgit.lib.PersonIdent;

/**
 * @author @muratovv
 * @date 27.02.17
 */
public class Author {
    private final String name;
    private final String mail;
    private Integer hash = null;

    private Author(String name, String emailAddress) {
        this.name = name;
        this.mail = emailAddress;
    }

    public static Author make(String name, String mail) {
        Author  newAuthor = new Author(name, mail);
        Integer hashCode  = getHashCode(newAuthor);
        authors.add(hashCode == null ? newAuthor : newAuthor.setHash(hashCode));
        return newAuthor;
    }

    /**
     * Fill authors fields from {@code personIdent}
     *
     * @param personIdent identifier in JGIT
     *
     * @return filled {@link Author}
     */
    public static Author parse(PersonIdent personIdent) {
        return make(personIdent.getName(), personIdent.getEmailAddress());
    }

    public String name() {
        return name;
    }

    public String mail() {
        return mail;
    }

    @Override
    public String toString() {
        return MoreObjects
                .toStringHelper(this)
                .add("name", name)
                .add("mail", mail)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author)) return false;
        Author author = (Author) o;
        return Objects.equal(name, author.name) ||
                Objects.equal(mail, author.mail);
    }

    private Author setHash(int val) {
        this.hash = val;
        return this;
    }

    @Override
    public int hashCode() {
        if (hash == null)
            hash = Objects.hashCode(name, mail);
        return hash;
    }

    private static MutableList<Author> authors = Lists.mutable.empty();

    private static Integer getHashCode(Author newAuthor) {
        final Integer[] hashCode = {null};
        authors.forEach(each -> {
            boolean equals = each.equals(newAuthor);
            if (equals) hashCode[0] = each.hashCode();
        });

        return hashCode[0];
    }
}
