package my.dev.libs.config.config_manager.config_spec_builder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * Created by Vladislav Bulikov on 14.09.2016.
 */

public interface ConfigurationSourceReader {

	boolean supports(Path path);

	Map<String, ?> read(String path) throws IOException;

	<T> T read(String path, Class<T> c) throws IOException;
}
