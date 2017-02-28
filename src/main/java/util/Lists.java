package util;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author @muratovv
 * @date 28.02.17
 */
public class Lists {
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
}
