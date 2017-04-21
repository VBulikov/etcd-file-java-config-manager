package my.dev.libs.config.config_manager.config_manager;

import my.dev.libs.config.config_manager.config_loader.ConfigUploader;
import my.dev.libs.config.config_manager.config_spec_builder.ConfigBuilder;
import org.kohsuke.args4j.CmdLineParser;

/**
 * Created by Vladislav Bulikov on 16.09.2016.
 */

public class ConfigManager {
    public static void main(String[] args) {

        CmdProps cmdProps = new CmdProps();
        CmdLineParser parser = new CmdLineParser(cmdProps);

        try{
            parser.parseArgument(args);

            switch (cmdProps.getCommand()){
                case "build": {
                    if(cmdProps.getRootDir() != null && cmdProps.getFileOut() != null){
                        ConfigBuilder configBuilder= new ConfigBuilder();
                        configBuilder.buildRawPropertyFile(cmdProps.getRootDir(), cmdProps.getFileOut());
                    }else{
                        System.out.println("You need to define both arguments : -dir and -out. Use command `help` for more info.");
                    }
                    break;
                }
                case "upload":{
                    if(cmdProps.getFileIn() != null){
                        if(cmdProps.getUrl() == null){
                            System.out.println("Argument -url was not found. Going to search environment variable CONFIG_URL.");
                            if(System.getenv("CONFIG_URL") != null){
                                cmdProps.setUrl(System.getenv("CONFIG_URL"));
                            }else {
                                System.out.println("Environment variable CONFIG_URL was not found.");
                                System.exit(-1);
                            }
                        }
                        ConfigUploader configUploader = new ConfigUploader();
                        configUploader.uploadProperties(cmdProps.getFileIn(), cmdProps.getUrl());
                    }else {
                        System.out.println("You need to define an input file : -in. Use command `help` for more info.");
                    }
                    break;
                }
                default:{
                    System.out.println("You need to define command.");
                    cmdProps.showHelp();
                    System.exit(-1);
                }
            }


        }catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
}
