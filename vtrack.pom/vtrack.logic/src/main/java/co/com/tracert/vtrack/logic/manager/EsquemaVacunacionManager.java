package co.com.tracert.vtrack.logic.manager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.dto.EsquemaDTO;
import co.com.tracert.vtrack.model.entities.Auditoria;
import co.com.tracert.vtrack.model.entities.Esquema;
import co.com.tracert.vtrack.model.entities.EsquemaAgregado;
import co.com.tracert.vtrack.model.entities.Estado;
import co.com.tracert.vtrack.model.entities.Pais;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.entities.Vacuna;
import co.com.tracert.vtrack.model.entities.VacunaDetalle;
import co.com.tracert.vtrack.model.exceptions.ExcepcionNegocio;
import co.com.tracert.vtrack.persistence.dao.DaoGenerico;
import co.com.tracert.vtrack.persistence.dao.DaoVTrack;

public class EsquemaVacunacionManager {

	private EntityManager entityManager;
	
	public EsquemaVacunacionManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	/**
	 * @descripcion Agrega un esquema sugerido al usuario
	 * @author Diego Lamus
	 * @fecha 8/10/2018
	 * @param usuario usuario al cual se le agrego el esquema
	 * @param esquema esquema que se agregara al esquema de vacunacion del usuario
	 */
	public EsquemaAgregado agregarEsquemaSugerido(Usuario usuario, Esquema esquema) throws Exception {
		try {
			// Validar que el usuario no sea nulo
			if(usuario== null)
				throw new ExcepcionNegocio("El usuario no puede ser nulo");
			// Validar que el esquema no sea nulo
			if(esquema==null)
				throw new Exception("El esquema que se agrega al carne de vacunacion no puede ser nulo");

			//Instanciar dao
			DaoGenerico<EsquemaAgregado> dao= new DaoGenerico<>(entityManager, EsquemaAgregado.class);
			
			//Instanciar esquema a persistir
			EsquemaAgregado esqAgregado=new EsquemaAgregado();
			esqAgregado.setEsquema(esquema);
			esqAgregado.setUsuario(usuario);
			// Persistir esquema
			return dao.crear(esqAgregado);
		}catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * @descripcion Realiza la auditoria de agregar un esquema de vacunacion al carne
	 * @author Victor Potes
	 * @fecha 30/11/2018
	 * @param esquemaAgregado Esquema que se agregó al carne de vacinacion
	 * @param usuarioEnSesion usuario que se encarga de realizar la accion de agregar el esquema al carne
	 * @throws Exception si no es posible auditar agregar un esquema al carne de vacunacion
	 */
	public void auditarAgregarEsquemaAlCarne(EsquemaAgregado esquemaAgregado, Usuario usuarioEnSesion) throws Exception {
		

		if(esquemaAgregado == null) {
			throw new ExcepcionNegocio("No se puede auditar la agregación del esquema al carné de vacunación porque el esquema en "
					+ "el carné es nulo");
		}
		Auditoria auditarRegistro = new Auditoria();

		if(esquemaAgregado.getIdEsquemaAgregado() <= 0L) {
			throw new ExcepcionNegocio("No se puede auditar la agregación del esquema al carné de vacunación porque el esquema id"
					+ "del esquema no es válido ");
		}
		
		if(esquemaAgregado.getEsquema().getIdEsquema() <= 0L) {
			throw new ExcepcionNegocio("No se puede auditar la agregación del esquema al carné de vacunación porque el id del "
					+ "esquema no es válido");
		}
		
		if(esquemaAgregado.getUsuario().getIdUsuario() <= 0L) {
			throw new ExcepcionNegocio("No se puede auditar la agregación del esquema al carné de vacunación porque el id del "
					+ "usuario al que se le agregará el esquema al carné no es válido ");
		}
		
		String informacionEsquemaAgregado = ""+esquemaAgregado.getIdEsquemaAgregado()+","+
		esquemaAgregado.getEsquema().getIdEsquema()+","+esquemaAgregado.getUsuario().getIdUsuario()+"";
		
		if(usuarioEnSesion==null) {
			throw new ExcepcionNegocio("El usuario en sesión es null");
		}

		DaoVTrack<Auditoria> daoAuditoria = new DaoVTrack<>(entityManager, Auditoria.class);
		
		auditarRegistro.setFecha(new Date());
		auditarRegistro.setAccion("A");
		auditarRegistro.setNombreTablaAfectada(EsquemaAgregado.class.getSimpleName());
		auditarRegistro.setIdTablaAfectada(new BigDecimal(esquemaAgregado.getIdEsquemaAgregado()));
		auditarRegistro.setUsuario(usuarioEnSesion);
		auditarRegistro.setValorAnterior(null);
		auditarRegistro.setValorNuevo(informacionEsquemaAgregado);
		
		daoAuditoria.crear(auditarRegistro);
		
	}
	
	/**
	 * @descripcion Este metodo retorna una lista de esquemas sugeridos a un usuario de acuerdo a un pais y sus caracteristicas
	 * @author Diego Lamus, Juan Bolanos
	 * @fecha 01/09/18
	 * @param usuario usuario al cual el sistema le sugerira esquemas 
	 * @return Lista de esquemas de vacunacion sugeridos
	 * @throws Exception
	 */
	private List<Esquema> sugerirEsquemasVacunacionPrivado(Usuario usuario, Pais pais) throws Exception {
		List<Esquema> esquemasSugeridos = null;
		try {
			// Validar que el usuario exista
			if (usuario==null) {
				throw new ExcepcionNegocio("Debe ingresar un usuario no nulo");
			}
			// Calcular la edad en meses del usuario
			BigDecimal meses = null;
			if(usuario.getFechaNacimiento()!=null) {
				meses = darEdadMeses(usuario);
			}
			// ejecutar consulta
			DaoVTrack<Esquema> consultas = new DaoVTrack<Esquema>(entityManager,Esquema.class);
			esquemasSugeridos = consultas.sugerirEsquemas(usuario, meses, pais);	
		}catch (Exception e) {
			throw e;
		}
		return esquemasSugeridos;
	}
	
	/**
	 * @description Este metodo calcula la edad del usuario en numero de meses
	 * @author Juan Bolanios
	 * @fecha 24/09/18
	 * @param usuario
	 * @return edad del usuario en meses
	 */
	private BigDecimal darEdadMeses(Usuario usuario) {
		LocalDate hoy = LocalDate.now();
		Date date = new java.util.Date(usuario.getFechaNacimiento().getTime());
		LocalDate fechaNacimiento = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return new BigDecimal((Period.between(fechaNacimiento, hoy).getYears() * 12) 
				+ Period.between(fechaNacimiento, hoy).getMonths());
	}
	
	/**
	 * @descripcion Se retorna la cantidad máxima de dosis de alguna vacuna de un esquema
	 * @fecha 25/09/18
	 * @author Juan Bolanios
	 * @param esquema
	 * @return Cantidad máxima de dosis de las vacunas de un esquema
	 */
	public int darDosisMaxima(Esquema esquema) {
		int dosisMaxima = 0;
		for (int i = 0; i < esquema.getVacunaEsquemas().size(); i++) {
			int dosis = esquema.getVacunaEsquemas().get(i).getDosis().size();
			if (dosis > dosisMaxima) {
				dosisMaxima = dosis;
			}
		}
		return dosisMaxima;
	}
	
	
	/**
	 * @author Ana Arango
	 * @param usuario usuario al cual el sistema le sugerira esquemas 
	 * @return Lista de esquemas de vacunacion en DTO
	 * @throws Exception
	 */
	public List<EsquemaDTO> sugerirEsquemasVacunacion(Usuario usuario, Pais pais) throws Exception {
		List<Esquema> esquemasSugeridos = sugerirEsquemasVacunacionPrivado(usuario, pais);
		List<EsquemaDTO> esquemasDTO = new ArrayList<EsquemaDTO>();
		for (Esquema esquema : esquemasSugeridos) {
			esquemasDTO.add(new EsquemaDTO(esquema, null));
		}		
		return esquemasDTO;		

	}
	
	
	
	
	/**@author Victor Potes
	 * @description Permitir llenar la base de datos con las vacunas
	 * @param idVacuna código de la vacuna
	 * @param nombreVacuna nombre de la vacuna
	 * @param idUsuario código del usuario administrados
	 * @param detalleVacuna Informacion general de la vacuna
	 * @throws Exception Puede ocurrir Exception por múltiples transacciones
	 */
	public void llenarBDVacunas (Long idVacuna, String nombreVacuna, Long idUsuario, String detalleVacuna) 
			throws Exception {
		try {
			
			DaoVTrack<Vacuna> daoVacuna = new DaoVTrack<>(entityManager, Vacuna.class);
			DaoVTrack<VacunaDetalle> daoVacunaDetalle = new DaoVTrack<>(entityManager, VacunaDetalle.class);
			DaoVTrack<Usuario> daoUsuario = new DaoVTrack<>(entityManager, Usuario.class);
			Estado estado = daoVacuna.encontrarEstado(Vacuna.class.getName(), ConstantesVtrack.ESTADO_ACTIVO);
			
			
			Vacuna vacuna = new Vacuna();
			
			vacuna.setIdVacuna(idVacuna);
			vacuna.setEstado(estado);
			Usuario usuario = daoUsuario.encontrarPorId(idUsuario);
			vacuna.setUsuario(usuario);
			vacuna.setNombre(nombreVacuna);
			
			vacuna = daoVacuna.crear(vacuna);
			
			VacunaDetalle vacunaDetalle = new VacunaDetalle();
			vacunaDetalle.setIdVacuna(idVacuna);
			vacunaDetalle.setVacuna(vacuna);
			vacunaDetalle.setInformacionGeneral(detalleVacuna);
			
			daoVacunaDetalle.crear(vacunaDetalle);
				
		}
		catch (Exception e) {
			throw e;
		}
		
	}
	
	/**@author Ana Arango
	 * @fecha 24/11/2018
	 * @description Encontrar todos los esquemas que no pertenezcan al país entrado por parámetro
	 * @param idPais código del país que no se quiere mostrar sus esquemas
	 * @throws Exception Puede ocurrir Exception por múltiples transacciones
	 */
	public List<EsquemaDTO> encontrarEsquemasPorPais(Long idPais) throws Exception {
		DaoVTrack<Esquema> dao=new DaoVTrack<Esquema>(entityManager, Esquema.class);
		List<Esquema> esquemas = dao.encontrarEsquemasPorPais(idPais);
		List<EsquemaDTO> esquemasDTO = new ArrayList<EsquemaDTO>();
		for (Esquema esquema : esquemas) {
			esquemasDTO.add(new EsquemaDTO(esquema, null));
		}		
		return esquemasDTO;
	}

	
	/**
	 * @author Ana Arango
	 * @fecha 24/11/2018
	 * @description Encontrar todos los paises exceptuando el país por parámetro
	 * @param idPais código del país que no se quiere mostrar
	 * @throws Exception Puede ocurrir Exception por múltiples transacciones
	 */
	public List<Pais> encontrarPaisExcepto(Long idPais) throws Exception {
		DaoGenerico<Pais> daoGenerico= new DaoGenerico<>(entityManager, Pais.class);
		List<Pais> paisesTodos = daoGenerico.encontrarTodos();
		List<Pais> paisesFiltrados= new ArrayList<Pais>();
		for (Pais pais : paisesTodos) {
			if(pais.getIdPais()!=idPais){
				paisesFiltrados.add(pais);
			}
		}
		return paisesFiltrados;
	}
	
	
	
}
