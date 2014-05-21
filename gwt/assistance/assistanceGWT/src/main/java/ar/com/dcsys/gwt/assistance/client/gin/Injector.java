package ar.com.dcsys.gwt.assistance.client.gin;

import ar.com.dcsys.gwt.assistance.client.activity.AssistanceActivityMapper;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.gin.WsGinModule;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import ar.com.dcsys.gwt.assistance.client.gin.AssistanceGWTGinModule;
import ar.com.dcsys.gwt.assistance.client.manager.JustificationsManager;
import ar.com.dcsys.gwt.assistance.client.manager.PeriodsManager;


@GinModules(value = {AssistanceGWTGinModule.class,
					AssistanceModelGinModule.class,
					 WsGinModule.class/*,
					 AuthModelGinModule.class */})
public interface Injector extends Ginjector {
	
	public EventBus eventbus();

	public AssistanceActivityMapper assistanceActivityMapper();
	public PeriodsManager periodsManager();
	public JustificationsManager justificationsManager();
	
	public WebSocket ws();
	
}
