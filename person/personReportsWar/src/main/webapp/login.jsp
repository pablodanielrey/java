<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix ="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<style>
		#login {
			display: float;
			text-align: center;
		}
	</style>
</head>
<body>
	
	<div id="login">
		<form name="loginform" action="" method="post">
			<table align="left" border="0" cellspacing="0" cellpadding="3">
			    <tr>
			        <td>Usuario:</td>
			        <td><input type="text" name="j_username" maxlength="30"></td>
			    </tr>
			    <tr>
			        <td>Clave:</td>
			        <td><input type="password" name="j_password" maxlength="30"></td>
			    </tr>
			    <tr>
			        <td colspan="2" align="right"><input type="submit" name="submit" value="Login"></td>
			    </tr>
			</table> 
		</form>	
	</div>
	
</body>
</html>
