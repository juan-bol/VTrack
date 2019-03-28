package co.com.tracert.vtrack.logic.manager;

import java.util.List;

import javax.persistence.EntityManager;

import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.entities.Cuenta;
import co.com.tracert.vtrack.model.entities.Estado;
import co.com.tracert.vtrack.model.entities.Permiso;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.exceptions.ExcepcionNegocio;
import co.com.tracert.vtrack.persistence.dao.DaoVTrack;

public class PermisosManager {

	private EntityManager entityManager;

	public PermisosManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * @descripcion Configura un permiso dependiendo las caracteristicas del mismo
	 *              en su creacion y modificacion
	 * @author Valery Ibarra
	 * @fecha 19/11/2018
	 * @param permiso Permiso con la informacion a modificar o crear en la base de
	 *                datos
	 * @throws Exception si los usuarios ingresados son nulos o no cumplen lo
	 *                   estipulado, asimismo con el nombre de cuenta o sucede algun
	 *                   error en la consulta a la base de datos
	 */
	public Permiso configurarPermisoGeneral(Permiso permiso) throws Exception {
		// Para obtener el usuario como tal lo busco en la base de datos por el id
		DaoVTrack<Usuario> daoUsuario = new DaoVTrack<>(entityManager, Usuario.class);
		DaoVTrack<Permiso> daoPermiso = new DaoVTrack<>(entityManager, Permiso.class);

		// usuario origen es el usuario que tiene permiso sobre otro, es decir, el que
		// los recibe
		Usuario usuOrigen = daoUsuario.encontrarPorId(permiso.getUsuarioOrigen().getIdUsuario());
		// Usuario destino es el que otorgo un permiso
		Usuario usuDestino = daoUsuario.encontrarPorId(permiso.getUsuarioDestino().getIdUsuario());
		// Cambio en el permiso por los usuarios completos
		permiso.setUsuarioOrigen(usuOrigen);
		permiso.setUsuarioDestino(usuDestino);

		// Hago validaciones de los campos ingresados en permiso
		try {
			permiso = verificacionesDatos(permiso);
		} catch (Exception e) {
			throw e;
		}
		// ya que validaciones de datos se cambio solamente el estado es lo unico que
		// cambiare en permiso Bd
		permiso.setEstado(permiso.getEstado());

		String estado = permiso.getEstado().getEstado();
		// Cuando un usuario da un permiso con el correo de otro usuario
		if (estado.equals(ConstantesVtrack.ESTADO_EN_ESPERA)) {
			//si el permiso es nuevo en darse
			if (permiso.getIdPermiso() == 0L) {
				// Busco si ya hay permisos con esos usuarios como origen y destino
				// respectivamente
				List<Permiso> listaPermisos = daoPermiso.darPermisoConUsuarios(usuOrigen, usuDestino);
				if (!listaPermisos.isEmpty()) {
					// ya hay un permiso con ese usuario origen y ese usuario destino
					Permiso permisoAnterior = listaPermisos.get(0);
					if (permisoAnterior != null	&& permisoAnterior.getEstado().getEstado().equals(ConstantesVtrack.ESTADO_INACTIVO)) {
						// si quiere crear nuevamente un permiso que fue inactivado(eliminado por el)
						DaoVTrack<Permiso> daoEstado = new DaoVTrack<Permiso>(entityManager, Permiso.class);
						Estado estadoCompleto = daoEstado.encontrarEstado(Permiso.class.getName(), estado);
						// Cambio por un estado real el estado que tiene el permiso, es decir con toda
						// la informacion
						permisoAnterior.setEstado(estadoCompleto);
						permisoAnterior.setNombreCuenta(null);
						permisoAnterior.setTipoPermiso(permiso.getTipoPermiso());
						permisoAnterior = daoPermiso.actualizar(permisoAnterior);
						permiso=permisoAnterior;
					} else {
						throw new ExcepcionNegocio("Ya hay un permiso concecido al usuario ingresado");
					}
				}else {
					//no ha concedido permisos antes a ese usuario
					permiso = daoPermiso.crear(permiso);
				}
				
			} else {
				permiso = daoPermiso.actualizar(permiso);
			}
		} else {
			if (estado.equals(ConstantesVtrack.ESTADO_APROBADO)) {
				// Cuando el usuario beneficiario acepta el permiso del usuario proveedor
				// Tambien cuando el usuario beneficiario es aquel que creo una cuenta asociado
				String nombreCuenta = permiso.getNombreCuenta();
				// Valido que nombre ingresado para asociar al nuevo usuario este bien
				if (nombreCuenta == null || nombreCuenta.trim().equals("")) {
					throw new ExcepcionNegocio("El nombre de la cuenta asociada debe contener informacion");
				}
				if (nombreCuenta.length() > 30) {
					throw new ExcepcionNegocio("El nombre de la cuenta asociada no debe exceder los 30 caracteres");
				}
			}
			permiso = daoPermiso.actualizar(permiso);
			if (permiso == null) {
				throw new ExcepcionNegocio("Problemas al actualizar el permiso en el Dao");
			}
		}
		return permiso;
	}

	/**
	 * @descripcion Configura un permiso de una cuenta asociada (usuario asociado)
	 * @author Valery Ibarra
	 * @fecha 19/11/2018
	 * @param permiso Permiso con la informacion a modificar o crear en la base de
	 *                datos
	 * @throws Exception si los usuarios ingresados son nulos o no cumplen lo
	 *                   estipulado, asimismo con el nombre de cuenta o sucede algun
	 *                   error en la consulta a la base de datos
	 */
	public Permiso configurarPermisoAsociado(Permiso permiso) throws Exception {
		// Para obtener el usuario como tal lo busco en la base de datos por el id
		DaoVTrack<Usuario> daoUsuario = new DaoVTrack<>(entityManager, Usuario.class);
		// usuario origen es el usuario que tiene permiso sobre otro, es decir, el que
		// cree
		Usuario usuOrigen = permiso.getUsuarioOrigen();
		usuOrigen = daoUsuario.encontrarPorId(permiso.getUsuarioOrigen().getIdUsuario());
		Usuario usuDestino = daoUsuario.encontrarPorId(permiso.getUsuarioDestino().getIdUsuario());
		permiso.setUsuarioOrigen(usuOrigen);
		permiso.setUsuarioDestino(usuDestino);
		// Hago validaciones de los campos ingresados en permiso
		try {
			permiso = verificacionesDatos(permiso);
		} catch (Exception e) {
			throw e;
		}

		String estado = permiso.getEstado().getEstado();
		// Creo el Dao de permisos para la transacciones que vaya a realizar
		DaoVTrack<Permiso> daoPermiso = new DaoVTrack<>(entityManager, Permiso.class);
		if (!estado.equals(ConstantesVtrack.ESTADO_APROBADO)) {
			throw new ExcepcionNegocio("El tipo de estado para una cuenta asociada creada debe ser aprobado");
		}
		// Debe ingresarle el nombre de cuenta
		String nombreCuenta = permiso.getNombreCuenta();
		// Valido que nombre ingresado para asociar al nuevo usuario este bien
		if (nombreCuenta == null || nombreCuenta.trim().equals("")) {
			throw new ExcepcionNegocio("El nombre de la cuenta asociada debe contener informacion");
		}
		if (nombreCuenta.length() > 30) {
			throw new ExcepcionNegocio("El nombre de la cuenta asociada no debe exceder los 30 caracteres");
		}
		permiso = daoPermiso.crear(permiso);
		if (permiso == null) {
			throw new ExcepcionNegocio("Problemas al crear el permiso en el Dao");
		}

		return permiso;

	}

	public Permiso verificacionesDatos(Permiso permiso) throws Exception {

		Usuario usuOrigen = permiso.getUsuarioOrigen();
		Usuario usuDestino = permiso.getUsuarioDestino();
		// Valida que los usuarios no sean nulos o sean invalidas sus identificaciones
		if (usuOrigen == null) {
			throw new ExcepcionNegocio("El usuario proveedor del permiso de la cuenta asociada es nulo");
		}
		if (usuOrigen.getEstado().getEstado().equals(ConstantesVtrack.ESTADO_INACTIVO)) {
			throw new ExcepcionNegocio("El usuario proveedor del permiso se encuentra inactivo");
		}
		if (usuOrigen.getIdUsuario() <= 0) {
			throw new ExcepcionNegocio("usuario proveedor del permiso tiene identificacion invalida");
		}
		if (usuDestino == null) {
			throw new ExcepcionNegocio("El usuario beneficiario del permiso es nulo");
		}
		if (usuDestino.getEstado().getEstado().equals(ConstantesVtrack.ESTADO_INACTIVO)) {
			throw new ExcepcionNegocio("El usuario beneficiario del permiso no se encuentra activo");
		}
		if (usuDestino.getIdUsuario() <= 0) {
			throw new ExcepcionNegocio("El usuario beneficiario del permiso tiene identificacion invalida");
		}
		if (usuDestino.getIdUsuario() == usuOrigen.getIdUsuario()) {
			// el usuario que concedio el permiso es el mismo que lo recibe
			throw new ExcepcionNegocio("Solo puede conceder permisos a otros usuarios. No a usted mismo");
		}
		// Obtengo el permiso y veo si es aprobado, ignorado o en espera
		String estado = permiso.getEstado().getEstado();
		if (estado == null) {
			throw new ExcepcionNegocio("No se asigno un tipo de estado");
		}
		// No creo que pase pero bueno
		if (estado.equals("")) {
			throw new ExcepcionNegocio("No se asigno valor al tipo de estado");
		}
		if (!estado.equals(ConstantesVtrack.ESTADO_EN_ESPERA) && !estado.equals(ConstantesVtrack.ESTADO_APROBADO)
				&& !estado.equals(ConstantesVtrack.ESTADO_IGNORADO)
				&& !estado.equals(ConstantesVtrack.ESTADO_INACTIVO)) {
			throw new ExcepcionNegocio("El tipo de estado asignado al permiso no es permitido");
		}
		DaoVTrack<Permiso> daoEstado = new DaoVTrack<Permiso>(entityManager, Permiso.class);
		Estado estadoCompleto = daoEstado.encontrarEstado(Permiso.class.getName(), estado);
		// Cambio por un estado real el estado que tiene el permiso, es decir con toda
		// la informacion
		permiso.setEstado(estadoCompleto);
		String tipoPermiso = permiso.getTipoPermiso();
		if (tipoPermiso == null) {
			throw new ExcepcionNegocio("No se asigno un tipo de permiso, de lectura o escritura");
		}
		if (tipoPermiso.equals("")) {
			throw new ExcepcionNegocio("No se asigno un valor al tipo de permiso, de lectura o escritura");
		}
		if (!tipoPermiso.equals(ConstantesVtrack.PERMISO_LECTURA)
				&& !tipoPermiso.equals(ConstantesVtrack.PERMISO_ESCRITURA)
				&& !tipoPermiso.equals(ConstantesVtrack.PERMISO_ASOCIADO)) {
			throw new ExcepcionNegocio("El tipo de permiso asignado no existe");
		}
		return permiso;
	}

	/**
	 * @descripcion Devuelve los permisos que tengo es decir los permisos recibidos
	 *              y que estan aprobados
	 * @author Valery Ibarra
	 * @fecha 1/11/2018
	 * @param usuarioOrigen Usuario que tiene permisos y se desea saber sobre cuales
	 *                      usuarios tiene dicho permiso
	 * @returns Lista de permisos en los que el usuario es origen
	 */
	public List<Permiso> darPermisosDestinoDeAprobado(Usuario usuarioOrigen) throws Exception {
		// Valida que el usuario no sea nulo y su identificacion
		if (usuarioOrigen == null) {
			throw new ExcepcionNegocio("El usuario que tiene o no permisos sobre otros es nulo");
		}
		if (usuarioOrigen.getIdUsuario() <= 0) {
			throw new ExcepcionNegocio("El usuario que tiene o no permisos presenta identificacion inválida");
		}
		DaoVTrack<Permiso> dao = new DaoVTrack<>(entityManager, Permiso.class);
		return dao.darPermisosDestinoDeAprobado(usuarioOrigen);
	}

	/**
	 * @descripcion Devuelve los permisos que tengo es decir los permisos recibidos,
	 *              aun los ignorados
	 * @author Valery Ibarra
	 * @fecha 21/11/2018
	 * @param usuarioOrigen Usuario que tiene permisos y se desea saber sobre cuales
	 *                      usuarios tiene dicho permiso
	 * @returns Lista de permisos en los que el usuario es origen
	 */
	public List<Permiso> darPermisosDestinoDe(Usuario usuarioOrigen) throws Exception {
		// Valida que el usuario no sea nulo y su identificacion
		if (usuarioOrigen == null) {
			throw new ExcepcionNegocio("El usuario que tiene o no permisos sobre otros es nulo");
		}
		if (usuarioOrigen.getIdUsuario() <= 0) {
			throw new ExcepcionNegocio("El usuario que tiene o no permisos presenta identificacion inválida");
		}
		DaoVTrack<Permiso> dao = new DaoVTrack<>(entityManager, Permiso.class);
		return dao.darPermisosDestinoDe(usuarioOrigen);
	}

	/**
	 * @descripcion Devuelve los permisos que concedio el usuario que entra como
	 *              parametro
	 * @author Valery Ibarra
	 * @fecha 21/11/2018
	 * @param usuarioDestino Usuario que ha concedido permisos
	 * @returns Lista de permisos que ha concedido el usuario que se ingresa
	 */
	public List<Permiso> darPermisosOrigenDe(Usuario usuarioDestino) throws Exception {
		// Valida que el usuario no sea nulo y su identificacion
		if (usuarioDestino == null) {
			throw new ExcepcionNegocio("El usuario que ingreso es nulo");
		}
		if (usuarioDestino.getIdUsuario() <= 0) {
			throw new ExcepcionNegocio("El usuario presenta identificacion inválida");
		}
		DaoVTrack<Permiso> dao = new DaoVTrack<>(entityManager, Permiso.class);
		return dao.darPermisosOrigenDe(usuarioDestino);
	}

	/**
	 * @descripcion Busca por medio del correo un usuario ya registrado y le otorga
	 *              permisos
	 * @author Valery Ibarra
	 * @fecha 28/11/2018
	 * @param permiso Tiene toda la informacion del permiso otorgado
	 * @param correo  Es el correo del usuario al que se le otorgaran permisos, es
	 *                decir, el que recibira
	 * @returns Lista de permisos que ha concedido el usuario que se ingresa
	 */
	public Permiso concederPermisoCorreo(Permiso permiso, String correo) throws Exception {
		DaoVTrack<Permiso> dao = new DaoVTrack<>(entityManager, Permiso.class);
		Cuenta cuenta = dao.encontrarCuentaPorCorreo(correo);
		if (cuenta == null) {
			throw new ExcepcionNegocio("No hay usuario registrado con el correo ingresado");
		}
		if (cuenta.getEstado().getEstado().equals(ConstantesVtrack.ESTADO_INACTIVO)) {
			throw new ExcepcionNegocio(
					"La cuenta a la que quiere otorgar permisos se encuentra inactiva(ha sido eliminada)");
		}
		// Usuario del correo ingresado
		Usuario usuarioOrigenC = cuenta.getUsuario();
		if (usuarioOrigenC == null) {
			throw new ExcepcionNegocio("No hay usuario para la cuenta");
		}
		if (usuarioOrigenC.getEstado().getEstado().equals(ConstantesVtrack.ESTADO_INACTIVO)) {
			throw new ExcepcionNegocio(
					"El usuario al que se quiere otorgar permisos se encuentra inactivo(ha sido eliminado)");
		}

		permiso.setUsuarioOrigen(usuarioOrigenC);
		// configura el permiso de acuerdo a la informacion dada por el usuario y
		// verifica lo ingresado
		permiso = configurarPermisoGeneral(permiso);

		return permiso;
	}

	/**
	 * @descripcion Devuelve los permisos que concedio el usuario ingresado y que
	 *              son aprobados
	 * @author Valery Ibarra
	 * @fecha 3/11/2018
	 * @param usuarioDestino Usuario que ha concedido permisos
	 * @returns Lista de permisos que ha concedido el usuario que se ingresa
	 */
	public List<Permiso> darPermisosOrigenDeAprobado(Usuario usuarioDestino) throws Exception {
		// Valida que el usuario no sea nulo y su identificacion
		if (usuarioDestino == null) {
			throw new ExcepcionNegocio("El usuario que ingreso es nulo");
		}
		if (usuarioDestino.getIdUsuario() <= 0) {
			throw new ExcepcionNegocio("El usuario presenta identificacion inválida");
		}
		DaoVTrack<Permiso> dao = new DaoVTrack<>(entityManager, Permiso.class);
		return dao.darPermisosOrigenDeAprobado(usuarioDestino);
	}
	
	/**
	 * @descripcion Devuelve los permisos que tengo es decir los permisos recibidos que estan en espera
	 * @author Valery Ibarra
	 * @fecha 04/12/2018
	 * @param usuarioOrigen Usuario que tiene permisos y se desea saber sobre cuales
	 *                      usuarios tiene dicho permiso en espera
	 * @returns Lista de permisos en los que el usuario es origen
	 */
	public List<Permiso> darPermisosDestinoDeEnEspera(Usuario usuarioOrigen) throws Exception {
		// Valida que el usuario no sea nulo y su identificacion
		if (usuarioOrigen == null) {
			throw new ExcepcionNegocio("El usuario que tiene o no permisos sobre otros es nulo");
		}
		if (usuarioOrigen.getIdUsuario() <= 0) {
			throw new ExcepcionNegocio("El usuario que tiene o no permisos presenta identificacion inválida");
		}
		DaoVTrack<Permiso> dao = new DaoVTrack<>(entityManager, Permiso.class);
		return dao.darPermisosDestinoDeEnEspera(usuarioOrigen);
	}

}