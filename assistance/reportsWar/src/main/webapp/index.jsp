<html>
<body>

	<form action="index.jsp">
		<div>Tipo de reporte</div>
		<div>
			<select name="reporte">
				<option value="a">Ausencias (Todas)</option>
				<option value="ai">Ausencias Injustificadas</option>
				<option value="aj">Ausencias Justificadas</option>
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
