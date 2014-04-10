<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix ="c" %>
<html>
<body>

	<jsp:useBean id="constants" class="ar.com.dcsys.server.assistance.ConstantsBean"/>

	<form action="ausencias" method="POST">
		<div>Tipo de reporte</div>
		<div>
			<div>
				Inicio: 
				<input name="start" type="date"/>
			</div>
			<div>
				Fin:
				<input name="end" type="date"/>
			</div>
		</div>
		<div>
			Grupo:
			<div>
				<select name="group">
					<c:forEach var="g" items="${groups}">
						<option value="${g.id}">${g.name}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div>
			<select name="periodFilter">
				<option value='<jsp:getProperty name="constants" property="all"/>'>Ausencias (Todas)</option>
				<option value='<jsp:getProperty name="constants" property="injustificatedAbsences"/>'>Ausencias Injustificadas</option>
				<option value='<jsp:getProperty name="constants" property="justificatedAbsences"/>'>Ausencias Justificadas</option>
			</select>
		</div>
		<div>
			<input type="submit" value="Generar Reporte"/>
		</div>
	</form>

</body>
</html>
