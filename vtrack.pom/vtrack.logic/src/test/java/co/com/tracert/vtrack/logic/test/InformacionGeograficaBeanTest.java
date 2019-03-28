package co.com.tracert.vtrack.logic.test;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.tracert.vtrack.model.interfaces.IInformacionGeograficaRemota;

public class InformacionGeograficaBeanTest {

	/**
	 * Obtiene el Ejb del Wildfly
	 */
	private LookUp lookup;
	
	/**
	 * Log que permite imprimir el proceso de las pruebas unitarias
	 */
	private static final Logger log = LoggerFactory.getLogger(InformacionGeograficaBeanTest.class);
	
	
	/**
	 * Interfaz para usar los metodos de InformacionUsuarioBean
	 */
	private IInformacionGeograficaRemota iInformacionGeograficaRemota;
    
	
	  @Before
	    public void setUp() throws Exception {
	      lookup = new LookUp();
	    }
	
	
	/**
	 * Prueba para llenar la base de datos con los departamentos y ciudades de Colombia
	 * @author Victor Potes
	 */
	@Ignore
	@Test
	public void llenarBDDepartamentosMunicipiosTest() {
		
		log.info("inicio llenarBDDepartamentosMunicipiosTest");
		
		try {
			iInformacionGeograficaRemota = lookup.lookupRemoteInformacionGeograficaEJB();
			
			try {
				//Me toco usar esta ruta porque lo intenté con varias para que se pudiera acceder desde cualquier
				//computador y no me funcionó, sin embargo esta prueba solo se ejecuta para llenar la base de datos.
				String fileName = "C:\\Users\\Asus\\git\\vtrack\\vtrack.pom\\vtrack.logic\\src\\main\\resources\\Departamentos_y_municipios_de_Colombia.csv";			
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
				String sCadena;
				in.readLine();
				while ((sCadena = in.readLine())!=null) {
				  String[] valor = sCadena.split(";");
				  Long codDep = Long.parseLong(valor[1]);
				  String nomDep = valor[2];
				  String cm = valor[3].concat(valor[4]);
				  cm = cm.replaceAll("\"", "");
				  Long codMun = Long.parseLong(cm);
				  String nomMun = valor[5];
				  iInformacionGeograficaRemota.llenarBDDepartamentosMunicipios(codDep, nomDep, codMun, nomMun);
				  
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
