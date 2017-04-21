package my.dev.libs.config.config_manager.config_spec_builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Vladislav Bulikov on 14.09.2016.
 */

public class EsfServiceInventoryXmlReader implements ConfigurationSourceReader {

	private static final String PROPERTIES_DIR_NAME = "ooa/acf/security/esf-service";
	private static final String ROLES_TO_FUNCTION_ITEMS_PROPERTY = "role-templates";

	@Override
	public boolean supports(Path path) {
		return path.toString().endsWith("service-inventory.xml");
	}

	@Override
	public Map<String, ?> read(String path) throws IOException {
		Map<String, Set<String>> roleToFunctionItemsMapping = new HashMap<>();
		try {
			roleToFunctionItemsMapping = getRolesToFunctionItemsMapping(new File(path));
		} catch (SAXException | ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return roleToFunctionItemsMapping;
	}

	@Override
	public <T> T read(String path, Class<T> c) throws IOException {
		Map<String, String> serviceInfo = new HashMap<>();
		Map<String, String> properties = new HashMap<>();
		// supports only config objects since it needs to be filled manually
		if (!(c.isAssignableFrom(Configuration.class))) {
			throw new InvalidParameterException();
		}
		T configuration = null;
		try {
			configuration = c.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// won't happen
			e.printStackTrace();
		}
		((Configuration) configuration).setServiceInfo(serviceInfo);
		((Configuration) configuration).setProperties(properties);

		serviceInfo.put("path", PROPERTIES_DIR_NAME);
		properties.put(ROLES_TO_FUNCTION_ITEMS_PROPERTY, serializedRolesToFunctionItemMapping(new File(path)));

		return configuration;
	}

	private String serializedRolesToFunctionItemMapping(File file) {
		Map<String, Set<String>> rolesToFunctionItem;
		String serialized = "";
		try {
			rolesToFunctionItem = getRolesToFunctionItemsMapping(file);
			ObjectMapper objectMapper = new ObjectMapper();
			serialized = objectMapper.writeValueAsString(rolesToFunctionItem);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		// Map readValue = objectMapper.readValue(serialized, Map.class);
		// System.out.println(readValue);
		return serialized;
	}

	private Map<String, Set<String>> getRolesToFunctionItemsMapping(File file)
			throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document = factory.newDocumentBuilder().parse(file);
		Map<String, Set<String>> rolesToFunctionItem = new HashMap<>();
		Map<String, Set<String>> functionGroups = getFunctionGroups(document);
		NodeList roles = document.getElementsByTagName("role");

		// for each role
		for (int i = 0; i < roles.getLength(); i++) {
			Element role = (Element) roles.item(i);
			String roleCode = role.getAttribute("code");
			rolesToFunctionItem.put(roleCode, new HashSet<>());
			NodeList groupsOfRole = role.getElementsByTagName("group");
			// collect all function items of all it's function groups
			for (int j = 0; j < groupsOfRole.getLength(); j++) {
				Element group = (Element) groupsOfRole.item(j);
				String functionGroupCode = group.getAttribute("code");
				rolesToFunctionItem.get(roleCode).addAll(functionGroups.get(functionGroupCode));
			}
		}
		return rolesToFunctionItem;
	}

	private Map<String, Set<String>> getFunctionGroups(Document document) {
		Map<String, Set<String>> functionGroupsToFunctionItems = new HashMap<>();
		Element functionGroups = (Element) document.getElementsByTagName("function-groups").item(0);
		NodeList groups = functionGroups.getElementsByTagName("group");
		// for each function group
		for (int i = 0; i < groups.getLength(); i++) {
			Element group = (Element) groups.item(i);
			String groupCode = group.getAttribute("code");
			functionGroupsToFunctionItems.put(groupCode, new HashSet<>());
			NodeList functionItems = group.getElementsByTagName("function");
			// collect it's function items
			for (int j = 0; j < functionItems.getLength(); j++) {
				Element functionItem = (Element) functionItems.item(j);
				functionGroupsToFunctionItems.get(groupCode).add(functionItem.getAttribute("code"));
			}
		}
		return functionGroupsToFunctionItems;
	}

}
