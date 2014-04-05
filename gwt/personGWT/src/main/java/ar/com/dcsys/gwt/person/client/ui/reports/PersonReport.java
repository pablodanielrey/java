package ar.com.dcsys.gwt.person.client.ui.reports;

import java.util.Comparator;
import java.util.Date;

import ar.com.dcsys.data.document.Document;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

public class PersonReport extends Composite implements PersonReportView {

	private static PersonReportUiBinder uiBinder = GWT.create(PersonReportUiBinder.class);

	interface PersonReportUiBinder extends UiBinder<Widget, PersonReport> {
	}

	private Presenter p;

	
	private final ListDataProvider<Document> documents = new ListDataProvider<Document>();
	
	@UiField(provided=true) DataGrid<Document> reports;
	
	
	
	///// necesario por la pedorrada de los handlers de sorts de las cell tables ////////////
	
	private interface FieldProvider<O,T> {
		public T get(O o);
	}
	
	private class DocumentComparator<T> implements Comparator<Document> {

		private final Comparator<T> c;
		private final FieldProvider<Document,T> fp;
		
		public DocumentComparator(FieldProvider<Document,T> fp, Comparator<T> c) {
			this.fp = fp;
			this.c = c;
		}
		
		@Override
		public int compare(Document o1, Document o2) {
			return c.compare(fp.get(o1), fp.get(o2));
		}
		
	}
	
	/////////////////////////////////////////////////
	
	
	private void createReports() {

		
		TextColumn<Document> name = new TextColumn<Document>() {
			@Override
			public String getValue(Document d) {
				if (d == null || d.getName() == null)  {
					return "";
				}
				return d.getName();
			}
		};
		name.setSortable(true);
		
		
		TextColumn<Document> desc = new TextColumn<Document>() {
			@Override
			public String getValue(Document d) {
				if (d == null || d.getDescription() == null)  {
					return "";
				}
				return d.getDescription();
			}
		};
		desc.setSortable(true);
		
		TextColumn<Document> hour = new TextColumn<Document>() {
			
			private final DateTimeFormat df = DateTimeFormat.getFormat("HH:mm:ss");
			
			@Override
			public String getValue(Document d) {
				if (d == null || d.getCreated() == null)  {
					return "";
				}
				return df.format(d.getCreated());
			}
		};
		hour.setSortable(true);

		TextColumn<Document> date = new TextColumn<Document>() {
			
			private final DateTimeFormat df = DateTimeFormat.getFormat("MM/dd/yyyy");
			
			@Override
			public String getValue(Document d) {
				if (d == null || d.getCreated() == null)  {
					return "";
				}
				return df.format(d.getCreated());
			}
		};
		date.setSortable(true);
		
		
		Cell<Document> cid = new AbstractCell<Document>() {
			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,	Document d, SafeHtmlBuilder sb) {
				// para que me genere el link correctamente.
				Anchor a = new Anchor(d.getId(), "/documentWeb/documents?t=" + d.getId());
				String href = a.getHref();
				
				SafeHtml sh = SafeHtmlUtils.fromSafeConstant("<a href=\"" + href + "\">" + d.getId() + "</a>");
				sb.append(sh);
			}
		};
		IdentityColumn<Document> id = new IdentityColumn<Document>(cid);
		id.setSortable(true);
		
		reports = new DataGrid<Document>();
		reports.addColumn(hour, "Hora");
		reports.addColumn(date,"Fecha");
		reports.addColumn(name,"Nombre");
		reports.addColumn(desc,"Descripción");
		reports.addColumn(id,"Reporte");
		
		
		///////// seteo la parte de los sorts de las columnas //////////

		//documents.getDataDisplays().clear();
		documents.addDataDisplay(reports);
		
		ListHandler<Document> documentHandler = new ListHandler<Document>(documents.getList());
		
		Comparator<String> stringComparator = new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if (o1 == null && o2 == null) {
					return 0;
				}
				if (o1 == null) {
					return -1;
				}
				if (o2 == null) {
					return 1;
				}
				return o1.compareTo(o2);
			}
		};
		
		Comparator<Date> dateComparator = new Comparator<Date>() {
			public int compare(Date o1, Date o2) {
				if (o1 == null && o2 == null) {
					return 0;
				}
				if (o1 == null) {
					return -1;
				}
				if (o2 == null) {
					return 1;
				}
				return o1.compareTo(o2);
			};
		};
 
 		
		documentHandler.setComparator(id, new DocumentComparator<String>(
					new FieldProvider<Document,String>() {
						@Override
						public String get(Document o) {
							return o.getId();
						}
					},
					stringComparator
				));

		documentHandler.setComparator(name, new DocumentComparator<String>(
				new FieldProvider<Document,String>() {
					@Override
					public String get(Document o) {
						return o.getName();
					}
				},
				stringComparator
			));
		
		documentHandler.setComparator(desc, new DocumentComparator<String>(
				new FieldProvider<Document,String>() {
					@Override
					public String get(Document o) {
						return o.getDescription();
					}
				},
				stringComparator
			));
		
		documentHandler.setComparator(hour, new DocumentComparator<Date>(
				new FieldProvider<Document,Date>() {
					@Override
					public Date get(Document o) {
						return o.getCreated();
					}
				},
				dateComparator
			));

		documentHandler.setComparator(date, new DocumentComparator<Date>(
				new FieldProvider<Document,Date>() {
					@Override
					public Date get(Document o) {
						return o.getCreated();
					}
				},
				dateComparator
			));
		
		reports.addColumnSortHandler(documentHandler);
		
	}
	

	public PersonReport() {
		createReports();

		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter p) {
		this.p = p;
	}

	@Override
	public void clear() {
		reports.setRowCount(0);
		documents.getList().clear();
		documents.refresh();
	}

	@Override
	public void addReport(Document d) {
		if (d == null) {
			return;
		}
		documents.getList().add(d);
		documents.refresh();
	}
	
	@UiHandler("generate")
	public void onGenerate(ClickEvent event) {
		if (p == null) {
			return;
		}
		p.generateReport();
	}
	
	@UiHandler("update")
	public void onUpdate(ClickEvent event) {
		if (p == null) {
			return;
		}
		p.updateReports();
	}

	
}

