<html>
<body>

	<jsp:useBean id="constants" class="ar.com.dcsys.server.person.ConstantsBean"/>

	<form action="reporte">
		<div>Tipo de reporte</div>
		<div>
			<select name="reporte">
				<option value='<jsp:getProperty name="constants" property="all"/>'>Ausencias (Todas)</option>
				<option value='<jsp:getProperty name="constants" property="injustificatedAbsences"/>'>Ausencias Injustificadas</option>
				<option value='<jsp:getProperty name="constants" property="justificatedAbsences"/>'>Ausencias Justificadas</option>
			</select>
		</div>
		<div>
			<input type="submit" value="Generar Reporte"/>
		</div>
	</form>
	
	
	<%
		String type = request.getParameter("reporte");
		
		if (type == null) {
			%>
				NULL
			<%
		} else if (type.equals("a")) {
			%>
				TODAS LAS AUSENCIAS
			<%
		} else if (type.equals("ai")) {
			%>
				INJUSTIFICADAS
			<%
		} else if (type.equals("aj")) {
			%>
				JUSTIFICADAS
			<%
		};
	%>
	

</body>
</html>
