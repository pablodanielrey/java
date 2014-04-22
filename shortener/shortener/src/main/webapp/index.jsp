<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix ="c" %>
<html>
<body>

	<c:if test="${urls != null}">
		<div>
			Redirecciones Generadas:
			<div>
				<c:forEach var="u" items="${urls}">
					<div>
						<a href="${u.url}">${u.url}</a>  ---> ${u.redir}
					</div>
				</c:forEach>
			</div>
		</div>
	</c:if>

	<div>
		<div>Acortamiento de Direcciones de la FCE</div>
		<div>
			<form action="shorten" method="POST">
				<div>
					<div>
						Url: 
						<input name="url" type="url"/>
					</div>
				</div>
				<div>
					<input type="submit" value="Generar Redirección"/>
				</div>
			</form>
		</div>
	</div>
	
</body>
</html>
