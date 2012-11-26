<%@page import="ch.shamu.streaming.media.exporter.action.HomeAction"%>
<%@page import="ch.shamu.streaming.media.exporter.action.LogAction"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 

<stripes:layout-render name="/WEB-INF/jsp/layout/default.jsp">
    <stripes:layout-component name="contents">
    	<H1>${actionBean.exportation.description}</H1>
    	
<c:set var="preclass" value=""/>
<c:if test="${actionBean.exportation.running}">
	<c:set var="preclass" value="streaming"/>
</c:if>
<pre class="${preclass}" id="logContent">
${actionBean.logContent}    
</pre>
   <script type="text/javascript">
   function toBottom()
   {
   	 window.scrollTo(0, document.body.scrollHeight);
   	 $("#command").focus();
   }
   $(function(){
	   toBottom();
   });


   </script>
   <c:if test="${actionBean.exportation.running}">
   		<img class="working" src="${pageContext.request.contextPath}/img/working.gif"/>
    	<script type="text/javascript">
    		var lastLineNumber = ${actionBean.fromLine};
    		var interval = setInterval(function(){
    			$.ajax({
    				url:'${pageContext.request.contextPath}/Log.action',
    				data:{"updateLog":"","logFileName":"${actionBean.logFileName}","fromLine":lastLineNumber},
    				dataType:"json",
    				success: function(data, textStatus, jqXHR){
    					if (data.newContent != ""){
    						$("#logContent").append(data.newContent);
        					toBottom();
    					}
    					lastLineNumber = data.lastLineNumber;	
    					if (data.done)
    						location.reload();
    				}
    			});
    			
    		},1000);
    		function sendCommand() {
                $.ajax({
    				url:'${pageContext.request.contextPath}/Log.action',
    				data: $("#sendCommand").serialize(),
    				dataType:"json",
    				success: function(data, textStatus, jqXHR){
    					
    				}
    			});
                $("#command").val("");
                return false;
            }
    	</script>
   <stripes:form id="sendCommand" beanclass="<%=LogAction.class%>" onsubmit="return false;">
     <stripes:hidden name="sendCommand" value=""/>
  	 <stripes:hidden name="logFileName" value="${actionBean.logFileName}"/>
   	 <stripes:text onkeypress="if (event.keyCode == 13) sendCommand();" id="command" name="command"></stripes:text>
   	 <button class="btn sendCommand" onclick="window.sendCommand();">Send</button>
   </stripes:form>
   <br>
   </c:if>
   <c:if test="${not empty actionBean.exportation.videoPath}">
   	<stripes:link class="btn btn-large btn-primary" beanclass="ch.shamu.streaming.media.exporter.action.DownloadAction" >
   	<stripes:param name="filePath">${actionBean.exportation.videoPath}</stripes:param>
   	<i class="icon-film icon-white"></i>&nbsp;Download Video</stripes:link> &nbsp;
   </c:if>
   <c:if test="${not empty actionBean.exportation.mp3Path}">
   	<stripes:link class="btn btn-large btn-primary" beanclass="ch.shamu.streaming.media.exporter.action.DownloadAction" >
   	<stripes:param name="filePath">${actionBean.exportation.mp3Path}</stripes:param>
   	<i class="icon-music icon-white"></i>&nbsp;Download MP3</stripes:link> &nbsp;
   </c:if>
   <stripes:link class="btn btn-large" beanclass="<%= HomeAction.class %>" >Back</stripes:link>
  
   <br><br>
   </stripes:layout-component>
</stripes:layout-render>