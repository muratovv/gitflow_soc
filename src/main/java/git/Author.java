package git;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.eclipse.jgit.lib.PersonIdent;

/**
 * @author @muratovv
 * @date 27.02.17
 */
public class Author {
    private final String name;
    private final String mail;

    public Author(String name, String emailAddress) {
        this.name = name;
        this.mail = emailAddress;
    }

    /**
     * Fill authors fields from {@code personIdent}
     *
     * @param personIdent identifier in JGIT
     *
     * @return filled {@link Author}
     */
    public static Author parse(PersonIdent personIdent) {
        return new Author(personIdent.getName(), personIdent.getEmailAddress());
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
        return Objects.equal(name, author.name) &&
                Objects.equal(mail, author.mail);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, mail);
    }
}
