package ar.com.dcsys.pr.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.data.document.DocumentBean;
import ar.com.dcsys.data.group.GroupBean;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationBean;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.justification.JustificationDateBean;
import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodAssignationBean;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailBean;
import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.MailChangeBean;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonBean;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.shared.event.SocketMessageEvent;
import ar.com.dcsys.gwt.ws.shared.event.SocketMessageEventHandler;
import ar.com.dcsys.gwt.ws.shared.event.SocketStateEvent;
import ar.com.dcsys.gwt.ws.shared.event.SocketStateEventHandler;
import ar.com.dcsys.pr.client.gin.Injector;
import ar.com.dcsys.pr.shared.TestManager;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProcessorTest implements EntryPoint {

	private final static Logger logger = Logger.getLogger(ProcessorTest.class.getName());
	
	private final Injector injector = GWT.create(Injector.class);
	
	private VerticalPanel vp = new VerticalPanel();
	private TestManager tm;
	
	@Override
	public void onModuleLoad() {
		
		final WebSocket ws = injector.getWebSocket();
	
		EventBus bus = injector.getEventBus();

		bus.addHandler(SocketMessageEvent.TYPE, new SocketMessageEventHandler() {
			@Override
			public void onMessage(String msg) {
				logger.log(Level.INFO, msg);
			}
		});
		
		
		try {
			tm = injector.testManagerProvider().get();
			tm.setTransport(ws);

			bus.addHandler(SocketStateEvent.TYPE, new SocketStateEventHandler() {
				@Override
				public void onOpen() {
					logger.log(Level.INFO,"socket abierto");
				}
				
				@Override
				public void onClose() {
					logger.log(Level.INFO,"socket cerrado");
				}
			});
			
			Button close = new Button("Close WebSocket");
			close.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					try {
						ws.close();
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage());
					}
				}
			});

			Button open = new Button("Open WebSocket");
			open.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					try {
						ws.open();
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage());
					}
				}
			});

			RootPanel.get().add(vp);		
			
			vp.add(open);
			vp.add(close);
			
			test();
			test1();
			test2();
			test3();
			test4();
			test5();
			test6();
			test7();
			test8();
			test9();
			test10();
			
			test20();
			test21();
			test22();

			test30();
			test31();
			test32();
			test33();

			test34();
			test35();
			test36();
			test37();
			test38();

			test40();
			test41();
			test42();
			test43();
			
			
			test50();
			test51();
			test52();
			test53();
			
			testEnum();
			testEnum2();
			testEnum3();
			testEnum4();
			
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
		
	}
	

	
	private void testEnum() {
		
		Button b = new Button("testEnum()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					tm.testEnum(new Receiver<PersonType>() {
						@Override
						public void onSuccess(PersonType t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}

	private void testEnum2() {
		
		Button b = new Button("testEnum2()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					tm.testEnum2(PersonType.EXTERNAL,new Receiver<PersonType>() {
						@Override
						public void onSuccess(PersonType t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}

	private void testEnum3() {
		
		Button b = new Button("testEnum3()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					Person p = new PersonBean();
					p.setDni("1");
					PersonType pt = PersonType.POSTGRADUATE;
					
					tm.testEnum3(p,pt,new Receiver<PersonType>() {
						@Override
						public void onSuccess(PersonType t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}	
	
	
	private void testEnum4() {
		
		Button b = new Button("testEnum4()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					Person p = new PersonBean();
					
					tm.testEnum4("algo",PersonType.STUDENT,new Receiver<PersonType>() {
						@Override
						public void onSuccess(PersonType t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}	
	
	
	private void test() {
		
		Button b = new Button("test()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					tm.test(new Receiver<String>() {
						@Override
						public void onSuccess(String t) {
							logger.log(Level.INFO,t);
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}

	private void test1() {
		
		Button b = new Button("test1()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					Person p = new PersonBean();
					p.setDni("1");
					p.setName("pablo");
					p.setLastName("rey");
					
					tm.test1(p,new Receiver<String>() {
						@Override
						public void onSuccess(String t) {
							logger.log(Level.INFO,t);
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}	
	
	private void test2() {
		
		Button b = new Button("test2()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					tm.test2(new Receiver<List<String>>() {
						@Override
						public void onSuccess(List<String> t) {
							for (String ts : t) {
								logger.log(Level.INFO,ts);
							}
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}	
	
	private void test3() {
		
		Button b = new Button("test3()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					tm.test3("ejecutando test3",new Receiver<String>() {
						@Override
						public void onSuccess(String t) {
							logger.log(Level.INFO,t);
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}		

	
	private void test4() {
		
		Button b = new Button("test4()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					List<String> s = new ArrayList<String>();
					s.add("1");
					s.add("2");
					tm.test4(s,new Receiver<String>() {
						@Override
						public void onSuccess(String t) {
							logger.log(Level.INFO,t);
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}			
	
	
	private void test5() {
		
		Button b = new Button("test5()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					List<Person> s = new ArrayList<Person>();
					
					Person p = new PersonBean();
					p.setDni("1");
					s.add(p);

					p = new PersonBean();
					p.setDni("2");
					s.add(p);

					p = new PersonBean();
					p.setDni("3");
					s.add(p);
					
					tm.test5(s,new Receiver<String>() {
						@Override
						public void onSuccess(String t) {
							logger.log(Level.INFO,t);
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}				
	
	
	
	private void test6() {
		
		Button b = new Button("test6()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					tm.test6(new Receiver<List<Person>>() {
						@Override
						public void onSuccess(List<Person> t) {
							for (Person ts : t) {
								logger.log(Level.INFO,ts.getDni());
							}
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}	
	
	
	
	private void test7() {
		
		Button b = new Button("test7()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					tm.test7("1212",new Receiver<Person>() {
						@Override
						public void onSuccess(Person t) {
							logger.log(Level.INFO,t.getDni());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}		
	
	
	private void test8() {
		
		Button b = new Button("test8()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					tm.test8("2121",new Receiver<List<Person>>() {
						@Override
						public void onSuccess(List<Person> t) {
							for (Person ts : t) {
								logger.log(Level.INFO,ts.getDni());
							}
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}

	
	
	private void test9() {
		
		Button b = new Button("test9()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					tm.test9(new Date(),new Receiver<List<Date>>() {
						@Override
						public void onSuccess(List<Date> t) {
							for (Date ts : t) {
								logger.log(Level.INFO,ts.toString());
							}
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}
		
	
	
	private void test10() {
		
		Button b = new Button("test10()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					tm.test10(true,new Receiver<Boolean>() {
						@Override
						public void onSuccess(Boolean b) {
							logger.log(Level.INFO,b.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}	
	
	
	
	private void test20() {
		
		Button b = new Button("test20()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					MailBean m = new MailBean();
					m.setMail("mail@mail.com");
					
					tm.test20(m,new Receiver<Mail>() {
						@Override
						public void onSuccess(Mail t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}
	
	private void test21() {
		
		Button b = new Button("test21()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					MailBean m = new MailBean();
					m.setMail("mail@mail.com");
					m.setPrimary(true);
					
					tm.test21(m,new Receiver<List<Mail>>() {
						@Override
						public void onSuccess(List<Mail> t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}
	
	private void test22() {
		
		Button b = new Button("test22()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					MailBean m = new MailBean();
					m.setMail("mail@mail.com");
					m.setPrimary(true);
					
					List<Mail> l = new ArrayList<Mail>();
					l.add(m);
					
					tm.test22(l,new Receiver<List<Mail>>() {
						@Override
						public void onSuccess(List<Mail> t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}
	
	
	
	private void test30() {
		
		Button b = new Button("test30()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					DocumentBean d = new DocumentBean();
					d.setId("dfdfdsfdsfdsf");
					d.setDescription("descripcion");
					d.setMimeType("aalgo mime");
					d.setName("petenombre");
					
					tm.test30(d,new Receiver<Document>() {
						@Override
						public void onSuccess(Document t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}
	
	private void test31() {
		
		Button b = new Button("test31()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					DocumentBean d = new DocumentBean();
					d.setId("dfdfdsfdsfdsf");
					d.setDescription("descripcion");
					d.setMimeType("aalgo mime");
					d.setName("petenombre");
					
					tm.test31(d,new Receiver<List<Document>>() {
						@Override
						public void onSuccess(List<Document> t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}
	
	private void test32() {
		
		Button b = new Button("test32()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					DocumentBean d = new DocumentBean();
					d.setId("dfdfdsfdsfdsf");
					d.setDescription("descripcion");
					d.setMimeType("aalgo mime");
					d.setName("petenombre");
					
					tm.test32(Arrays.asList((Document)d),new Receiver<List<Document>>() {
						@Override
						public void onSuccess(List<Document> t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}
	
	
	private void test33() {
		
		Button b = new Button("test33()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					tm.test33(null,new Receiver<Void>() {
						@Override
						public void onSuccess(Void t) {
							if (t == null) {
								logger.log(Level.INFO,"null");
							} else {
								logger.log(Level.INFO,t.toString());
							}
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}	
	
	
	private void test34() {
		
		Button b = new Button("test34()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					MailBean mb = new MailBean();
					mb.setMail("pablo@econo");
					
					MailChangeBean mc = new MailChangeBean();
					mc.setConfirmed(false);
					mc.setMail(mb);
					mc.setPersonId("dsfdfdfdf");
					mc.setToken("sdfdf3434fgwc423f4fewf");
					
					tm.test34(mc,new Receiver<Void>() {
						@Override
						public void onSuccess(Void t) {
							if (t == null) {
								logger.log(Level.INFO,"null");
							} else {
								logger.log(Level.INFO,t.toString());
							}
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}	
	
	private void test35() {
		
		Button b = new Button("test35()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					tm.test35(null,new Receiver<MailChange>() {
						@Override
						public void onSuccess(MailChange t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}	
	
	private void test36() {
		
		Button b = new Button("test36()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					MailBean mb = new MailBean();
					mb.setMail("pablo@econo");
					
					MailChangeBean mc = new MailChangeBean();
					mc.setConfirmed(false);
					mc.setMail(mb);
					mc.setPersonId("dsfdfdfdf");
					mc.setToken("sdfdf3434fgwc423f4fewf");					
					
					tm.test36(mc,new Receiver<MailChange>() {
						@Override
						public void onSuccess(MailChange t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}		
	
	
	private void test37() {
		
		Button b = new Button("test37()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					tm.test37(new Receiver<List<MailChange>>() {
						@Override
						public void onSuccess(List<MailChange> t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}		
	
	private void test38() {
		
		Button b = new Button("test38()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					MailBean mb = new MailBean();
					mb.setMail("pablo@econo");
					
					MailChangeBean mc = new MailChangeBean();
					mc.setConfirmed(false);
					mc.setMail(mb);
					mc.setPersonId("dsfdfdfdf");
					mc.setToken("sdfdf3434fgwc423f4fewf");					
					
					List<MailChange> l = new ArrayList<MailChange>();
					l.add(mc);
					l.add(mc);
					l.add(mc);
					tm.test38(l,new Receiver<List<MailChange>>() {
						@Override
						public void onSuccess(List<MailChange> t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}
	
	private void test40() {
		
		Button b = new Button("test40()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					GroupBean g = new GroupBean();
					g.setId("sdfdsfsd");
					g.setName("nombreeeeee");
					
					List<Person> ps = new ArrayList<Person>();
					PersonBean p = new PersonBean();
					p.setDni("fsdfd");
					p.setName("pablo");
					ps.add(p);
					g.setPersons(ps);

					List<Mail> mails = new ArrayList<Mail>();
					MailBean mb = new MailBean();
					mb.setMail("sdfds@econo");
					mails.add(mb);
					g.setMails(mails);
					
					g.setTypes(Arrays.asList(GroupType.ALIAS));
					
					tm.test40(g,new Receiver<List<Person>>() {
						@Override
						public void onSuccess(List<Person> t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}
	
	private void test41() {
		
		Button b = new Button("test41()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					PersonBean p = new PersonBean();
					p.setDni("fsdfd");
					p.setName("pablo");

					tm.test41(p,new Receiver<List<PeriodAssignation>>() {
						@Override
						public void onSuccess(List<PeriodAssignation> t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}
	
	private void test42() {
		
		Button b = new Button("test42()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {

					tm.test42(new Receiver<List<PeriodType>>() {
						@Override
						public void onSuccess(List<PeriodType> t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}
	
	private void test43() {
		
		Button b = new Button("test43()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					PersonBean p = new PersonBean();
					p.setDni("fsdfd");
					p.setName("pablo");
					
					PeriodAssignationBean pa = new PeriodAssignationBean();
					pa.setId("sfdsdfds");
					pa.setPerson(p);
					pa.setStart(new Date());
					pa.setType(PeriodType.DAILY);
					
					tm.test43(p, pa, new Receiver<Void>() {
						@Override
						public void onSuccess(Void t) {
							logger.log(Level.INFO,"void");
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}
	
	
	
	
	
	
	
	
	
	private void test50() {
		
		Button b = new Button("test50()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					Justification j = new JustificationBean();
					j.setId("id:3122331");
					j.setCode("ab2code");
					j.setDescription("Justificacion de prueba");
					
					tm.test50(j,new Receiver<Justification>() {
						@Override
						public void onSuccess(Justification t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}
	
	private void test51() {
		
		Button b = new Button("test51()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					JustificationBean j = new JustificationBean();
					j.setId("id:3122331");
					j.setCode("ab2code");
					j.setDescription("Justificacion de prueba");					
					
					tm.test51(j,new Receiver<List<Justification>>() {
						@Override
						public void onSuccess(List<Justification> t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}
	
	private void test52() {
		
		Button b = new Button("test52()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					JustificationBean j = new JustificationBean();
					j.setId("id:3122331");
					j.setCode("ab2code");
					j.setDescription("Justificacion de prueba");
					
					List<Justification> js = new ArrayList<Justification>();
					js.add(j);
					js.add(j);
					js.add(j);							
					
					tm.test52(js,new Receiver<List<Justification>>() {
						@Override
						public void onSuccess(List<Justification> t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}
	
	
	
	private void test53() {
		
		Button b = new Button("test53()");
		
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					Justification j = new JustificationBean();
					j.setId("id:3122331");
					j.setCode("ab2code");
					j.setDescription("Justificacion de prueba");
					
					Person person = new PersonBean();
					person.setDni("1");
					person.setName("emanuel");
					person.setLastName("pais");
					

					JustificationDate jd = new JustificationDateBean();
					jd.setEnd(new Date());
					jd.setStart(new Date());
					jd.setId("id-jd-123141");
					jd.setJustification(j);
					jd.setNotes("notasssss");
					jd.setPerson(person);
					
					tm.test53(jd,new Receiver<JustificationDate>() {
						@Override
						public void onSuccess(JustificationDate t) {
							logger.log(Level.INFO,t.toString());
						}
						
						@Override
						public void onError(String error) {
							Window.alert(error);
						}
					});
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		vp.add(b);
	}			
}
