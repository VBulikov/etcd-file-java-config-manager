package my.dev.libs.config.config_manager.config_loader;

import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Map;

/**
 * Created by Vladislav Bulikov on 15.09.2016.
 */

public class ConfigFiller {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ConfigFiller.class);

    public static void fillWithProperties(URL url, Map<String, Map<String, String>> map){
        EtcdLoader etcdLoader = new EtcdLoader(url);

        for(Map.Entry<String, Map<String, String>> entry: map.entrySet()){
            String dir = etcdLoader.createDirectory(entry.getKey());
            for(Map.Entry<String, String> property: entry.getValue().entrySet()){
                checkPropertiy(property);

                System.out.println(etcdLoader.createProperty(dir, property));
            }
        }
        etcdLoader.closeConnection();
    }

    private static Map.Entry checkPropertiy(Map.Entry property){
        if(property.getValue() != null && property.getValue().equals("!REQUIRED!")){
            logger.error(property.getKey().toString() + " property is required for correct work. ");
            System.exit(-1);
        }else if(property.getValue() == null){
            Map.Entry entry = property;
            entry.setValue("");
            return entry;
        }
        return property;
    }
}
