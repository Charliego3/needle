import io.github.kits.PropertiesKit;
import io.github.kits.log.Logger;
import junit.framework.TestCase;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class ProptiesKitTest extends TestCase {

	public void testMapPut() {
		ConcurrentHashMap<String, File> map = new ConcurrentHashMap<>();

		System.out.println(map.put("cache", new File("/Users/nzlong/dev/project/Backstage/winter/web/pom.xml")));
		System.out.println(map.put("cache", new File("/Users/nzlong/dev/project/Backstage/winter/web/src/main/resources/logger.properties")));

		System.out.println(map.get("cache"));
	}

	public void testGetPorp() {
		String filePath = "/Users/nzlong/dev/project/Backstage/winter/common/target/test-classes/logger.properties";
//		URL    resource = FileKit.class.getClassLoader().getResource(filePath);
//		Logger.info(resource.getFile());
		Logger.info("haha");
		String prop = PropertiesKit.getString(new File(filePath), "LogType").orElse(null);
		Logger.info(prop);
		prop = PropertiesKit.getString(new File(filePath), "LogPath").orElse(null);
		Logger.info(prop);
		prop = PropertiesKit.getString(new File(filePath), "LogLevelsss").orElse(null);
		Logger.info(prop);
		prop = PropertiesKit.getString(new File("/Users/nzlong/dev/project/Backstage/winter/common/src/test/resources/sdfsdf.properties"), "LogLevelsss").orElse(null);
		Logger.info(prop);
		prop = PropertiesKit.getString(new File("/Users/nzlong/dev/project/Backstage/winter/common/src/test/resources/sdfsdf.properties"), "LogPath").orElse(null);
		Logger.info(prop);
	}

	public void testGet() {
		String log = PropertiesKit.getString("logger", "LogType").orElse("logger.properties.log is null");
		Logger.error(log);
	}

}
