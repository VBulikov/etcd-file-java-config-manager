package my.dev.libs.config.config_manager.config_spec_builder;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by Vladislav Bulikov on 14.09.2016.
 */

public class YamlReader implements ConfigurationSourceReader {

	@Override
	public boolean supports(Path path) {
		return path.toString().contains("config.yml");
	}

	public Map<String, ?> read(String path) throws IOException {
		InputStream in = Files.newInputStream(Paths.get(path));
		Yaml yaml = new Yaml();
		return (Map<String, Object>) yaml.load(in);
	}

	public <T> T read(String path, Class<T> c) throws IOException {
		Yaml yaml = new Yaml();
		try (InputStream in = Files.newInputStream(Paths.get(path))) {
			return yaml.loadAs(in, c);
		}
	}

}
