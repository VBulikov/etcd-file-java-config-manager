package my.dev.libs.config.config_manager.config_spec_builder;

import java.util.Map;

/**
 * Created by Vladislav Bulikov on 14.09.2016.
 */

public final class Configuration {

    private Map< String, String > serviceInfo;
    private Map< String, String > properties;

    public Map<String, String> getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(Map<String, String> serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "serviceInfo=" + serviceInfo +
                ", properties=" + properties +
                '}';
    }
}
