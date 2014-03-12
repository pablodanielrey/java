package ar.com.dcsys.server.person;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public class ServletExportCSV {

	public static PrintWriter getWriter(String fileName, HttpServletResponse resp) throws IOException {
		
		SimpleDateFormat dateF = new SimpleDateFormat("dd-MM-YY");
		SimpleDateFormat hourF = new SimpleDateFormat("HH:mm:ss");
		
		Date now = new Date();
		resp.addHeader("Content-Disposition", "attachment;filename=" + fileName + "-" + hourF.format(now) + "-de-" + dateF.format(now) + ".csv");
		resp.addHeader("Content-Type", "text/csv; charset=UTF-8");
		
		return new PrintWriter(resp.getOutputStream());
	}
	
}
