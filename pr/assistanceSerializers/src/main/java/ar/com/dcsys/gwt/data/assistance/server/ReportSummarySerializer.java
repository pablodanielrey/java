package ar.com.dcsys.gwt.data.assistance.server;

import java.util.logging.Logger;

import ar.com.dcsys.data.report.ReportSummary;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ReportSummarySerializer implements CSD<ReportSummary> {

	public static final Logger logger = Logger.getLogger(ReportSummarySerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
	
	@Override
	public String toJson(ReportSummary o) {
		String d = gson.toJson(o);
		logger.warning(d);
		return d;
	}

	@Override
	public ReportSummary read(String json) {
		logger.warning(json);
		ReportSummary rs = gson.fromJson(json, ReportSummary.class);
		return rs;
	}

}
