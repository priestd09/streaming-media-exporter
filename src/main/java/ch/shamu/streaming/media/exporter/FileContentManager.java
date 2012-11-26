package ch.shamu.streaming.media.exporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

// This class manages text file lines in memory and has the ability to read/persist them on disk
// It's mostly used as a synchronized in memory cache to improve performance and avoid useless disk IO
public class FileContentManager {

	// hash of files content by filename
	static final Map<String,List<String>> cache = new ConcurrentHashMap<String, List<String>>();
	static final Map<String, Date> timeCache = new ConcurrentHashMap<String, Date>();
	
	public static List<String> getFileContent(String fileName) throws FileNotFoundException{
		if (cache.containsKey(fileName)){
			return cache.get(fileName);
		}
		// read the file
		List<String> content = new ArrayList<String>();
		File file = new File(WebappConfig.get("log.dir")+"/downloads/"+fileName);
		Scanner s = new Scanner(file).useDelimiter("\n");
		while (s.hasNext()){
			content.add(s.next());
		}
		cache.put(fileName, content);
		timeCache.put(fileName, new Date(file.lastModified()));
		return content;
	}

	public static void append(String fileName,String line) throws FileNotFoundException {
		List<String> content = getFileContent(FilePathUtils.getFileName(fileName));
		content.add(line);
	}
	
	public static void createFile(String fileName){
		cache.put(FilePathUtils.getFileName(fileName),new ArrayList<String>());
		timeCache.put(FilePathUtils.getFileName(fileName), new Date());
		writeToDisk(fileName);
	}
	
	// should never by called simultaneously for the same file but let's make sure
	public static void writeToDisk(String fileName){
		File file = new File(fileName);
		if (!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			Writer fstream = new FileWriter(file,false); // overwrite
			BufferedWriter writer = new BufferedWriter(fstream);
			for (String s : getFileContent(FilePathUtils.getFileName(fileName))){
				writer.append(s);
				writer.newLine();
			}
			writer.flush();
			writer.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

	public static Date getFileTime(String logFileName) throws FileNotFoundException {
		getFileContent(logFileName); // make sure the log has been read
		return timeCache.get(logFileName);
	}
	
}
