<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Your data exist some error!</title>
</head>
<body>
<h1>Your data exist some error,you should modify them!!!</h1>
<%-- ${msg} --%>
<c:if test="${not empty errorList}">
	<ul>
	    <c:forEach var="listValue" items="${errorList}">
	        <li>${listValue.getMoreInfo()}</li>
	    </c:forEach>
	</ul>
</c:if>
</body>
</html>