<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix ="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<style>
		#form {
			display: float;
			text-align: center;
		}
		
		#titulo {
			display: float;
			text-align: center;
			font-size: large;
		}
		
		#logout {
			color: red;
			text-align: right;
		}
	</style>
<body>

	<jsp:useBean id="constants" class="ar.com.dcsys.server.assistance.ConstantsBean"/>

	<div id="logout">
		<a href="/asistencia/logout">Salir</a>
	</div> 

	<div id="titulo">
		Reportes
	</div>

	<div id="form">
		<form action="/asistencia/absence" method="POST">
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
				<select name="group">
					<c:forEach var="g" items="${groups}">
						<option value="${g.id}">${g.name}</option>
					</c:forEach>
				</select>
			</div>
			<div>
				Tipo de reporte:
				<select name="periodFilter">
					<option value='<jsp:getProperty name="constants" property="all"/>'>Ausencias (Todas)</option>
					<option value='<jsp:getProperty name="constants" property="injustificatedAbsences"/>'>Ausencias Injustificadas</option>
					<option value='<jsp:getProperty name="constants" property="justificatedAbsences"/>'>Ausencias Justificadas</option>
				</select>
			</div>
			<div>
				Tipo de exportación:
				<select name="exportType">
					<option value="pdf">Pdf</option>
					<option value="xls">Excel</option>
					<option value="docx">Docx</option>
					<option value="csv">Csv</option>
				</select>
			</div>
			<div>
				<input type="submit" value="Generar"/>
			</div>
		</form>
	</div>

</body>
</html>
