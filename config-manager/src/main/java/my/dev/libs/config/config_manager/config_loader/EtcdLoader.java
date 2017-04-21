package my.dev.libs.config.config_manager.config_loader;

import mousio.etcd4j.EtcdClient;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vladislav Bulikov on 15.09.2016.
 */

public class EtcdLoader {
    private EtcdClient etcdClient;
    public EtcdLoader(URL url){
        try {
            this.etcdClient = new EtcdClient(url.toURI());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String createDirectory(String path){
        try {
            etcdClient.putDir(path).send();
        }catch (Exception e){
            e.printStackTrace();
        }
        return path;
    }

    public String createProperty(String path, Map.Entry<String, String> property){
        try {
            String fullpath = path+'/'+property.getKey();
            etcdClient.put(fullpath, property.getValue()).timeout(5, TimeUnit.SECONDS).send().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return path+'/'+property.getKey();
    }


    public int closeConnection(){
        try {
            etcdClient.close();
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

}
