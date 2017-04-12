package git;

import org.junit.Test;

import static git.Author.make;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author @muratovv
 * @date 13.04.17
 */
public class AuthorTest {
    @Test
    public void makeTest() throws Exception {
        Author vasya = make("vasya", "vasya@pimp.org");
        Author petya = make("petya", "vasya@pimp.org");
        assertEquals(vasya.hashCode(), petya.hashCode());
        Author uyra = Author.make("vasya", "uyra@sauna.com");
        assertEquals(vasya.hashCode(), uyra.hashCode());
        Author noname = Author.make("noname", "no@mail.com");
        assertNotEquals(vasya.hashCode(), noname.hashCode());
    }

}