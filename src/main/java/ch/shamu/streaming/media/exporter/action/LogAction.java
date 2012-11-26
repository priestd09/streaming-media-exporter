package ch.shamu.streaming.media.exporter.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.base.Joiner;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;
import ch.shamu.streaming.media.exporter.DefaultAction;
import ch.shamu.streaming.media.exporter.Exportation;
import ch.shamu.streaming.media.exporter.FileContentManager;
import ch.shamu.streaming.media.exporter.MediaExporter;
import ch.shamu.streaming.media.exporter.FilePathUtils;
import ch.shamu.streaming.media.exporter.LogStreamUpdate;
import ch.shamu.streaming.media.exporter.WebappConfig;

public class LogAction extends DefaultAction {
	
	@Validate(required=true)
	private String logFileName;

	@Validate(required=true, on="sendCommand")
	private String command;
	
	private Integer fromLine = 0;
	
	private String logContent;
	
	private Boolean stream = true;

	private Exportation exportation;
	
	public String getLogFileName() {
		return logFileName;
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}
	
	public String getDescription() {
		return FilePathUtils.getDescription(getLogFileName());	
	}
	
	@DefaultHandler
	public Resolution showLog() throws FileNotFoundException{
		this.exportation = new Exportation(logFileName);
		this.logContent = Joiner.on("\n").join(FileContentManager.getFileContent(logFileName));
		setFromLine(FileContentManager.getFileContent(logFileName).size());
		return new ForwardResolution("/WEB-INF/jsp/log.jsp");
	}
	
	@HandlesEvent("updateLog")
	public Resolution updateLog() throws JsonGenerationException, JsonMappingException, IOException{
		return new StreamingResolution("text/json", new StringReader(getLogJson()));
	}
	
	@HandlesEvent("sendCommand")
	public Resolution sendCommand() throws IOException{
		FileWriter fstream = null;
	    fstream = new FileWriter("/tmp/groove-input",false);
	    BufferedWriter writer = new BufferedWriter(fstream);
	    writer.append(command);
	    writer.newLine();
	    writer.flush();
	    writer.close();
	    ObjectMapper mapper = new ObjectMapper();
		return new StreamingResolution("text/json", new StringReader(mapper.writeValueAsString(new Boolean(true))));
	}
	
	
	public String getLogJson() throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		LogStreamUpdate res = new LogStreamUpdate();
		List<String> fileContent = FileContentManager.getFileContent(logFileName);
		Integer i = 0;
		// skip the already known lines
		while (fileContent.size() > i && i < fromLine){
			String line = fileContent.get(i);
			if (line.split(":")[0].equals(MediaExporter.COMPLETED_FILE))
				res.done = true;
			i++;
		}
		// read the new lines
		while (fileContent.size() > i){
			String line = fileContent.get(i);
			if (line.split(":")[0].equals(MediaExporter.COMPLETED_FILE))
				res.done = true;
			res.newContent += line;
			res.newContent += "\n";
			i++;
		}
		res.lastLineNumber = i;
		return mapper.writeValueAsString(res);
	}

	public Integer getFromLine() {
		return fromLine;
	}

	public void setFromLine(Integer fromLine) {
		this.fromLine = fromLine;
	}

	public String getLogContent() {
		return logContent;
	}

	public Boolean getStream() {
		return stream;
	}

	public void setStream(Boolean stream) {
		this.stream = stream;
	}

	@ValidationMethod()
	public void checkPaths() {
		FilePathUtils.assertSafe(logFileName);
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Exportation getExportation() {
		return exportation;
	}
	
}
