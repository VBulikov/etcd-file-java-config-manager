package my.dev.libs.config.config_manager.config_manager;

import org.apache.commons.validator.routines.UrlValidator;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Vladislav Bulikov on 16.09.2016.
 */

public class CmdProps {
    private Path rootDir = Paths.get(System.getProperty("user.dir"));
    private File fileIn = null;
    private File fileOut = new File(System.getProperty("user.dir"),"default_config.json");
    private URL url = null;
    private String command = "help";
    private UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);


    @Option(name="-dir",usage="Sets a root directory to find configs")
    public void setRootDir(String rootDir) {
        try {
            this.rootDir = Paths.get(rootDir);
        }catch (Exception e){
            System.out.println("Path : " + rootDir + " - is not correct.");
            System.exit(-1);
        }
    }

    @Option(name="-in",usage="Sets an input file")
    public void setFileIn(String fileIn) {
        try {
            File file;
            if(Paths.get(fileIn).isAbsolute()){
                file = new File(fileIn);
            }else {
                file = new File(System.getProperty("user.dir"),fileIn);
            }

            if(!file.exists()){
                System.out.println("Path to input file : " + fileIn + " - is not correct. File doesn`t exist");
                System.exit(-1);
            }
            this.fileIn = file;
        }catch (Exception e){
            System.out.println("Path to input file : " + fileIn + " - is not correct.");
            System.exit(-1);
        }
    }

    @Option(name="-out",usage="Sets an output")
    public void setFileOut(String fileOut) {
        try {
            this.fileOut = new File(fileOut,"default_config.json");
        }catch (Exception e){
            System.out.println("Path to result file : " + fileOut + " - is not correct.");
            System.exit(-1);
        }
    }

    @Option(name="-url",usage="Sets an URL")
    public void setUrl(String url) {
        if(this.validator.isValid(url)) {
            try {
                this.url = new URL(url);

            } catch (Exception e) {
                System.out.println("Etcd server url : " + url + " - is not correct.");
                System.exit(-1);
            }
        }else {
            System.out.println("Etcd server url : " + url + " - is not correct.");
            System.exit(-1);
        }
    }

    @Argument(index = 0, metaVar = "command")
    public void setCommand(String function){
        if(function.equals("build") || function.equals("upload")){
            this.command = function;
        }else {
            showHelp();
            System.exit(0);
        }
    }
    public void showHelp(){
        System.out.println("~~~~~~~~~");
        System.out.println("Possible commands:");
        System.out.println("~~~~~~~~~");
        System.out.println("help");
        System.out.println("");
        System.out.println("~~~~~~~~~");
        System.out.println("build");
        System.out.println("");
        System.out.println("with arguments: ");
        System.out.println("-dir : as a Path to root directory. If not defined - uses current directory.");
        System.out.println("-out : as a file wrere to put configuration. If not defined - uses current directory/default.json.");
        System.out.println("~~~~~~~~~");
        System.out.println("upload");
        System.out.println("");
        System.out.println("with arguments: ");
        System.out.println("-in : as a Path to .json file to upload");
        System.out.println("-url : as an URL to etcd server. If not defined - uses CONFIG_URL environment variable.");
        System.out.println("~~~~~~~~~");
    }


    public File getFileIn() {
        return fileIn;
    }

    public File getFileOut() {
        return fileOut;
    }

    public String getCommand() {
        return command;
    }

    public URL getUrl() {
        return url;
    }

    public Path getRootDir(){
        return rootDir;
    }
}
