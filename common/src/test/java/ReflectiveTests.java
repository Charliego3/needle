import io.github.common.model.User;
import io.github.kits.Reflective;
import junit.framework.TestCase;

import java.lang.reflect.InvocationTargetException;

public class ReflectiveTests extends TestCase {

	public void testNewInstance() throws IllegalAccessException, InvocationTargetException, InstantiationException {
		User user = new User();

		User instance = Reflective.instance(user);
		System.out.println(instance);
	}

}
