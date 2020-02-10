import io.github.common.model.User;
import io.github.kits.Envs;
import io.github.kits.Types;
import io.github.kits.log.Logger;
import junit.framework.TestCase;

import java.util.Arrays;

public class TypesTest extends TestCase {

	public void testType() {
		Object arr = new int[]{1, 2, 4, 6};

//		Short arr = 6;
		Integer[] type = Types.type(arr, Integer[].class);
		System.out.println(Arrays.toString(type));
		Logger.infof("Types.type: {}", 1, 2, 3);
		Logger.infof("Types.type: {}{}", "ss", type);
		Logger.infof("Types.type: {}", type);
		Logger.infof("Types.type: {}", type);
		Logger.infof("Types.type: {}", type);

		Envs.sleep(11000);
	}

	public void testPrintBasicTypeArr() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		System.out.println(short[].class.getName());
		System.out.println(int[].class.getName());
		System.out.println(long[].class.getName());
		System.out.println(float[].class.getName());
		System.out.println(double[].class.getName());
		System.out.println(boolean[].class.getName());
		System.out.println(char[].class.getName());
		System.out.println(byte[].class.getName());
		System.out.println(Short[].class.getName());
		System.out.println(Integer[].class.getName());
		System.out.println(Long[].class.getName());
		System.out.println(Float[].class.getName());
		System.out.println(Double[].class.getName());
		System.out.println(Boolean[].class.getName());
		System.out.println(Character[].class.getName());
		System.out.println(Byte[].class.getName());
	}

	public void testArr() {
		short[] shorts = new short[10];
		arrVal(shorts);
		System.out.println(Arrays.toString(shorts));
	}

	public void arrVal(short[] shorts) {
		for (int i = 0; i < 8; i++) {
			shorts[i] = (short) (i+1);
		}
	}

}
