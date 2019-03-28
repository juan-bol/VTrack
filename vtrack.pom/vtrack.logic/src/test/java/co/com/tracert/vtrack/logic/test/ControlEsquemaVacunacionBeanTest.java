package co.com.tracert.vtrack.logic.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.tracert.vtrack.model.interfaces.IControlEsquemaVacunacionRemota;
import co.com.tracert.vtrack.model.interfaces.IInformacionGeograficaRemota;
import co.com.tracert.vtrack.model.interfaces.IInformacionUsuarioRemota;

public class ControlEsquemaVacunacionBeanTest {

	/**
	 * Obtiene el Ejb del Wildfly
	 */
	private LookUp lookup;
	
	/**
	 * Log que permite imprimir el proceso de las pruebas unitarias
	 */
	private static final Logger log = LoggerFactory.getLogger(ControlEsquemaVacunacionBeanTest.class);
	
	/**
	 * Interfaz para usar los metodos de InformacionUsuarioBean
	 */
	private IInformacionUsuarioRemota iInformacionUsuarioRemota;
	
	
	/**
	 * Interfaz para usar los metodos de ControlEsquemaVacunacionRemota
	 */
	private IControlEsquemaVacunacionRemota iControlEsquemaVacunacionRemota;
	
	
	/**
	 * Interfaz para usar los metodos de InformacionUsuarioBean
	 */
	private IInformacionGeograficaRemota iInformacionGeograficaRemota;
    
	
	  @Before
	    public void setUp() throws Exception {
	      lookup = new LookUp();
	    }
	
		/**
		 * Prueba para llenar la base de datos con las vacunas
		 * @author Victor Potes
		 */
		@Test
		@Ignore
		public void llenarBDVacunasTest() {
			
			log.info("inicio llenarBDVacunasTest");
			
			try {
				iControlEsquemaVacunacionRemota = lookup.lookupRemoteControlEsquemaEJB();
				
				try {
					//Me toco usar esta ruta porque lo intenté con varias para que se pudiera acceder desde cualquier
					//computador y no me funcionó, sin embargo esta prueba solo se ejecuta para llenar la base de datos.
					String fileName = "C:\\Users\\Asus\\git\\vtrack\\vtrack.pom\\vtrack.logic\\src\\main\\resources\\vacunas.csv";			
					BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
					String sCadena;
					in.readLine();
					while ((sCadena = in.readLine())!=null) {
					  String[] valor = sCadena.split("<");
					  Long idVacuna = Long.parseLong(valor[0]);
					  String nombreVacuna = valor[1];
					  Long idUsuario = Long.parseLong(valor[2]);
					  String detalleVacuna = valor[4];
					  iControlEsquemaVacunacionRemota.llenarBDVacunas(idVacuna, nombreVacuna, idUsuario, detalleVacuna);
					  
					} 
				}
				catch (Exception e) {
					throw e;
				}
				
							
			} 
			catch (Exception e) {
				
				log.info(e.getMessage());
			}
		
		}
	

}
