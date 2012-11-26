<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<stripes:layout-definition>
<!DOCTYPE html>
<html>
    <head>
        <title>Export media from teh interwebs</title>
        <link rel="stylesheet"
              type="text/css"
              href="${pageContext.request.contextPath}/css/bootstrap.cerulean.min.css"/>
        <link rel="stylesheet"
              type="text/css"
              href="${pageContext.request.contextPath}/css/default.css"/>
        <script  type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
        <script  type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
        <stripes:layout-component name="html_head"/>
    </head>
    <body>
    	<div class="container">
    	  <stripes:layout-component name="contents"/>
    	</div>  
    </body>
</html>
</stripes:layout-definition>