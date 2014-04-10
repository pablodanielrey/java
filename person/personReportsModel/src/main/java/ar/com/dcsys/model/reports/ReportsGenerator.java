package ar.com.dcsys.model.reports;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.awt.Color;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
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
	
	public synchronized void reportPersons(OutputStream out) throws PersonException {
		List<Person> persons = personsManager.findAll();
		if (persons == null || persons.size() <= 0) {
			throw new PersonException("ninguna persona existente");
		}
		
		List<Report> reports = generateReports(persons);
		
		
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
							col.column("Nombre","getName",DataTypes.stringType()),
							col.column("Apellido","getLastName",DataTypes.stringType()),
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
	
}
