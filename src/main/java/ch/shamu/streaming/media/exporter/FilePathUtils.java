package ch.shamu.streaming.media.exporter;

import com.google.common.base.Joiner;

public class FilePathUtils {

	public static void assertSafe(String s){
		if (s.contains("../") || s.contains("..\\"))
			throw new IllegalAccessError();
	}
	
	public static String getExtension(String s){
		return s.split("\\.")[s.split("\\.").length-1];
	}

	public static String getFileName(String filePath) {
		return filePath.split("/")[filePath.split("/").length-1];
	}

	public static String replaceExtentionWith(String filePath,
			String newExtension) {
		String[] split = filePath.split("\\.");
		split[split.length-1] = "";
		Joiner j = Joiner.on(".");
		return  j.join(split)+newExtension;
	}
	
	public static String getDescription(String s){
		return s.substring(0, s.lastIndexOf(".")).replace("_",  " - ");
	}

}
