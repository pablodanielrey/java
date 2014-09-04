<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix ="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<c:if test="${refresh != null}">
		<meta http-equiv="refresh" content="2">
	</c:if>
	<style>
		ul li {
		  list-style: none;
		  float:left;
		  width:100%;
		  margin-bottom:0.5em;
		}
				
		.itemImpar div{
		  background-color:#ccc;
		}
		
		.itemPar div{
		  background-color:#eee;
		}
		
		.col1,.col2,.col3,.col4 {
		  float:left;
		  width:6em;
		  height:1.5em;
		  
		  margin-left:0.5em;
		}
		
		.col4 {
		    text-align:center;
		}
		
		.col4 button {
		    cursor:pointer;
		}
	</style>
</head>
<body>
	
	<div>
		<form action="devices" method="post">
			<input type="hidden" name="function" value="getUsers"/> 
			<button type="submit"/>Actualizar Usuarios</button>
		</form>
	</div>
	
	<div>
		<c:if test="${messages != null}">${messages}</c:if>
	</div>



	<div id="users">
	
		<form id="enroll" action="devices" method="post">
			<input type="hidden" name="function" value="enroll"/>
	
			<ul>
				<c:forEach var="user" items="${userList}" varStatus="status">
					<c:choose>
						<c:when test="${status.count % 2 == 0}"><li class="itemPar"></c:when>
						<c:when test="${status.count % 2 != 0}"><li class="itemImpar"></c:when>
					</c:choose>
						<div>
							<div class="col1">${user.dni}</div>
							<div class="col2">${user.name}</div>
							<div class="col3">${user.lastName}</div>
							<div class="col4"><button form="enroll" name="personId" value="${user.id}">Enrolar</button></div>
						</div>
					</li>
				</c:forEach>
			</ul>
		
		</form>		
		
	</div>
	
</body>
</html>
