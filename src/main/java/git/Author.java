package git;

import com.google.common.base.MoreObjects;
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
}
