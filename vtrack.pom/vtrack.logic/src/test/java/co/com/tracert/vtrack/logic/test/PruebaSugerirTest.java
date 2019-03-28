package co.com.tracert.vtrack.logic.test;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.tracert.vtrack.model.dto.EsquemaDTO;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.interfaces.IControlEsquemaVacunacionRemota;
import co.com.tracert.vtrack.model.interfaces.IInformacionUsuarioRemota;

public class PruebaSugerirTest {

	/**
	 * Obtiene el Ejb del Wildfly
	 */
	private LookUp lookup;

	/**
	 * Log que permite imprimir el proceso de las pruebas unitarias
	 */
	private static final Logger log = LoggerFactory.getLogger(InformacionUsuarioBeanTest.class);

	/**
	 * Interfaz para usar los metodos de InformacionUsuarioBean
	 */
	private IInformacionUsuarioRemota iInformacionUsuarioRemota;

	/**
	 * Interfaz para usar los metodos de ControlEsquemaVacunacionRemota
	 */
	private IControlEsquemaVacunacionRemota iControlEsquemaVacunacionRemota;

	@Before
	public void setUp() throws Exception {
		lookup = new LookUp();
	}

	/**
	 * Prueba para verificar que el usuario esta en la base de datos
	 * 
	 * @author Victor Potes
	 */
	@Test
	@Ignore
	public void consultarUsuarioTest() {
		try {

			log.info("inicio consultarUsuarioTest");
			iInformacionUsuarioRemota = lookup.lookupRemoteUsuarioEJB();
			iControlEsquemaVacunacionRemota = lookup.lookupRemoteControlEsquemaEJB();

			Long idUsuario = 209L;
			Usuario usuario = iInformacionUsuarioRemota.consultarUsuario(idUsuario);

			List<EsquemaDTO> listaEsq = iControlEsquemaVacunacionRemota.sugerirEsquemasVacunacion(usuario, usuario.getPai());

			String vacio = "";
			
		} catch (Exception e) {
			log.info(e.getMessage());
		}
	}

}
