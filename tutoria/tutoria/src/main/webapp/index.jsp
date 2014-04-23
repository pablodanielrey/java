<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix ="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<body>
	<div>
		<div><a href="logout">salir</a></div>
		<div>Ingreso de datos de tutorías</div>
		<div>
			<form action="index" method="POST">
			
				<div>
					Fecha:
					<input type="date" name="date">
				</div>
				<div>
					Legajo:
					<input name="studentNumber"/>
				</div>
				<div>
					Tipo:
					<select name="situation">
						<c:forEach var="u" items="${situations}">
							<option value="${u}">${u}</option>
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
