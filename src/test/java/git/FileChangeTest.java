package git;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author @muratovv
 * @date 27.02.17
 */
public class FileChangeTest {
    @Test
    public void parseDiff() throws Exception {
        String rawInput = "diff --git a/README.md b/README.md\n" +
                "index 8a4371c..86bb6c8 100644\n" +
                "--- a/README.md\n" +
                "+++ b/README.md\n" +
                "@@ -1,10 +1,10000 @@\n" +
                "-Copyright 2013, 2014 Dominik Stadler\n" +
                "-you may not use this file except in compliance with the License.\n" +
                "-You may obtain a copy of the License at\n" +
                "- http://www.apache.org/licenses/LICENSE-2.0\n" +
                "-Unless required by applicable law or agreed to in writing, software\n" +
                "-change anoter one if can\n" +
                "-WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "-See the License for the specific language governing permissions and\n" +
                "-limitations under the License.\n" +
                "-Add new\n" +
                "\\ No newline at end of file\n" +
                "+http://kPFapNUdC.gov\n" +
                "+http://zS.ru\n" +
                "+http://AWjvbMAHQh.com\n" +
                "+http://tDEl.gov\n" +
                "+http://AkgpjZ.org\n" +
                "+http://uSUoenybuF.ru";

        String     FILE_NAME = "test";
        FileChange changes   = FileChange.parseDiff(FILE_NAME, rawInput);
        assertTrue(changes.aliases().contains(FILE_NAME));
        assertEquals(changes.insertions(), 6);
        assertEquals(changes.deletions(), 10);

    }

}