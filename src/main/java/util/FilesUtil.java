package util;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author @muratovv
 * @date 05.04.17
 */
public class FilesUtil {

    /**
     * Write data to file
     *
     * @param path dest path
     * @param data for store
     *
     * @return successful of writing
     */
    public static boolean writeFile(String path, String data) {
        try {
            PrintWriter writer = new PrintWriter(path, "UTF-8");
            writer.write(data);
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
