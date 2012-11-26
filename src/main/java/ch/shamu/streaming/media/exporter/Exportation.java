package ch.shamu.streaming.media.exporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Exportation {
	private String logFileName;
	private String description;
	private String mp3Path;
	private String videoPath;
	private Date time;
	private String formattedTime;
	private Boolean running;
	// SimpleDateFormat is not threadsafe
	private ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>(){

		@Override
		protected SimpleDateFormat initialValue() {
			// TODO Auto-generated method stub
			return new SimpleDateFormat("dd.MM.yyyy HH:mm");
		}
		
	};
	
	public Exportation(String logFileName) throws FileNotFoundException {
		this.logFileName = logFileName;
		this.description = FilePathUtils.getDescription(logFileName);
		this.time = FileContentManager.getFileTime(logFileName);
		this.formattedTime = dateFormat.get().format(FileContentManager.getFileTime(logFileName));
		this.running = true;
		List<String> fileContent = FileContentManager.getFileContent(logFileName);
		for (String line :fileContent){
			if (line.split(":")[0].equals(MediaExporter.COMPLETED_FILE)){
				this.running = false;
				this.videoPath = WebappConfig.get("videos.folder")+"/"+line.split(":")[1];
				this.mp3Path = FilePathUtils.replaceExtentionWith(videoPath,"mp3").replace(WebappConfig.get("videos.folder"),WebappConfig.get("music.folder"));
				
				if (!new File(videoPath).exists()) videoPath = null;
				if (!new File(mp3Path).exists()) mp3Path = null;
			}	
		}
	}

	public String getLogFileName() {
		return logFileName;
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMp3Path() {
		return mp3Path;
	}

	public void setMp3Path(String mp3Path) {
		this.mp3Path = mp3Path;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}
	
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getFormattedTime() {
		return formattedTime;
	}

	public void setFormattedTime(String time) {
		this.formattedTime = time;
	}

	public Boolean getRunning() {
		return running;
	}

	public void setRunning(Boolean running) {
		this.running = running;
	}
	
}
