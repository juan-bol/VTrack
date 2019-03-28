package co.com.tracert.vtrack.logic.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.tracert.vtrack.model.entities.Dosis;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.interfaces.IControlCarneVacunacionRemota;
import co.com.tracert.vtrack.model.interfaces.IInformacionUsuarioRemota;

public class ControlCarneVacunacionBeanTest {

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
	public final static Long usuarioId = 4L;
	
	private final static String nombreUsuario4 = "Ana Arango";
	
	
	/**
	 * Interfaz para usar los metodos de InformacionUsuarioBean
	 */
	private IInformacionUsuarioRemota iInformacionUsuarioRemota;
	
	/**
	 * Interfaz para usar los metodos de ControlCarneVacunacionBean
	 */
	private IControlCarneVacunacionRemota iControlCarneVacunacionRemota;
    
	
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
	public void registrarAplicacionDosisTest(){
	
		log.info("inicio registrarAplicacionDosisTest");
		
		try {
			iInformacionUsuarioRemota = lookup.lookupRemoteUsuarioEJB();
			iControlCarneVacunacionRemota = lookup.lookupRemoteControlCarneEJB();
			
			
			Usuario usuario =iInformacionUsuarioRemota.consultarUsuario(usuarioId);
			
			
			
//			iControlCarneVacunacionRemota.registrarAplicacionDosis(usuario, dosis, fecha);
			
			
			assertNotNull("El usuario es null, no esta en la base de datos",usuario);
			assertEquals("Los nombres no coinciden",usuario.getNombre(), nombreUsuario4);
			
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info(e.getMessage());
		}
		
	}

}
