package ar.com.dcsys.model.reports;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.awt.Color;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.GroupsManager;
import ar.com.dcsys.model.PersonsManager;

@Singleton
public class ReportsGenerator {

	private final PersonsManager personsManager;
	private final GroupsManager groupsManager;
	
	@Inject
	public ReportsGenerator(PersonsManager personsManager, GroupsManager groupsManager) {
		this.personsManager = personsManager;
		this.groupsManager = groupsManager;
	}
	
	
	private List<Report> generateReports(List<Person> persons) throws PersonException {
		List<Report> rs = new ArrayList<>();
		for (Person p : persons) {
			List<Group> groups = groupsManager.findByPerson(p);
			Report r = new Report();
			r.setPerson(p);
			r.setGroups(groups);
			rs.add(r);
		}
		return rs;
	}

	private List<Report> generateAllReports() throws PersonException {
		List<Person> persons = personsManager.findAll();
		if (persons == null || persons.size() <= 0) {
			throw new PersonException("ninguna persona existente");
		}
		
		List<Report> reports = generateReports(persons);
		return reports;
	}
	
	public synchronized void reportPersons(OutputStream out) throws PersonException {
		
		List<Report> reports = generateAllReports();
		Collections.sort(reports,namesComparator);
		
		try {
			
			StyleBuilder bold = stl.style().bold();
			StyleBuilder boldCentered = stl.style(bold).setHorizontalAlignment(HorizontalAlignment.CENTER);
			
			StyleBuilder title = stl.style(boldCentered)
									.setVerticalAlignment(VerticalAlignment.MIDDLE)
									.setFontSize(15);			
			
			StyleBuilder columnTitle = stl.style(boldCentered)
					.setBorder(stl.pen1Point())
					.setBackgroundColor(Color.LIGHT_GRAY);			
			
			
			DynamicReports.report()
				.title(cmp.verticalList()
						.add(cmp.text("Datos Personales").setStyle(title))
						.add(cmp.horizontalList()
								.add(cmp.text("Fecha Reporte :"))
								.add(cmp.currentDate())
								)
						.add(cmp.filler().setFixedHeight(10))
						)
						.setColumnTitleStyle(columnTitle)
						.highlightDetailEvenRows()						
						.columns(
//							col.column("Id","getId",DataTypes.stringType()),
							col.column("Apellido","getLastName",DataTypes.stringType()),
							col.column("Nombre","getName",DataTypes.stringType()),
							col.column("Dni","getDni",DataTypes.stringType()),
							col.column("Oficinas","getOffices",DataTypes.stringType()),
							col.column("Cargos","getPositions",DataTypes.stringType())
						)
				.setDataSource(new ListBeanDataSource<Report>(reports))
				.toPdf(out);
		
		} catch (Exception e) {
			throw new PersonException(e);
		}		
	}
	
	
	
	
	private static final Comparator<Report> positionsComparator = new Comparator<Report>() {
		@Override
		public int compare(Report r1, Report r2) {
			if (r1 == null && r2 == null) {
				return 0;
			}
			if (r1 == null) {
				return -1;
			}
			if (r2 == null) {
				return 1;
			}
			int cmp = r1.getPositions().compareTo(r2.getPositions());
			if (cmp != 0) {
				return cmp;
			}

			return namesComparator.compare(r1, r2);
		}
	};   
	
	
	private static final Comparator<Report> namesComparator = new Comparator<Report>() {
		@Override
		public int compare(Report r1, Report r2) {
			if (r1 == null && r2 == null) {
				return 0;
			}
			if (r1 == null) {
				return -1;
			}
			if (r2 == null) {
				return 1;
			}
			
			int cmp = r1.getLastName().compareTo(r2.getLastName());
			if (cmp != 0) {
				return cmp;
			}
			
			cmp = r1.getName().compareTo(r1.getName());
			if (cmp != 0) {
				return cmp;
			}
			
			cmp = r1.getDni().compareTo(r2.getDni());
			return cmp;				
		}
	};  
	
	private static final Comparator<Report> officeComparator = new Comparator<Report>() {
		@Override
		public int compare(Report r1, Report r2) {
			if (r1 == null && r2 == null) {
				return 0;
			}
			if (r1 == null) {
				return -1;
			}
			if (r2 == null) {
				return 1;
			}
			
			int cmp = r1.getOffices().compareTo(r2.getOffices());
			if (cmp != 0) {
				return cmp;
			}
			
			return namesComparator.compare(r1, r2);
		}
	}; 
	
	
	
	
	public synchronized void reportPersonsByOffice(OutputStream out) throws PersonException {
		
		List<Report> reports = generateAllReports();
		Collections.sort(reports,officeComparator);
		
		try {
			
			StyleBuilder bold = stl.style().bold();
			StyleBuilder boldCentered = stl.style(bold).setHorizontalAlignment(HorizontalAlignment.CENTER);
			
			StyleBuilder title = stl.style(boldCentered)
									.setVerticalAlignment(VerticalAlignment.MIDDLE)
									.setFontSize(15);			
			
			StyleBuilder columnTitle = stl.style(boldCentered)
					.setBorder(stl.pen1Point())
					.setBackgroundColor(Color.LIGHT_GRAY);			
			
			
			TextColumnBuilder<String> office = col.column("Oficinas","getOffices",DataTypes.stringType());
			ColumnGroupBuilder officeG = grp.group(office)
						.groupByDataType()
						.addFooterComponent(cmp.pageBreak());
			
			DynamicReports.report()
				.title(cmp.verticalList()
						.add(cmp.text("Datos Personales").setStyle(title))
						.add(cmp.horizontalList()
								.add(cmp.text("Fecha Reporte :"))
								.add(cmp.currentDate())
								)
						.add(cmp.filler().setFixedHeight(10))
						)
						.setColumnTitleStyle(columnTitle)
						.highlightDetailEvenRows()						
						.columns(
//							col.column("Id","getId",DataTypes.stringType()),
							col.column("Apellido","getLastName",DataTypes.stringType()),
							col.column("Nombre","getName",DataTypes.stringType()),
							col.column("Dni","getDni",DataTypes.stringType()),
							office,
							col.column("Cargos","getPositions",DataTypes.stringType())
						)
				.groupBy(officeG)
				.setDataSource(new ListBeanDataSource<Report>(reports))
				.toPdf(out);
		
		} catch (Exception e) {
			throw new PersonException(e);
		}		
	}	
	
	public synchronized void reportPersonsByPosition(OutputStream out) throws PersonException {
		
		List<Report> reports = generateAllReports();
		Collections.sort(reports,positionsComparator);		
		
		try {
			
			StyleBuilder bold = stl.style().bold();
			StyleBuilder boldCentered = stl.style(bold).setHorizontalAlignment(HorizontalAlignment.CENTER);
			
			StyleBuilder title = stl.style(boldCentered)
									.setVerticalAlignment(VerticalAlignment.MIDDLE)
									.setFontSize(15);			
			
			StyleBuilder columnTitle = stl.style(boldCentered)
					.setBorder(stl.pen1Point())
					.setBackgroundColor(Color.LIGHT_GRAY);			
			
			
			TextColumnBuilder<String> position = col.column("Cargos","getPositions",DataTypes.stringType());
			ColumnGroupBuilder positionG = grp.group(position)
					.groupByDataType()
					.addFooterComponent(cmp.pageBreak());			
			
			DynamicReports.report()
				.title(cmp.verticalList()
						.add(cmp.text("Datos Personales").setStyle(title))
						.add(cmp.horizontalList()
								.add(cmp.text("Fecha Reporte :"))
								.add(cmp.currentDate())
								)
						.add(cmp.filler().setFixedHeight(10))
						)
						.setColumnTitleStyle(columnTitle)
						.highlightDetailEvenRows()						
						.columns(
//							col.column("Id","getId",DataTypes.stringType()),
							col.column("Apellido","getLastName",DataTypes.stringType()),
							col.column("Nombre","getName",DataTypes.stringType()),
							col.column("Dni","getDni",DataTypes.stringType()),
							col.column("Oficinas","getOffices",DataTypes.stringType()),
							position
						)
				.groupBy(positionG)
				.setDataSource(new ListBeanDataSource<Report>(reports))
				.toPdf(out);
		
		} catch (Exception e) {
			throw new PersonException(e);
		}		
	}		
	
}
