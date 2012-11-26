<%@page import="ch.shamu.streaming.media.exporter.action.LogAction"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ taglib uri="http://shamu.ch/expoter/taglib" prefix="fn" %>
<stripes:layout-render name="/WEB-INF/jsp/layout/default.jsp">
    <stripes:layout-component name="contents">
    <div class="row">
    	<div class="offset1 span10">
    		<h1>Export Stuff</h1>
        	<stripes:form class="well form-inline" beanclass="ch.shamu.streaming.media.exporter.action.HomeAction">
        	    <stripes:hidden name="export"></stripes:hidden>
        		<stripes:errors/>
        		<fieldset style="margin-bottom:10px">
	        		<stripes:label for="url">Media URL or "groove:" followed by a search query</stripes:label>
	        		<br>
	        		<stripes:text style="font-size:25px; line-height:30px; width:724px; height:30px" id="url" name="url"></stripes:text>
	        	</fieldset>
	        	<fieldset style="margin-bottom:10px">
	        		<stripes:label for="artist">File name</stripes:label>
	        		<br>
		        	<input style="width:230px" type="text" id="artist" name="artist" placeholder="Artist*"/>
		        	<input style="width:228px" type="text" id="album" name="album" placeholder="Album"/>
		        	<input style="width:230px" type="text" id="title" name="title" placeholder="Title*"/>
		        </fieldset>
		        <fieldset style="margin-bottom:10px">
		        	<label class="checkbox">
		        		<stripes:checkbox name="keepVideo" value="true" checked="checked"/> Keep Video
		        	</label>
		        	<br>
		        	<label class="checkbox" style="margin-top:6px">
		        		<stripes:checkbox name="extractAudio" value="true" checked="checked"/> Extract Audio
		        	</label>  	
        		</fieldset>
        		<button class="btn btn-large btn-primary" name="download">
        			Export
        		</button>
        	</stripes:form>    
    	</div>
    </div>
    <div class="row">
    	<div class="offset1 span10">
	    	<h1>Previous exportations</h1>
	    	<table class="table table-striped table-bordered table-condensed">
	    		<thead>
	    			<tr><th style="width:1px">&nbsp;</th><th style="width:1px">&nbsp;</th><th>File</th><th style="width:1px;white-space:nowrap;color:#999">Time</th></tr>
	    		</thead>
	    		<tbody>
	    		  <c:forEach items="${actionBean.previousExportations}" var="exportation">
	    		  	<tr>
	    		  		<td><c:choose>
	    		  		    <c:when test="${empty exportation.mp3Path}">
	    		  		     &nbsp;
	    		  		    </c:when>
	    		  			<c:otherwise>
	    		  				<stripes:link class="btn btn-small" beanclass="ch.shamu.streaming.media.exporter.action.DownloadAction" >
   								<stripes:param name="filePath">${exportation.mp3Path}</stripes:param><i class="icon-music"></i></stripes:link>
	    		  			</c:otherwise>
	    		  		</c:choose></td>
	    			    <td><c:choose>
	    		  		    <c:when test="${empty exportation.videoPath}">
	    		  		     &nbsp;
	    		  		    </c:when>
	    		  			<c:otherwise>
	    		  				<stripes:link class="btn btn-small" beanclass="ch.shamu.streaming.media.exporter.action.DownloadAction" >
   								<stripes:param name="filePath">${exportation.videoPath}</stripes:param><i class="icon-film"></i></stripes:link>
	    		  			</c:otherwise>
	    		  		</c:choose></td>  
	    		  		<td>
	    		  			<stripes:link beanclass="<%= LogAction.class %>"><stripes:param name="logFileName">${exportation.logFileName}</stripes:param> ${exportation.description}</stripes:link>
	    		  		</td>  
	    		  		<td style="color:#999;white-space:nowrap">
	    		  			${exportation.formattedTime}
	    		  		</td>  				
	    		  	</tr>
	    		  </c:forEach>
	    		</tbody>
	    	</table>
    	</div>
    </div>
    <br>
    <br>
    </stripes:layout-component>
</stripes:layout-render>