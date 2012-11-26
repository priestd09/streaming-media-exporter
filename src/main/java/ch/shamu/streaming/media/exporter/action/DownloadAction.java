package ch.shamu.streaming.media.exporter.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.Validate;
import ch.shamu.streaming.media.exporter.DefaultAction;
import ch.shamu.streaming.media.exporter.FilePathUtils;
import ch.shamu.streaming.media.exporter.WebappConfig;

public class DownloadAction extends DefaultAction {

	@Validate(required=true)
	private String filePath;
	
	@DefaultHandler
	public Resolution download() throws FileNotFoundException{
		FilePathUtils.assertSafe(filePath);
		if (!filePath.startsWith(WebappConfig.get("videos.folder")) && !filePath.startsWith(WebappConfig.get("music.folder"))){
			throw new IllegalAccessError("File is outside of authorized folders");
		}
		FileReader reader = new FileReader(new File(filePath));
		return new StreamingResolution("application/octet-stream", reader).setFilename(FilePathUtils.getFileName(filePath));
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
