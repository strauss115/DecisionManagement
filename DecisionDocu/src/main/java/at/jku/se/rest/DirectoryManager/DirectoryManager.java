package at.jku.se.rest.DirectoryManager;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Class Directory Manager 
 * @author August
 *
 */
public class DirectoryManager {
	
	/**
	 * Returns directory
	 * @return {@link File}
	 */
	public static File getDirectory(){
		File file = getDirectoryOnline();
		if (file!=null&&!file.getParentFile().getName().equals("target")){
			return file;
		}
		return getDirectoryOffline();
	}
	
	private static File getDirectoryOffline(){
		File file = new File("src/main/resources");
		return file;
	}
	
	private static File getDirectoryOnline(){
		File file;
		ClassLoader threadClassLoader = Thread.currentThread()
				.getContextClassLoader();
		if (threadClassLoader != null) {
			URL url = threadClassLoader.getResource("");
			if (url != null) {
				try {
					file = new File(url.toURI());
					if(file.exists()){
						return file;
					}
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
