package my.dev.libs.config.config_manager.config_spec_builder;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Vladislav Bulikov on 14.09.2016.
 */

public class ConfigBuilder {

	private Map configurations;

	public Map getConfigurations() {
		return configurations;
	}

	public void setConfigurations(Map configurations) {
		this.configurations = configurations;
	}

	public ConfigBuilder() {
		this.configurations = new LinkedHashMap<>();
	}

	public void buildRawPropertyFile(Path rootDir, File fileOut) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Files.walk(rootDir).filter(Files::isRegularFile).forEach(this::fillConfigurations);

			mapper.writerWithDefaultPrettyPrinter().writeValue(fileOut, configurations);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fillConfigurations(Path path) {
		List<ConfigurationSourceReader> readers = new LinkedList<>();
		readers.add(new YamlReader());
		readers.add(new EsfServiceInventoryXmlReader());
		try {
			for (ConfigurationSourceReader reader : readers) {
				if (reader.supports(path)) {
					System.out.println(path);
					System.out.println(reader.read(path.toString()));
					System.out.println();
					Configuration configuration = reader.read(path.toString(), Configuration.class);
					configurations.put(configuration.getServiceInfo().get("path"), configuration.getProperties());
					break;
				}
			}
		} catch (

		IOException e) {

		}

	}

}
