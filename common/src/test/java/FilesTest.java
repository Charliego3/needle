import io.github.kits.log.Logger;
import junit.framework.TestCase;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class FilesTest extends TestCase {

	public void testGetPath() {
		Path path = Paths.get("logger.properties");
		Logger.info(path.toAbsolutePath().toString());
	}

}
