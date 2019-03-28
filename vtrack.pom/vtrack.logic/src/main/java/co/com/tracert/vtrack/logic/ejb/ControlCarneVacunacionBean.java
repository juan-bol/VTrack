package co.com.tracert.vtrack.logic.ejb;

import java.util.Date;
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

import co.com.tracert.vtrack.logic.manager.EsquemasCarneManager;
import co.com.tracert.vtrack.model.dto.EsquemaDTO;
import co.com.tracert.vtrack.model.entities.Dosis;
import co.com.tracert.vtrack.model.entities.DosisAplicada;
import co.com.tracert.vtrack.model.entities.Esquema;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.entities.Vacuna;
import co.com.tracert.vtrack.model.entities.VacunaOculta;
import co.com.tracert.vtrack.model.interfaces.IControlCarneVacunacionLocal;
import co.com.tracert.vtrack.model.interfaces.IControlCarneVacunacionRemota;
@Stateless(name = "ControlCarneVacunacionBean", mappedName = "controlCarneVacunacionBean")

@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Local(IControlCarneVacunacionLocal.class)
@Remote(IControlCarneVacunacionRemota.class)
public class ControlCarneVacunacionBean implements IControlCarneVacunacionLocal, IControlCarneVacunacionRemota {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * @descripcion Registra la aplicacion de una dosis en un esquema del carne del
	 *              usuario
	 * @author Juan Bolanios
	 * @param usuario Usuario que aplicara la dosis
	 * @param esquema Dosis que se aplicara el usuario
	 */
	@Override
	public void registrarAplicacionDosis(Usuario usuario, Dosis dosis, Date fecha) throws Exception {
		EsquemasCarneManager manager = new EsquemasCarneManager(entityManager);
		manager.registrarAplicacionDosis(usuario, dosis, fecha);
	}

	
	/**
	 *  @descripcion Ocultar la vista de una vacuna en el carne de vacunacion
	 * @author Valery Ibarra
	 * @fecha 10/10/2018
	 * @param usuario Usuario que ocultara la vacuna en su carne
	 * @param vacuna Vacuna que se ocultara para todos los esquemas del carne del usuario
	 * @throws Exception si el usuario o vacunas ingresados son nulos, tambien cuando hay error en las consultas a la base de datos 
	 */
	@Override
	public VacunaOculta ocultarVacunaCarne(Usuario usuario, Vacuna vacuna) throws Exception {
		EsquemasCarneManager manager = new EsquemasCarneManager(entityManager);
		return manager.ocultarVacunaCarne(usuario, vacuna);
	}

	/**
	 * @Description devuelve el carne de vacunacion de un usuario
	 * @author Diego Lamus
	 * @fecha 9/10/2018
	 * @param usuario usuario sobre el cual se consultara el carne de vacunacion
	 * @return carne vacunacion
	 * @throws Exception si el usuario ingresado es nulo o sucede algun error en la
	 *                   consulta a la base de datos
	 */
	@Override
	public List<EsquemaDTO> darCarneVacunacion(Usuario usuario) throws Exception {
		EsquemasCarneManager manager = new EsquemasCarneManager(entityManager);
		return manager.darCarneVacunacion(usuario);
	}

	/**
	 * @descripcion modifica la dosis aplicada en los esquemas del carne de vacunacion del usuario
	 * @author Diego Lamus, Valery Ibarra
 	 * @fecha 23/11/2018
	 * @param dosisAplicada Es la dosis que un usuario tiene aplicada en su carne y desea eliminar dicha aplicacion
	 * @throws Exception si la dosis aplicada es nula o si hay problemas en el dao
	 */
	@Override
	public DosisAplicada modificarAplicacionDosis(DosisAplicada dosisAplicada, String estado) throws Exception {
		EsquemasCarneManager manager = new EsquemasCarneManager(entityManager);
		DosisAplicada dosAplic = manager.modificarAplicacionDosis(dosisAplicada,estado);
		return dosAplic;
	}

	/**
	 * @descripcion Este metodo determina si un esquema que el usuario tiene agregado a su carne no tiene vacunas, si es así, lo elimina
	 * @author Juan Bolaños
	 * @param esquema del usuario
	 * @throws Exception
	 */
	@Override
	public void verificarEsquemaAgregadoVacio(Usuario usuario, Esquema esquema) throws Exception {
		//TODO Agregar al deployment
		EsquemasCarneManager manager = new EsquemasCarneManager(entityManager);
		manager.verificarEsquemaAgregadoVacio(usuario, esquema);
	}


	/**
	 * @descripcion Realiza la auditoria de eliminar una vacuna de un esquema en el carne
	 * @author Victor Potes
	 * @fecha 30/11/2018
	 * @param vacunaOculta Vacuna que se elimino del esquema en el carne
	 * @param usuarioEnSesion usuario que se encarga de realizar la accion de eliminar la vacuna
	 * @throws Exception si no es posible auditar eliminar una vacuna de un esquema en el carne
	 */
	@Override
	public void auditarEliminarVacunaDeEsquemaEnCarne(VacunaOculta vacunaOculta, Usuario usuarioEnSesion)
			throws Exception {
		EsquemasCarneManager manager = new EsquemasCarneManager(entityManager);
		manager.auditarEliminarVacunaDeEsquemaEnCarne(vacunaOculta, usuarioEnSesion);
	}


	/**
	 * @descripcion Realiza la auditoria de modificar la aplicacion de una dosis en una vacuna
	 * @author Victor Potes
	 * @fecha 06/12/2018
	 * @param dosisAplicadaAnterior dosis aplicada que contiene los datos anteriores a la dosis aplicada actual
	 * @param dosisAplicadaActual dosis aplicada actual
	 * @param usuarioEnSesion usuario que realiza la acción de modificar la aplicación de la dosis
	 * @throws Exception si no es posible auditar la modificacion de una dosis aplicada
	 */
	@Override
	public void auditarModificarAplicacionDosis(DosisAplicada dosisAplicadaAnterior, DosisAplicada dosisAplicadaActual,
			Usuario usuarioEnSesion) throws Exception {
		
		EsquemasCarneManager manager = new EsquemasCarneManager(entityManager);
		manager.auditarModificarAplicacionDosis(dosisAplicadaAnterior, dosisAplicadaActual, usuarioEnSesion);
	}
	
}
