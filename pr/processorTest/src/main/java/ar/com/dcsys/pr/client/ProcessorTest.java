package ar.com.dcsys.pr.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.Gender;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.data.person.Telephone;
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
	
	private class PersonB implements Person {

		public PersonB(String dni) {
			this.dni = dni;
		}
		
		private String dni;
		
		@Override
		public String getId() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setId(String id) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setName(String n) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getLastName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setLastName(String n) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getDni() {
			// TODO Auto-generated method stub
			return dni;
		}

		@Override
		public void setDni(String dni) {
			this.dni = dni;
		}

		@Override
		public String getAddress() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setAddress(String addr) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getCity() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setCity(String city) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getCountry() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setCountry(String country) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Gender getGender() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setGender(Gender gender) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Date getBirthDate() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setBirthDate(Date d) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public List<PersonType> getTypes() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setTypes(List<PersonType> types) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public List<Telephone> getTelephones() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setTelephones(List<Telephone> tels) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
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
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
		
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
					Person p = new PersonB("1");
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
					s.add(new PersonB("1"));
					s.add(new PersonB("2"));
					s.add(new PersonB("3"));
					s.add(new PersonB("4"));
					s.add(new PersonB("5"));
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
	
}
