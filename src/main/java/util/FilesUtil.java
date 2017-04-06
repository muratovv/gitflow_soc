package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

/**
 * @author @muratovv
 * @date 05.04.17
 */
public class FilesUtil {
    private static final Logger logger = LoggerFactory.getLogger(FilesUtil.class);

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
            File file = new File(path);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            PrintWriter writer = new PrintWriter(path, "UTF-8");
            writer.write(data);
            writer.close();
            return true;
        } catch (IOException e) {
            logger.error("writeFile", e);
            return false;
        }
    }

    public static boolean writeFile(Path path, String data) {
        return writeFile(path.toString(), data);
    }
}
