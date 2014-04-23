<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix ="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<meta charset="utf-8">
	<style>
		#message {
			color: red;
		}
		
		#logout {
			display: float;
			text-align: right;
		}
		
		#title {
			font-size: larger;
			text-align: center;
		}
		
		#form {
			margin-top: 30px;
			text-align: center;
		}
	</style>
</head>
<body>
	<div>
		<div id="logout"><a href="logout">Salir</a></div>
		<div id="title">Ingreso de datos de tutorías</div>
		
		<c:if test="${message != null}">
			<div id="message">
				${message}
			</div>
		</c:if>
		
		
		<div id="form">
			<form action="index" method="POST" accept-charset="UTF-8">
			
				<div>
					Fecha:
					<input type="date" name="date" value="${date}">
				</div>
				<div>
					Legajo:
					<input name="studentNumber"/>
				</div>
				<div>
					Tipo:
					<select name="situation">
						<c:forEach var="u" items="${situations}" varStatus="loop">
							<option value="${loop.index}">${u}</option>
						</c:forEach>
					</select>
				</div>

				<div>
					<input type="submit" value="aceptar"/>
				</div>

			</form>
		</div>
	</div>
	
</body>
</html>
