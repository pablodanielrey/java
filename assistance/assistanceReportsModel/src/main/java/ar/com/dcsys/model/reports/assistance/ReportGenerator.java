package ar.com.dcsys.model.reports.assistance;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.model.GroupsManager;
import ar.com.dcsys.model.reports.ReportExportType;

public class ReportGenerator {

	private class ReportQuery {
		Date start;
		Date end;
		ReportType type;
		String group;
		
		public ReportQuery(Date start, Date end, ReportType type, String group) {
			this.start = start;
			this.end = end;
			this.type = type;
			this.group = group;
		}
	}

	private final ReportDataGenerator rdg;
	private GroupsManager groupsManager;
	
	@Inject
	public ReportGenerator(ReportDataGenerator rdg) {
		this.rdg = rdg;
	}
	
	/**
	 * Remuevo los reportes que no cumplen con el tipo de filtro requerido
	 * @param rs
	 * @param reportType
	 */
	private void filterReport(ReportSummary rs, ReportType reportType) {
		
		List<Report> reportsToRemove = new ArrayList<>();

		if (reportType.equals(ReportType.ABSENCES)) {
			
			for (Report r : rs.getReports()) {
				if (!r.isAbscence()) {
					reportsToRemove.add(r);
				}
			}
			
		} else if (reportType.equals(ReportType.INJUSTIFICATED)) {
		
            for (Report r : rs.getReports()) {
            	if (!r.isAbscence()) {
            		
            		reportsToRemove.add(r); 

            	} else if ((r.getJustifications() != null && r.getJustifications().size() > 0) || 
            			  (r.getGjustifications() != null && r.getGjustifications().size() > 0)) {
            		
            		reportsToRemove.add(r);
            	}
            }
            
		} else if (reportType.equals(ReportType.JUSTIFICATED)) {

            for (Report r : rs.getReports()) {
            	if (!r.isAbscence()) {
            		
            		reportsToRemove.add(r);
            		
            	} else if ((r.getJustifications() == null || r.getJustifications().size() <= 0) &&
            			  (r.getGjustifications() == null || r.getGjustifications().size() <= 0)) {
            		
            		reportsToRemove.add(r);
            	}
            }
			
		}
		
		rs.getReports().removeAll(reportsToRemove);
		
	}	
	
	
	private JasperReportBuilder formatReport(ReportSummary rs, ReportQuery query) throws DRException {
		
		ReportType type = query.type;
		String reportType = (ReportType.ABSENCES.equals(type) ? "Todas" : ((ReportType.INJUSTIFICATED.equals(type)) ? "Justificadas" : ((ReportType.INJUSTIFICATED.equals(type)) ? "Injustificadas" : ""))); 
		
		StyleBuilder bold = stl.style().bold();
		StyleBuilder boldCentered = stl.style(bold).setHorizontalAlignment(HorizontalAlignment.CENTER);
		
		StyleBuilder title = stl.style(boldCentered)
								.setVerticalAlignment(VerticalAlignment.MIDDLE)
								.setFontSize(15);
		
		StyleBuilder columnTitle = stl.style(boldCentered)
								.setBorder(stl.pen1Point())
								.setBackgroundColor(Color.LIGHT_GRAY);
		
		
		TextColumnBuilder<String> person = Columns.column("Nombre",".getName", DataTypes.stringType());
		
		JasperReportBuilder report = DynamicReports.report()
				.title(cmp.verticalList()
							.add(cmp.text("Ausencias").setStyle(title))
							.add(cmp.horizontalList()
									.add(cmp.text("Fecha Reporte :"))
									.add(cmp.currentDate())
									)
							.add(cmp.horizontalList()
									.add(cmp.text("Tipo Reporte"))
									.add(cmp.text(reportType))
									)
							.add(cmp.horizontalList()
									.add(cmp.text("Inicio"))
									.add(cmp.text(query.start))
									)
							.add(cmp.horizontalList()
									.add(cmp.text("Fin"))
									.add(cmp.text(query.end))
									)
							.add(cmp.horizontalList()
									.add(cmp.text("Grupo"))
									.add(cmp.text(query.group))
									)
							.add(cmp.filler().setFixedHeight(10))
							)
				.setColumnTitleStyle(columnTitle)
				.highlightDetailEvenRows()
				.columns(
						person,
						Columns.column("Fecha",".getDate",DataTypes.dateType())
							.setPattern("dd/MM/yyyy"),
						Columns.column("Justificación",".getJustification",DataTypes.stringType()),
						Columns.column("Justificación General", ".getGeneralJustification", DataTypes.stringType())
				)
				.groupBy(person)
				.setDataSource(new ReportDataSource(rs));

			return report;
	}
	
	private void exportReport(JasperReportBuilder report, ReportExportType type, OutputStream out) throws IOException, DRException {
		if (ReportExportType.PDF.equals(type)) {
			report.toPdf(out);
		} else if (ReportExportType.EXCEL.equals(type)) {
			report.toExcelApiXls(out);
		} else if (ReportExportType.DOCX.equals(type)) {
			report.toDocx(out);
		} else {
			report.toCsv(out);
		}		
	}	
	
	
	

	
	
	
	public void reportAbsences(OutputStream out, Date start, Date end, Group group, ReportExportType type) throws IOException {
		
		String name = group.getName() == null ? "Sin nombre" : group.getName();
		ReportQuery query = new ReportQuery(start,end,ReportType.ABSENCES,name);
		

		List<Person> persons = group.getPersons();
		ReportSummary rs = rdg.getReport(start, end, persons);
		filterReport(rs, ReportType.ABSENCES);
		
		try {
			JasperReportBuilder report = formatReport(rs, query);
			exportReport(report, type, out);

		} catch (DRException e) {
			throw new IOException(e);
		}
	}
	
	public void reportInjustificatedAbsences(OutputStream out, Date start, Date end, Group group, ReportExportType type) throws IOException {

		String name = group.getName() == null ? "Sin nombre" : group.getName();
		ReportQuery query = new ReportQuery(start,end,ReportType.INJUSTIFICATED,name);

		List<Person> persons = group.getPersons();
		ReportSummary rs = rdg.getReport(start, end, persons);
		filterReport(rs, ReportType.INJUSTIFICATED);
		
		try {
			JasperReportBuilder report = formatReport(rs, query);
			exportReport(report, type, out);
		} catch (DRException e) {
			throw new IOException(e);
		}
	}
	
	public void reportJustificatedAbsences(OutputStream out, Date start, Date end, Group group, ReportExportType type) throws IOException {

		String name = group.getName() == null ? "Sin nombre" : group.getName();
		ReportQuery query = new ReportQuery(start,end,ReportType.JUSTIFICATED,name);

		List<Person> persons = group.getPersons();
		ReportSummary rs = rdg.getReport(start, end, persons);
		filterReport(rs, ReportType.JUSTIFICATED);
		
		try {
			JasperReportBuilder report = formatReport(rs, query);
			exportReport(report, type, out);
		} catch (DRException e) {
			throw new IOException(e);
		}
	}

	
}
