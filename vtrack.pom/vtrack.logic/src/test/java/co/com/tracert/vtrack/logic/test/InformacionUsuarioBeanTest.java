package co.com.tracert.vtrack.logic.test;

import static org.junit.Assert.*;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.tracert.vtrack.logic.ejb.InformacionUsuarioBean;
import co.com.tracert.vtrack.model.entities.Cuenta;
import co.com.tracert.vtrack.model.entities.Menu;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.interfaces.IInformacionUsuarioRemota;
import co.com.tracert.vtrack.logic.test.LookUp;

/**
 * 
 * @author Victor Potes
 * Clase Test que permite realizar pruebas unitarias a los metodos mas importantes de InformacionUsuarioBean
 */
public class InformacionUsuarioBeanTest{
	
	/**
	 * Obtiene el Ejb del Wildfly
	 */
	private LookUp lookup;
	
	/**
	 * Log que permite imprimir el proceso de las pruebas unitarias
	 */
	private static final Logger log = LoggerFactory.getLogger(InformacionUsuarioBeanTest.class);
	
	/**
	 * Id del usuario con el que se van a hacer las pruebas unitarias
	 */
	public final static Long usuarioId = 1L;
	
	private final static String nombreUsuario4 = "Victor Potes";
	
	
	/**
	 * Interfaz para usar los metodos de InformacionUsuarioBean
	 */
	private IInformacionUsuarioRemota iInformacionUsuarioRemota;
    
	
	  @Before
	    public void setUp() throws Exception {
	      lookup = new LookUp();
	    }
	
	
	/**
	 * Prueba para verificar que el usuario esta en la base de datos
	 * @author Victor Potes
	 */
	@Test
	@Ignore
	public void consultarUsuarioTest() {
		
		log.info("inicio consultarUsuarioTest");
		
		try {
			iInformacionUsuarioRemota = lookup.lookupRemoteUsuarioEJB();
			Usuario usuario =iInformacionUsuarioRemota.consultarUsuario(usuarioId);
			assertNotNull("El usuario es null, no esta en la base de datos",usuario);
			assertEquals("Los nombres no coinciden",usuario.getNombre(), nombreUsuario4);
			
		} 
		catch (Exception e) {
			
			log.info(e.getMessage());
		}
	
	}
	
	
	
	/**
	 * Prueba para verificar que el usuario esta en la base de datos
	 * @author Victor Potes
	 */
	@Test
	@Ignore
	public void crearCuentaTest() {
		
		log.info("inicio crearCuentaTest");
		
		try {
			iInformacionUsuarioRemota = lookup.lookupRemoteUsuarioEJB();
			
			//Esta cuenta se creara para el administrador. Una vez creada la cuenta, el usuario se crea con id 1
			//en la Bd, se conecta con esta cuenta y se le asigna el rol de administrador
			
			
			Cuenta cuenta = new Cuenta();
			cuenta.setContrasenia("Icesi2018");
			cuenta.setCorreo("pruebaIcesi@l.com");

			cuenta = iInformacionUsuarioRemota.crearCuenta(cuenta);
			
			log.error(""+cuenta.getIdCuenta());
			
			
			
		} 
		catch (Exception e) {
			
			log.info(e.getMessage());
		}
		
	}
	
	
	
//	/**
//	 * Prueba para verificar el menu del usuario de acuerdo con su rol
//	 * @author Victor Potes
//	 */
//	@Test
//	public void consultarMenuUsuario() {
//		
//		log.info("inicio consultarUsuarioTest");
//		
//		try {
//			iInformacionUsuarioRemota = lookup.lookupRemoteUsuarioEJB();
//			Usuario usuario =iInformacionUsuarioRemota.consultarUsuario(usuarioId);
//			int tamanio = iInformacionUsuarioRemota.consultarMenuUsuario(usuario).size();
//			assertEquals("El usuario debe tener 6 menu-paginas",tamanio, 6);
//		} 
//		catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			log.info(e.getMessage());
//
//		}
//		
//	}
	
	
}
