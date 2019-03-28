package co.com.tracert.vtrack.logic.ejb;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.com.tracert.vtrack.logic.manager.EsquemaVacunacionManager;
import co.com.tracert.vtrack.model.dto.EsquemaDTO;
import co.com.tracert.vtrack.model.entities.Esquema;
import co.com.tracert.vtrack.model.entities.EsquemaAgregado;
import co.com.tracert.vtrack.model.entities.Pais;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.interfaces.IControlEsquemaVacunacionLocal;
import co.com.tracert.vtrack.model.interfaces.IControlEsquemaVacunacionRemota;

@Stateless(name = "ControlEsquemaVacunacionBean", mappedName = "controlEsquemaVacunacionBean")

@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Local(IControlEsquemaVacunacionLocal.class)
@Remote(IControlEsquemaVacunacionRemota.class)
public class ControlEsquemaVacunacionBean implements IControlEsquemaVacunacionLocal, IControlEsquemaVacunacionRemota {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * @author Diego Lamus, Juan Bolanios
	 * @param usuario usuario al cual el sistema le sugerira esquemas
	 * @return Lista de esquemas de vacunacion sugeridos
	 * @throws Exception
	 */
	@Override
	public List<EsquemaDTO> sugerirEsquemasVacunacion(Usuario usuario, Pais pais) throws Exception {
		EsquemaVacunacionManager manager = new EsquemaVacunacionManager(entityManager);
		return manager.sugerirEsquemasVacunacion(usuario, pais);
	}

	/**
	 * @descripcion Agrega un esquema sugerido al usuario
	 * @author Diego Lamus
	 * @fecha 8/10/2018
	 * @param usuario usuario al cual se le agregara el esquema
	 * @param esquema esquema que se agregara al carne de vacunacion del usuario
	 */
	@Override
	public EsquemaAgregado agregarEsquemaSugerido(Usuario usuario, Esquema esquema) throws Exception {
		EsquemaVacunacionManager manager = new EsquemaVacunacionManager(entityManager);
		return manager.agregarEsquemaSugerido(usuario, esquema);
	}

	/**
	 * @descripcion Calcula la cantidad de dosis maxima de las vacunas de un esquema
	 * @author Juan Bolanios
	 * @param esquema
	 * @return numero de dosis maxima de las vacunas de un esquema
	 */
	@Override
	public int darDosisMaxima(Esquema esquema) throws Exception {
		EsquemaVacunacionManager manager = new EsquemaVacunacionManager(entityManager);
		return manager.darDosisMaxima(esquema);
	}

	/**
	 * @author Victor Potes
	 * @description Permitir llenar la base de datos con las vacunas
	 * @param idVacuna      código de la vacuna
	 * @param nombreVacuna  nombre de la vacuna
	 * @param idUsuario     código del usuario administrados
	 * @param detalleVacuna Informacion general de la vacuna
	 * @throws Exception Puede ocurrir Exception por múltiples transacciones
	 */
	@Override
	public void llenarBDVacunas(Long idVacuna, String nombreVacuna, Long idUsuario, String detalleVacuna)
			throws Exception {
		EsquemaVacunacionManager manager = new EsquemaVacunacionManager(entityManager);
		manager.llenarBDVacunas(idVacuna, nombreVacuna, idUsuario, detalleVacuna);

	}

	/**
	 * @author Ana Arango
	 * @fecha 24/11/2018
	 * @description Encontrar todos los esquemas que no pertenezcan al país entrado
	 *              por parámetro
	 * @param idPais código del país que no se quiere mostrar sus esquemas
	 * @throws Exception Puede ocurrir Exception cuando el país del usuario en
	 *                   sesión es null
	 */
	@Override
	public List<EsquemaDTO> encontrarEsquemasPorPais(Long idPais) throws Exception {
		EsquemaVacunacionManager manager = new EsquemaVacunacionManager(entityManager);

		return manager.encontrarEsquemasPorPais(idPais);
	}

	/**
	 * @author Ana Arango
	 * @fecha 24/11/2018
	 * @description Encontrar todos los paises exceptuando el país por parámetro
	 * @param idPais código del país que no se quiere mostrar
	 * @throws Exception 
	 */
	@Override
	public List<Pais> encontrarPaisExcepto(Long idPais) throws Exception {
		EsquemaVacunacionManager manager = new EsquemaVacunacionManager(entityManager);

		return manager.encontrarPaisExcepto(idPais);
	}

	/**
	 * @descripcion Realiza la auditoria de agregar un esquema de vacunacion al carne
	 * @author Victor Potes
	 * @fecha 30/11/2018
	 * @param esquemaAgregado Esquema que se agregó al carne de vacinacion
	 * @param usuarioEnSesion usuario que se encarga de realizar la accion de agregar el esquema al carne
	 * @throws Exception si no es posible auditar agregar un esquema al carne de vacunacion
	 */
	@Override
	public void auditarAgregarEsquemaAlCarne(EsquemaAgregado esquemaAgregado, Usuario usuarioEnSesion) throws Exception {
		EsquemaVacunacionManager manager = new EsquemaVacunacionManager(entityManager);
		manager.auditarAgregarEsquemaAlCarne(esquemaAgregado, usuarioEnSesion);
	}

}
