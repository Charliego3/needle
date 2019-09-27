import io.github.kits.Strings;
import io.github.kits.log.Logger;
import junit.framework.TestCase;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class StringsTest extends TestCase {

	public void testIndentRight() {
		String source = "haha";
		Logger.info(Strings.indentRight(source, 3) + "=");
	}

}
