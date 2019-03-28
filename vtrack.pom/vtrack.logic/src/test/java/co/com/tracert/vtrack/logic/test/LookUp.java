package co.com.tracert.vtrack.logic.test;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import co.com.tracert.vtrack.logic.ejb.ControlCarneVacunacionBean;
import co.com.tracert.vtrack.logic.ejb.ControlCentrosVacunacionBean;
import co.com.tracert.vtrack.logic.ejb.ControlEsquemaVacunacionBean;
import co.com.tracert.vtrack.logic.ejb.InformacionGeograficaBean;
import co.com.tracert.vtrack.logic.ejb.InformacionUsuarioBean;
import co.com.tracert.vtrack.logic.ejb.NotificacionesBean;
import co.com.tracert.vtrack.model.interfaces.IControlCarneVacunacionRemota;
import co.com.tracert.vtrack.model.interfaces.IControlCentroVacunacionRemota;
import co.com.tracert.vtrack.model.interfaces.IControlEsquemaVacunacionRemota;
import co.com.tracert.vtrack.model.interfaces.IInformacionGeograficaRemota;
import co.com.tracert.vtrack.model.interfaces.IInformacionUsuarioRemota;
import co.com.tracert.vtrack.model.interfaces.INotificacionesRemota;

class LookUp{
	
	
	protected final static String appName = "vtrack";
	protected final static String moduleName = "vtrack.logic";
	protected final static String distinctName = "";
	protected static Context contexto;
	
	public LookUp() throws NamingException {
		  final Hashtable jndiProperties = new Hashtable();
	        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
	        jndiProperties.put(Context.PROVIDER_URL,"http-remoting://localhost:8080");
	        contexto = new InitialContext(jndiProperties);	
	}
			

	private String sysoLookUp(String beanName, String viewClassName) {
		  System.out.println("Looking EJB via JNDI ");
		  String ruta = "ejb:" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + viewClassName;
	      System.out.println(ruta);
	      return ruta;
	}
	
    protected IInformacionUsuarioRemota lookupRemoteUsuarioEJB() throws NamingException {
 
        final String beanName = InformacionUsuarioBean.class.getSimpleName();
        final String viewClassName = IInformacionUsuarioRemota.class.getName();
        String ruta = sysoLookUp(beanName, viewClassName);
        return (IInformacionUsuarioRemota) contexto.lookup(ruta);
    }
    
    protected IControlEsquemaVacunacionRemota lookupRemoteControlEsquemaEJB() throws NamingException {
 
        final String beanName = ControlEsquemaVacunacionBean.class.getSimpleName();
        final String viewClassName = IControlEsquemaVacunacionRemota.class.getName();
        String ruta = sysoLookUp(beanName, viewClassName);
 
        return (IControlEsquemaVacunacionRemota) contexto.lookup(ruta);
 
    }
    
    protected IControlCarneVacunacionRemota lookupRemoteControlCarneEJB() throws NamingException {
    	 
        final String beanName = ControlCarneVacunacionBean.class.getSimpleName();
        final String viewClassName = IControlCarneVacunacionRemota.class.getName();
        String ruta = sysoLookUp(beanName, viewClassName);
 
        return (IControlCarneVacunacionRemota) contexto.lookup(ruta);
 
    }
    
    protected IInformacionGeograficaRemota lookupRemoteInformacionGeograficaEJB() throws NamingException {
   	 
        final String beanName = InformacionGeograficaBean.class.getSimpleName();
        final String viewClassName = IInformacionGeograficaRemota.class.getName();
        String ruta = sysoLookUp(beanName, viewClassName);
 
        return (IInformacionGeograficaRemota) contexto.lookup(ruta);
 
    }
    
    protected IControlCentroVacunacionRemota lookupRemoteControlCentrosVacunacionEJB() throws NamingException {
      	 
        final String beanName = ControlCentrosVacunacionBean.class.getSimpleName();
        final String viewClassName = IControlCentroVacunacionRemota.class.getName();
        String ruta = sysoLookUp(beanName, viewClassName);
 
        return (IControlCentroVacunacionRemota) contexto.lookup(ruta);
 
    }
    
    protected INotificacionesRemota lookupRemoteNotificacionesEJB() throws NamingException {
      	 
        final String beanName = NotificacionesBean.class.getSimpleName();
        final String viewClassName = INotificacionesRemota.class.getName();
        String ruta = sysoLookUp(beanName, viewClassName);
 
        return (INotificacionesRemota) contexto.lookup(ruta);
 
    }

}
