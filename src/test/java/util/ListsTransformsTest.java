package util;

import com.google.common.collect.ImmutableList;
import javafx.util.Pair;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author @muratovv
 * @date 28.02.17
 */
public class ListsTransformsTest {
    @Test
    public void zipOverlappedPairs() throws Exception {
        List<Pair<Integer, Integer>> pairs = ListsTransforms.zipOverlappedPairs(ImmutableList.of(1, 2, 3, 4));
        assertEquals(new Pair<>(1, 2), pairs.get(0));
        assertEquals(new Pair<>(2, 3), pairs.get(1));
        assertEquals(new Pair<>(3, 4), pairs.get(2));
        assertEquals(3, pairs.size());
    }

    @Test
    public void emptyZipOverlappedPairs() throws Exception {
        List<Pair<Integer, Integer>> pairs = ListsTransforms.zipOverlappedPairs(ImmutableList.of(1));
        assertEquals(0, pairs.size());
    }
}