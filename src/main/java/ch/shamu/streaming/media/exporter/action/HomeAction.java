package ch.shamu.streaming.media.exporter.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;
import ch.shamu.streaming.media.exporter.DefaultAction;
import ch.shamu.streaming.media.exporter.Exportation;
import ch.shamu.streaming.media.exporter.MediaExporter;
import ch.shamu.streaming.media.exporter.FilePathUtils;
import ch.shamu.streaming.media.exporter.WebappConfig;

public class HomeAction extends DefaultAction {
	
	@Validate(required=true, on="download")
	private String url;
	
	private String album;
	
	@Validate(required=true, on="download")
	private String artist;
	
	@Validate(required=true, on="download")
	private String title;
	
	private Integer numExportations = 20;
	
	private Boolean keepVideo = false;
	private Boolean extractAudio = true;
	
	@DefaultHandler
	public Resolution index(){
		return new ForwardResolution("/WEB-INF/jsp/index.jsp");
	}
	
	@HandlesEvent("export")
	public Resolution export() throws IOException{
		String id = artist;
		if (album != null && !"".equals(album)){
			id += "/"+album;
		}
		id += "/"+title;
		MediaExporter.getInstance().run(url, id, extractAudio, keepVideo);
		return new RedirectResolution(LogAction.class).addParameter("logFileName",id.replace("/", "_")+".log");
	}

	@ValidationMethod(on="download")
	public void checkPaths() {
		if (album != null){
			FilePathUtils.assertSafe(album);
		}
		FilePathUtils.assertSafe(artist);
		FilePathUtils.assertSafe(title);
	}
	
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getKeepVideo() {
		return keepVideo;
	}

	public void setKeepVideo(Boolean keepVideo) {
		this.keepVideo = keepVideo;
	}

	public Boolean getExtractAudio() {
		return extractAudio;
	}

	public void setExtractAudio(Boolean extractAudio) {
		this.extractAudio = extractAudio;
	}
	
	public List<Exportation> getPreviousExportations() throws UnsupportedEncodingException, FileNotFoundException{
		List<Exportation> res = new ArrayList<Exportation>();
		File folder = new File(WebappConfig.get("log.dir")+"/downloads");
	    File[] listOfFiles = folder.listFiles();
	    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        res.add(new Exportation(listOfFiles[i].getName()));
		      }
		    }
	    Collections.sort(res, new Comparator<Exportation>(){
	        public int compare(Exportation e1, Exportation e2)
	        {
	            return Long.valueOf(e2.getTime().getTime()).compareTo(e1.getTime().getTime());
	        } });
	    
	    return res;
	}

	public Integer getNumExportations() {
		return numExportations;
	}

	public void setNumExportations(Integer numExportations) {
		this.numExportations = numExportations;
	}
	
}
