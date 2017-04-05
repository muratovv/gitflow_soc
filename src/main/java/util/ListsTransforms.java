package util;

import javafx.util.Pair;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author @muratovv
 * @date 28.02.17
 */
public class ListsTransforms {
    /**
     * Create overlapped pairs from source
     * <p>
     * Example: [1, 2, 3, 4] => [(1, 2), (2, 3), (3, 4)]
     */
    public static <T> List<Pair<T, T>> zipOverlappedPairs(List<T> lst) {
        ArrayList<Pair<T, T>> res = new ArrayList<>();
        for (int i = 0; i < lst.size() - 1; i++) {
            T current = lst.get(i);
            T next    = lst.get(i + 1);
            res.add(new Pair<>(current, next));
        }
        return res;
    }

    /**
     * Convert list to eclipse immutable list
     */
    public static <T> ImmutableList<T> convert(List<T> list) {
        return org.eclipse.collections.impl.factory.Lists.immutable.withAll(list);
    }
}
