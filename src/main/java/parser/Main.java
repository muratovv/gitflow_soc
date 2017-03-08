package parser;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OneArgumentOptionHandler;
import org.kohsuke.args4j.spi.Setter;

import java.io.File;

/**
 * @author @muratovv
 * @date 20.02.17
 */
public class Main {
    public static void main(String[] args) {
        InputParameters p      = new InputParameters();
        CmdLineParser   parser = new CmdLineParser(p);
        System.out.println("target repo: " + p.targetRepository);
    }

    private static class InputParameters {
        @Option(name = "target", required = true, handler = RepoHelper.class)
        private File targetRepository;
    }

    /**
     * Helper class for parsing repository
     */
    private static class RepoHelper extends OneArgumentOptionHandler<File> {

        public RepoHelper(CmdLineParser parser, OptionDef option, Setter<? super File> setter) {
            super(parser, option, setter);
        }

        @Override
        protected File parse(String argument) throws NumberFormatException, CmdLineException {
            File f = new File(argument);
            if (!f.isDirectory()) new CmdLineException(owner, "Repository must be folder");
            if (f.list((dir, name) -> ".git".equals(name)).length == 0)
                new CmdLineException(super.owner, "Folder must contains .git folder");
            return f;
        }
    }
}
