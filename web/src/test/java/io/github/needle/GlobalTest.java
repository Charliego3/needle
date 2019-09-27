package io.github.needle;

import junit.framework.TestCase;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class GlobalTest extends TestCase {

	public void testReplace$() {
		String t = "abcde$fghijklmn";
		System.out.println(t.replaceFirst("$", "\\$"));
	}

	public void testGetBytesFromFile() throws IOException {
		getBytesFromFile();
	}

	public void testSaveFile() throws IOException {
		File file = Files.createFile(Paths.get("test_fileName_"+ System.currentTimeMillis()+ ".png")).toFile();
		try (FileOutputStream fileOuputStream = new FileOutputStream(file);
			 BufferedOutputStream bf = new BufferedOutputStream(fileOuputStream)) {
//			bf.write(getBytesFromFile().getBytes(StandardCharsets.UTF_8));
			bf.write(getBytesFromFile().getBytes(StandardCharsets.ISO_8859_1));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(file);
	}

	public void testSave() {
		StringBuilder stringBuilder = new StringBuilder();

		getFileFromBytes(stringBuilder.toString().getBytes(), Paths.get("random" + System.currentTimeMillis() + ".png").toAbsolutePath().toString());
	}

	public String getBytesFromFile() throws IOException {
		File f = new File("/Users/nzlong/Desktop/Screenshot_1550889614.png");
		FileInputStream       stream = new FileInputStream(f);
		ByteArrayOutputStream out    = new ByteArrayOutputStream(1000);
		byte[]                b      = new byte[1000];
		int                   n;
		while ((n = stream.read(b)) != -1)
			out.write(b, 0, n);
		stream.close();
		out.close();
		byte[] bytes = out.toByteArray();

		System.out.println(new String(bytes, StandardCharsets.ISO_8859_1));

		return new String(bytes, StandardCharsets.ISO_8859_1);
//		return bytes;
	}

	/** *//**
	 * 把字节数组保存为一个文件 
	 * @Author Sean.guo
	 * @EditTime 2007-8-13 上午11:45:56 
	 */
	public static File getFileFromBytes(byte[] b, String outputFile) {
		BufferedOutputStream stream = null;
		File                 file   = null;
		try {
			file = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
			try {
			stream.close();
			} catch (IOException e1) {
			e1.printStackTrace();
			}
			}
		}
		return file;
	}

}
