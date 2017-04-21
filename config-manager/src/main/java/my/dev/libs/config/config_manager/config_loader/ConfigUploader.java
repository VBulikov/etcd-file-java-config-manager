package my.dev.libs.config.config_manager.config_loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.net.URL;
import java.util.Map;

/**
 * Created by Vladislav Bulikov on 15.09.2016.
 */

public class ConfigUploader {


    public void uploadProperties(File fileIn, URL url){
        try{
            ObjectMapper mapper = new ObjectMapper();
            // read JSON from a file
            Map<String, Map<String, String>> properties = mapper.readValue(
                    fileIn.getAbsoluteFile(),
                    new TypeReference<Map<String, Map>>() {
                    });
            System.out.println(properties.toString());
            ConfigFiller.fillWithProperties(url, properties);
            System.exit(0);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
