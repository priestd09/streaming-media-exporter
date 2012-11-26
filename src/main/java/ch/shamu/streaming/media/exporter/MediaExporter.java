package ch.shamu.streaming.media.exporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

// Utility class to check if a tool exists on the system and invoke it
public class MediaExporter {
	
	public static final String PROCESS_DONE = "Process Done";
	public static final Object COMPLETED_FILE = "DestinationFile";
	
	public enum ExternalTool {
		YOUTUBE_DL("youtube-dl"), GROOVE_DL("groove.py"), FFMPEG("ffmpeg");
		public String name;
		public String params;
		public String path;
		
		private ExternalTool(String s){
			name = s;
			path = WebappConfig.get(name+".path");
			params = WebappConfig.get(name+".params");
		}
	}
	
	private static MediaExporter instance;
	
	private MediaExporter(){
		
	}
	
	public static MediaExporter getInstance(){
		
		if (instance == null){
			instance = new MediaExporter();
		}

		return instance;
		
	}
	
	public void run(String url, String filename, Boolean extractAudio, Boolean keepVideo) throws IOException{
		System.out.println("Downloading : "+url);
		
		ProcessRunner runner = new ProcessRunner(url, filename, extractAudio, keepVideo);
		runner.createLogFile();
		runner.start();
	}
	

	
	public class ProcessRunner extends Thread {
		  private Process p;
		  private String url;
		  private String id;
		  private String logFileName;
		  private Boolean extractAudio;
		  private Boolean keepVideo;
		  
		  public ProcessRunner(String url, String id, Boolean extractAudio, Boolean keepVideo) {
			this.setDaemon(true);
			this.id = id;
			this.keepVideo = keepVideo;
			this.extractAudio = extractAudio;
			this.url = url;
		    
		  }
		  
		  public void createLogFile() {
			 this.logFileName = WebappConfig.get("log.dir")+"/downloads/"+id.replace("/", "_")+".log";
			 FileContentManager.createFile(this.logFileName);
		  }
		  
		  public void run() {
			try{
				
			    if (url.startsWith("groove:")){
			    	FileContentManager.append(logFileName, "Groove-dl query");
			    	ProcessBuilder pb = new ProcessBuilder(WebappConfig.get("python.path"), "-u", ExternalTool.GROOVE_DL.path, url.split(":")[1]);
			    	pb.redirectErrorStream(true);
			    	pb.directory(new File(WebappConfig.get("temp.folder")));
			    	Map<String,String> env = pb.environment();
			    	System.out.println(WebappConfig.get("priority.path")+":"+env.get("PATH"));
					env.put("PATH", WebappConfig.get("priority.path")+":"+env.get("PATH"));
					this.p = pb.start();
					String downloadedMp3FileName = logExecution(p, logFileName);
					String dest = WebappConfig.get("music.folder")+"/"+id+".mp3";
					if (! new File(dest).getParentFile().exists()){
						new File(dest).getParentFile().mkdirs();
					}
					FileContentManager.append(logFileName, "mv "+downloadedMp3FileName+" "+dest);
					pb = new ProcessBuilder("mv", downloadedMp3FileName, dest);
					pb.start().waitFor();
					FileContentManager.append(logFileName, COMPLETED_FILE+":"+id+".mp3");
					FileContentManager.writeToDisk(logFileName);
			    	return;
			    }
			    
				ProcessBuilder pb = new ProcessBuilder(ExternalTool.YOUTUBE_DL.path, url);
				pb.directory(new File(WebappConfig.get("temp.folder")));
				pb.redirectErrorStream(true);
				Map<String,String> env = pb.environment();
				env.put("PATH", WebappConfig.get("priority.path")+":"+env.get("PATH"));
				this.p = pb.start();
				
				String downloadedVideoFileName = WebappConfig.get("temp.folder")+"/"+logExecution(p, logFileName);

		        if (extractAudio != null && extractAudio){
		        	if (! new File(WebappConfig.get("music.folder")+"/"+id+".mp3").getParentFile().exists()){
		        		new File(WebappConfig.get("music.folder")+"/"+id+".mp3").getParentFile().mkdirs();
		        	}
		        	pb = new ProcessBuilder(ExternalTool.FFMPEG.path, "-i",downloadedVideoFileName,"-f","mp3","-ab","256000","-vn",WebappConfig.get("music.folder")+"/"+id+".mp3");
					FileContentManager.append(logFileName, ExternalTool.FFMPEG.path+" -i "+downloadedVideoFileName+" -f mp3 -ab 256000 -vn "+WebappConfig.get("music.folder")+"/"+id+".mp3");
		        	pb.directory(new File(WebappConfig.get("temp.folder")));
					pb.redirectErrorStream(true);
					env = pb.environment();
					env.put("PATH", WebappConfig.get("priority.path")+":"+env.get("PATH"));
					this.p = pb.start();
					FileContentManager.append(logFileName,"Extracting audio from "+downloadedVideoFileName+" to mp3");
					logExecution(p, logFileName);
		        }
		        FileContentManager.append(logFileName,PROCESS_DONE);
				System.out.println("exportation finished !");
				if (downloadedVideoFileName != null){
					// create destination directory if it doest not exist
					String src = downloadedVideoFileName;
					String relativeDest = id+"."+FilePathUtils.getExtension(src);
					String dest = WebappConfig.get("videos.folder")+"/"+relativeDest;

					
					if (keepVideo != null && keepVideo){
						FileContentManager.append(logFileName,"Moving downloaded file to videos folder");
						FileContentManager.append(logFileName,"mv "+src+" "+dest);
						if (! new File(dest).getParentFile().exists()){
							new File(dest).getParentFile().mkdirs();
						}
						pb = new ProcessBuilder("mv", src, dest);
					}
					else {
						FileContentManager.append(logFileName,"Removing downloaded video file");
						pb = new ProcessBuilder("rm", "-rf", src);
					}
					pb.start().waitFor();
					FileContentManager.append(logFileName,COMPLETED_FILE+":"+id+"."+FilePathUtils.getExtension(downloadedVideoFileName));
					FileContentManager.writeToDisk(logFileName);
				}
			  }
			catch (Exception e){
				e.printStackTrace();
			}
		  }
		  
		private String logExecution(Process p, String fileName) throws IOException, InterruptedException {
			String s;
			String downloadedFileName = null;
		    BufferedReader stdout = new BufferedReader(new InputStreamReader(p.getInputStream()));
		    while ((s = stdout.readLine()) != null) {
		    	FileContentManager.append(fileName, s);
			    String[] split = s.split(" ");
			    if (split.length > 1 && split[1].equals("Destination:")){
			    	downloadedFileName = split[split.length-1];
			    }

			}
	        p.waitFor();
	        return downloadedFileName;
		}
	}
}
