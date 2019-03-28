package co.com.tracert.vtrack.logic.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import co.com.tracert.vtrack.model.dto.EsquemaDTO;
import co.com.tracert.vtrack.model.entities.Auditoria;
import co.com.tracert.vtrack.model.entities.Dosis;
import co.com.tracert.vtrack.model.entities.DosisAplicada;
import co.com.tracert.vtrack.model.entities.Esquema;
import co.com.tracert.vtrack.model.entities.EsquemaAgregado;
import co.com.tracert.vtrack.model.entities.Estado;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.entities.Vacuna;
import co.com.tracert.vtrack.model.entities.VacunaOculta;
import co.com.tracert.vtrack.model.exceptions.ExcepcionNegocio;
import co.com.tracert.vtrack.persistence.dao.DaoGenerico;
import co.com.tracert.vtrack.persistence.dao.DaoVTrack;
import co.com.tracert.vtrack.model.constants.ConstantesVtrack;


public class EsquemasCarneManager {

	private EntityManager entityManager;

	public EsquemasCarneManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * @descripcion Este metodo registra la aplicacion de una dosis por parte de un
	 *              usuario
	 * @author Juan Bolanios, Diego Lamus, Valery Ibarra
	 * @fecha 15/09/18
	 * @param usuario Usuario que aplicará la dosis
	 * @param dosis   Dosis aplicada por el usuario
	 * @throws ExcepcionNegocio
	 */
	public DosisAplicada registrarAplicacionDosis(Usuario usuario, Dosis dosis, Date fecha) throws Exception{
		try {
			// Validar que el usuario exista
			if (usuario == null) {
				throw new ExcepcionNegocio("Debe ingresar un usuario no nulo");
			}
			// Validar que el usuario tenga un ID
			if (usuario.getIdUsuario() == 0L) {
				throw new ExcepcionNegocio("El usuario debe tener ID");
			}
			// Validar que la dosis exista
			if (dosis == null) {
				throw new ExcepcionNegocio("Debe ingresar una dosis no nula");
			}
			// Validar que el usuario tenga un ID
			if (dosis.getIdDosis() == 0L) {
				throw new ExcepcionNegocio("La dosis debe tener ID");
			}
			// Validar que la fecha no sea nula
			if (fecha == null) {
				throw new ExcepcionNegocio("Se debe ingresar una fecha valida.");
			}
			// Validar que la fecha no sea superior a la fecha actual
			if(fecha.compareTo(new Date())> 0){
				throw new ExcepcionNegocio("La fecha ingresada es mayor que la fecha actual.");
			}
			// Consultar estado
			DaoVTrack<DosisAplicada> dao = new DaoVTrack<DosisAplicada>(entityManager, DosisAplicada.class);
			Estado estado = dao.encontrarEstado(DosisAplicada.class.getName(), ConstantesVtrack.ESTADO_ACTIVO);
			// Se valida que el tipo estado de DOSIS_APLICADA exista
			if (estado == null) {
				throw new ExcepcionNegocio("El tipo de estado perteneciente a la entidad dosis aplicada no existe");
			}
			// Creacion de la dosis aplicada
			DosisAplicada dosisAplicada = new DosisAplicada();
			dosisAplicada.setDosi(dosis);
			dosisAplicada.setFechaAplicacion(fecha);
			dosisAplicada.setUsuario(usuario);
			dosisAplicada.setEstado(estado);
			// Crear la dosis
			return dao.crear(dosisAplicada);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @descripcion Oculta una vacuna del carne de un usuario
	 * @author Valery Ibarra
	 * @fecha 8/10/2018
	 * @param usuario usuario que quiere ocultar una vacuna
	 * @param vacuna  vacuna que el usuario desea ocultar
	 * @throws Exception
	 */
	public VacunaOculta ocultarVacunaCarne(Usuario usuario, Vacuna vacuna) throws Exception {
		try {
			// Aqui hago las verificaciones de lo que entra como parametro
			if (usuario == null) {
				throw new ExcepcionNegocio("El usuario no puede ser nulo");
			}
			if (usuario.getIdUsuario() == 0L) {
				throw new ExcepcionNegocio("El usuario debe tener algun valor");
			}
			// Validar que la vacuna no sea nula
			if (vacuna == null) {
				throw new ExcepcionNegocio("La vacuna no puede ser nula");
			}
			if (vacuna.getIdVacuna() == 0L) {
				throw new ExcepcionNegocio("La vacuna debe tener algun valor");
			}
			// Se crea la instancia de la vacuna oculta
			VacunaOculta vacunaOculta = new VacunaOculta();
			// Se indica la vacuna que se va a ocultar a partir del usuario y la vacuna del
			// esquema
			vacunaOculta.setUsuario(usuario);
			vacunaOculta.setVacuna(vacuna);
			DaoGenerico<VacunaOculta> dao = new DaoGenerico<>(entityManager, VacunaOculta.class);
			// Se guarda en la base de datos la vacuna oculta
			return dao.crear(vacunaOculta);
		} catch (Exception e) {
			throw e;
		}
	}

	
	
	/**
	 * @descripcion Realiza la auditoria de eliminar una vacuna de un esquema en el carne
	 * @author Victor Potes
	 * @fecha 30/11/2018
	 * @param vacunaOculta Vacuna que se elimino del esquema en el carne
	 * @param usuarioEnSesion usuario que se encarga de realizar la accion de eliminar la vacuna
	 * @throws Exception si no es posible auditar eliminar una vacuna de un esquema en el carne
	 */
	public void auditarEliminarVacunaDeEsquemaEnCarne(VacunaOculta vacunaOculta, Usuario usuarioEnSesion) throws Exception {
		

		if(vacunaOculta == null) {
			throw new ExcepcionNegocio("No se puede auditar la eliminación de la vacuna porque la vacunaOculta es nula");
		}
		Auditoria auditarRegistro = new Auditoria();

		if(vacunaOculta.getIdVacunaOculta() <= 0L) {
			throw new ExcepcionNegocio("No se puede auditar la eliminación de la vacuna en el esquema porque su id es inválido ");
		}
		
		if(vacunaOculta.getVacuna().getIdVacuna() <= 0L) {
			throw new ExcepcionNegocio("No se puede auditar la eliminación de la vacuna porque el id de la vacuna es nula");
		}
		
		if(vacunaOculta.getUsuario().getIdUsuario() <= 0L) {
			throw new ExcepcionNegocio("No se puede auditar la eliminación de la vacuna del esquema en el carné porque el id"
					+ " del usuario el cual quiere eliminar la vacuna de su esquema es inválido ");
		}
		
		String informacionVacunaOculta = ""+vacunaOculta.getIdVacunaOculta()+","+
				vacunaOculta.getVacuna().getIdVacuna()+","+vacunaOculta.getUsuario().getIdUsuario()+"";
		
		if(usuarioEnSesion==null) {
			throw new ExcepcionNegocio("El usuario en sesión es null");
		}

		DaoVTrack<Auditoria> daoAuditoria = new DaoVTrack<>(entityManager, Auditoria.class);

		auditarRegistro.setFecha(new Date());
		auditarRegistro.setAccion("E");
		auditarRegistro.setNombreTablaAfectada(VacunaOculta.class.getSimpleName());
		auditarRegistro.setIdTablaAfectada(new BigDecimal(vacunaOculta.getIdVacunaOculta()));
		auditarRegistro.setUsuario(usuarioEnSesion);
		auditarRegistro.setValorAnterior(null);
		auditarRegistro.setValorNuevo(informacionVacunaOculta);
		
		daoAuditoria.crear(auditarRegistro);
		
	}
	
	
	
	
	/**
	 * @descripcion modifica la dosis aplicada en los esquemas del carne de vacunacion del usuario
	 * @author Diego Lamus, Valery Ibarra
 	 * @fecha 23/11/2018
	 * @param dosisAplicada Es la dosis que un usuario tiene aplicada en su carne y desea eliminar dicha aplicacion
	 * @throws Exception si la dosis aplicada es nula o si hay problemas en el dao
	 */
	public DosisAplicada modificarAplicacionDosis(DosisAplicada dosisAplicada, String estado)throws Exception {
		try {
			// Validar que la dosis aplicada no sea nula
			if (dosisAplicada == null) {
				throw new ExcepcionNegocio("La dosis aplicada no puede ser nula");
			}
			// Validar que el id de la dosis aplicada no sea nulo
			if (dosisAplicada.getIdDosisAplicada()== 0L) {
				throw new ExcepcionNegocio("El identificador de la dosis aplicada es nulo");
			}
			// Validar que la fecha de la dosis aplicada no sea nula
			if (dosisAplicada.getFechaAplicacion() == null) {
				throw new ExcepcionNegocio("Por favor ingrese una fecha valida");
			}
			//La validación que sigue no aplica para cuando se de
			if(estado.equals(ConstantesVtrack.ESTADO_ACTIVO)) {
				// Validar que la fecha no sea superior a la fecha actual
				if(dosisAplicada.getFechaAplicacion().compareTo(new Date())> 0){
					throw new ExcepcionNegocio("La fecha ingresada es mayor que la fecha actual.");
				}
			}
			
			
			// Encontrar la dosis en la base de datos y validar que exista
			DaoVTrack<DosisAplicada> dao = new DaoVTrack<>(entityManager, DosisAplicada.class);
			DosisAplicada dosAplic = dao.encontrarPorId(dosisAplicada.getIdDosisAplicada());
			if(dosAplic==null) {
				throw new ExcepcionNegocio("La dosis aplicada no existe en la base de datos. No es posible modificarla");
			}
			//Actualizar el estado de la dosis de dosis
			Estado estad = dao.encontrarEstado(DosisAplicada.class.getName(), estado);
			if (estad == null) {
				throw new ExcepcionNegocio("El tipo de estado perteneciente a la entidad dosis aplicada no existe");
			}
			dosAplic.setEstado(estad);
			// Actualizar la fecha de la dosis aplicada
			dosAplic.setFechaAplicacion(dosisAplicada.getFechaAplicacion());
			// Actualizar la dosis aplicada
			return 	dao.actualizar(dosAplic);
		} catch (Exception e) {
			throw e;
		}

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
	public void auditarModificarAplicacionDosis(DosisAplicada dosisAplicadaAnterior, DosisAplicada 
			dosisAplicadaActual, Usuario usuarioEnSesion)throws Exception {
		
		if(dosisAplicadaActual == null) {
			throw new ExcepcionNegocio("No se puede auditar la modificación de la aplicación de una dosis porque ésta"
					+ " se encuentra nula");
		}
		
		if(dosisAplicadaAnterior == null) {
			throw new ExcepcionNegocio("No se puede auditar la modificación de la aplicación de una dosis porque "
					+ "no hay una anterior aplicación de dosis la cual modificar");
		}
		
		if(usuarioEnSesion == null) {
			throw new ExcepcionNegocio("El usuario en sesión es null");
		}
		
		Auditoria auditarRegistro = new Auditoria();
		
		String informacionRegistroAnterior = ""+dosisAplicadaAnterior.getIdDosisAplicada()+",";
		String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dosisAplicadaAnterior.
				getFechaAplicacion().getTime()).split(" ")[0];
		informacionRegistroAnterior = informacionRegistroAnterior.concat(date+",");
		informacionRegistroAnterior = informacionRegistroAnterior.concat(""+dosisAplicadaAnterior.getDosi().getIdDosis()+
				","+dosisAplicadaAnterior.getUsuario().getIdUsuario()+","+dosisAplicadaAnterior.getEstado().getIdEstado()+"");

		String informacionRegistroActual = ""+dosisAplicadaActual.getIdDosisAplicada()+",";
		String dateActual = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dosisAplicadaActual.
				getFechaAplicacion().getTime()).split(" ")[0];
		informacionRegistroActual = informacionRegistroActual.concat(dateActual+",");
		informacionRegistroActual = informacionRegistroActual.concat(""+dosisAplicadaActual.getDosi().getIdDosis()+
				","+dosisAplicadaActual.getUsuario().getIdUsuario()+","+dosisAplicadaActual.getEstado().getIdEstado()+"");
		
		
		
		auditarRegistro.setFecha(new Date());
		auditarRegistro.setAccion("M");
		auditarRegistro.setNombreTablaAfectada(DosisAplicada.class.getSimpleName());
		auditarRegistro.setIdTablaAfectada(new BigDecimal(dosisAplicadaActual.getIdDosisAplicada()));
		auditarRegistro.setUsuario(usuarioEnSesion);
		auditarRegistro.setValorAnterior(informacionRegistroAnterior);
		auditarRegistro.setValorNuevo(informacionRegistroActual);
		

		DaoVTrack<Auditoria> daoAuditoria = new DaoVTrack<>(entityManager, Auditoria.class);
		daoAuditoria.crear(auditarRegistro);
		
	}

	
	
	/**
	 * @descripcion Retorna la lista de EsquemasDto que representan el carné de vacunación de un usuario
	 * @author Ana Arango, Diego Lamus
 	 * @fecha 3/11/2018
	 * @param usuario usuario al cual el sistema le sugerira esquemas 
	 * @return Lista de esquemas DTO que pertenece al usuario ingresado
	 * @throws Exception
	 */
	public List<EsquemaDTO> darCarneVacunacion(Usuario usuario) throws Exception {
		try {
			// Validar que el usuario no sea nulo
			if (usuario == null) {
				throw new ExcepcionNegocio("El usuario no puede ser nulo");
			}
			// Validar que el id del usuario sea consistente
			if (usuario.getIdUsuario() <= 0) {
				throw new ExcepcionNegocio("El usuario debe tener un id valida. Recibida: " + usuario.getIdUsuario());
			}
			DaoVTrack<EsquemaAgregado> dao = new DaoVTrack<>(entityManager, EsquemaAgregado.class);
			// dar esquemas agregados por el ususario
			List<Esquema> carnes = dao.darCarneVacunacion(usuario);
			// Listar vacunas ocultas por el usuario
			List<Vacuna> vacunasOcultas = dao.darVacunasOcultas(usuario);
			// Listar dosis aplicadas
			List<DosisAplicada> dosisAplicadas =dao.darDosisAplicadas(usuario);
			// Construir dto
			List<EsquemaDTO> esquemasDTO = new ArrayList<EsquemaDTO>();
			for (Esquema carne : carnes) {
				esquemasDTO.add(new EsquemaDTO(carne, usuario.getFechaNacimiento(),vacunasOcultas,dosisAplicadas));
			}
			return esquemasDTO;				
		} catch (Exception e) {
			throw e;
		}		
	}
	

	/**
	 * @descripcion Este metodo determina si un esquema que el usuario tiene agregado a su carne no tiene vacunas, si es así, lo elimina
	 * @author Juan Bolaños
	 * @fecha 23/10/18
	 * @param usuario 
	 * @param esquema del usuario
	 * @throws Exception
	 */
	public void verificarEsquemaAgregadoVacio(Usuario usuario, Esquema esquema) throws Exception{
		//TODO Agregar al deployment
		try {
			//Validar que el usuario no sea nulo
			if(usuario==null) {
				throw new ExcepcionNegocio("El usuario no puede ser nulo");
			}
			
			//Validar que el esquema agregado no sea nulo
			if(esquema==null) {
				throw new ExcepcionNegocio("El esquema no puede ser nulo");
			}
			//Instanciar DAO
			DaoVTrack<Esquema> daoEsquema = new DaoVTrack<>(entityManager, Esquema.class);
			
			//Se crea la instancia del esquema agregado
			esquema = daoEsquema.encontrarPorId(esquema.getIdEsquema());
			
			//Verificar que el esquema agregado exista
			if(esquema==null) {
				throw new ExcepcionNegocio("El esquema no existe");
			}
			
			//Se consulta si el esquema agregado tiene todas sus vacunas ocultas
			boolean esquemaVacio = daoEsquema.verificarEsquemaAgregadoVacio(usuario, esquema);			
			
			if(esquemaVacio) {
				//Instanciar DAO
				DaoVTrack<EsquemaAgregado> daoEsquemaAgregado = new DaoVTrack<>(entityManager, EsquemaAgregado.class);
				//Se consulta el esquema agregado perteneciente al usuario y al esquema
				EsquemaAgregado esquemaAgregado = daoEsquemaAgregado.encontrarEsquemaAgregadoPorUsuarioEsquema(usuario,esquema);
				//Se elimina el esquema agregado
				daoEsquemaAgregado.eliminar(esquemaAgregado);
				DaoVTrack<VacunaOculta> daoVacunaOculta = new DaoVTrack<>(entityManager, VacunaOculta.class);
				//Se recuperan las vacunas que fueron ocultas
				daoVacunaOculta.recuperarVacunasOcultas(usuario); //TODO revisar modelo de datos (Juan)
			}
			
		} catch(Exception e) {
			throw e;
		}
	}

}
