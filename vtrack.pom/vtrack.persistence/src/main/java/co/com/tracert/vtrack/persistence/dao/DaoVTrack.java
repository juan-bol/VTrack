package co.com.tracert.vtrack.persistence.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.entities.Auditoria;
import co.com.tracert.vtrack.model.entities.CentroVacunacion;
import co.com.tracert.vtrack.model.entities.Ciudad;
import co.com.tracert.vtrack.model.entities.Cuenta;
import co.com.tracert.vtrack.model.entities.DosisAplicada;
import co.com.tracert.vtrack.model.entities.Esquema;
import co.com.tracert.vtrack.model.entities.EsquemaAgregado;
import co.com.tracert.vtrack.model.entities.Estado;
import co.com.tracert.vtrack.model.entities.Menu;
import co.com.tracert.vtrack.model.entities.Pais;
import co.com.tracert.vtrack.model.entities.Permiso;
import co.com.tracert.vtrack.model.entities.Region;
import co.com.tracert.vtrack.model.entities.Rol;
import co.com.tracert.vtrack.model.entities.TipoDocumentoPais;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.entities.Vacuna;
import co.com.tracert.vtrack.model.entities.VacunaOculta;

/**
 * @Nombre DaoVtrack
 * @Fecha 02/10/2018
 * @Descripcion esta clase contiene consultas especificas que se realizan sobre
 *              la base de datos
 * @author Juan David Bolanios, Diego Lamus, Santiago Restrepo
 */
public class DaoVTrack<T> extends DaoGenerico<T> {

	public DaoVTrack(EntityManager entityManager, Class<T> tipoClase) {
		super(entityManager, tipoClase);
	}

	/**
	 * @author Juan David Bolanios, Diego Lamus, Ana Arango
	 * @param usuario usuario al cual se le van a sugerir esquemas
	 * @param meses   Edad en meses del usuario
	 * @return Lista de esquemas de vacunacion sugeridos
	 * @throws Exception
	 */
	public List<Esquema> sugerirEsquemas(Usuario usuario, BigDecimal meses, Pais otroPais) {
		// Construir consulta
		String jpql = "SELECT esq FROM Esquema esq";
		boolean primerFiltro = true;
		// Verificar si se debe filtrar por edad
		if (meses != null) {
			jpql += " WHERE esq.mesInicio<=" + meses.intValue() + " AND esq.mesFin>=" + meses.intValue();
			primerFiltro = false;
		}
		// Verificar filtro por genero
		if (usuario.getGenero() != null && usuario.getGenero() != "") {
			jpql += (primerFiltro) ? " WHERE (esq.genero='" + usuario.getGenero() + "' OR esq.genero='A')"
					: " AND (esq.genero='" + usuario.getGenero() + "' OR esq.genero='A')";
			primerFiltro = false;
		}
		// Verificar filtro por pais
		Pais paisPorBuscar = otroPais;
		if (otroPais == null && usuario.getPai() != null) {
			paisPorBuscar = usuario.getPai();
		}
		if (paisPorBuscar != null && paisPorBuscar.getIdPais() > 0) {
			jpql += (primerFiltro) ? " WHERE esq.pai.idPais=" + paisPorBuscar.getIdPais()
					: "AND esq.pai.idPais=" + paisPorBuscar.getIdPais();
			primerFiltro = false;
		}
		// Eliminar esquemas agregados por el usuario
		jpql += (primerFiltro)
				? " WHERE esq NOT IN (  SELECT esq2 FROM Esquema esq2"
						+ " INNER JOIN EsquemaAgregado esqAgg ON esq2=esqAgg.esquema"
						+ " INNER JOIN Usuario usu ON esqAgg.usuario=usu" + " WHERE usu.idUsuario="
						+ usuario.getIdUsuario() + ")"
				: " AND esq NOT IN ( SELECT esq2 FROM Esquema esq2"
						+ " INNER JOIN EsquemaAgregado esqAgg ON esq2=esqAgg.esquema"
						+ " INNER JOIN Usuario usu ON esqAgg.usuario=usu" + " WHERE usu.idUsuario="
						+ usuario.getIdUsuario() + ")";
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<Esquema> esquemas = (List<Esquema>) query.getResultList();
		return esquemas;
	}

	/**
	 * @descripcion Retorna una lista de menu items de acuerdo al rol del usuario
	 * @param Usuario Usuario al cual se mostrara el menu
	 * @return menuItems Una lista con los items del menu de la aplicacion
	 *         coherentes con el rol del usuario logueado
	 * @author Diego Lamus
	 * @Fecha 27/10/2018
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> listarMenuItems(Usuario usuario) {
		List<Menu> menuItems = new ArrayList<>();
		String jpql = "SELECT men FROM Menu men " + " INNER JOIN MenuRol menRol ON men=menRol.menu"
				+ " INNER JOIN Rol rol ON rol=menRol.rol" + " INNER JOIN RolUsuario rolUsu ON rol=rolUsu.rol"
				+ " WHERE rolUsu.usuario.idUsuario=" + usuario.getIdUsuario();
		// TODO desquemar usuario (diego lamus)
		Query consulta = entityManager.createQuery(jpql);
		menuItems = (List<Menu>) consulta.getResultList();
		return menuItems;
	}

	/**
	 * @Descripcion Metodo que se encarga de dar el carne de vacunacion del usuario.
	 * @Fecha 23/10/2018
	 * @author Victor Potes, Diego Lamus
	 * @param usuario usuario al cual se le dara el carne de vacunacion
	 * @return esquemas lista de esquemas que se encuentran en el carne de vacuacion
	 *         del usuario
	 */

	public List<Esquema> darCarneVacunacion(Usuario usuario) {
		String jpql = "SELECT esq FROM Esquema esq" + " INNER JOIN EsquemaAgregado esqAgg ON esq=esqAgg.esquema"
				+ " WHERE esqAgg.usuario.idUsuario=" + usuario.getIdUsuario();
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<Esquema> esquemas = (List<Esquema>) query.getResultList();
		return esquemas;
	}

	/**
	 * @descripcion devuelve la lista de los regiones dado un determinado pais del
	 *              sistema Vtrack
	 * @author Juan Bolaños
	 * @return Lista de regiones pertenecientes a un pais
	 * @throws Exception
	 */
	public List<Region> listarRegiones(Pais pais) {
		String jpql = "SELECT reg FROM Region reg WHERE reg.pai.idPais='" + pais.getIdPais() + "'";
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<Region> regiones = (List<Region>) query.getResultList();
		return regiones;
	}

	/**
	 * @description devuelve la lista de las ciudades dado una determinada región
	 *              del sistema Vtrack
	 * @author Juan Bolaños
	 * @return Lista de las ciudades pertenecientes a una region
	 * @throws Exception
	 */
	public List<Ciudad> listarCiudades(Region region) {
		String jpql = "SELECT ciu FROM Ciudad ciu " + " INNER JOIN Region reg ON reg=ciu.region "
				+ " WHERE ciu.region.idRegion='" + region.getIdRegion() + "'";
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<Ciudad> ciudades = (List<Ciudad>) query.getResultList();
		return ciudades;
	}

	/**
	 * @author Diego lamus
	 * @descripción se deberá traer el usuario a traves de la cuenta basado en su
	 *              correo y contraseña recibidos por parametro.
	 * @returns Cuenta cuenta del usuario que hace inicio de sesion
	 * @throws Exception En caso que no se encuentre se al usuario o su contraseña
	 *                   no sea valida
	 * @fecha 27/10/2018
	 */
	public Usuario autenticarUsuario(Cuenta cuentaUsuario) {
		String jpql = "SELECT usu FROM Usuario usu " + " WHERE usu.cuenta.correo='" + cuentaUsuario.getCorreo() + "'"
				+ " AND usu.cuenta.contrasenia='" + cuentaUsuario.getContrasenia() + "'";
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
//		Usuario usuario = (Usuario) query.getSingleResult(); Landa TODO
		List usuarios = query.getResultList();
		Usuario usu = null;
		if (!usuarios.isEmpty()) {
			usu = (Usuario) usuarios.get(0);
		}

		return usu;
	}

	/**
	 * @author Victor Potes
	 * @descripción Busca la cuenta de acuerdo a su correo
	 * @parametro correo correo de la cuenta que se piensa retornar
	 * @returns Cuenta cuenta que tiene el correo ingresado por parametro
	 * @throws Exception En caso que no se encuentre encuentre la cuenta, significa
	 *                   que aun no se ha registrado con ese correo
	 * @fecha 27/10 /2018
	 */
	public Cuenta encontrarCuentaPorCorreo(String correo) {
		String jpql = "SELECT cue FROM Cuenta cue " + " WHERE cue.correo='" + correo + "'";
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
//		Usuario usuario = (Usuario) query.getSingleResult(); Landa TODO
		List cuentas = query.getResultList();
		Cuenta cue = null;
		if (!cuentas.isEmpty()) {
			cue = (Cuenta) cuentas.get(0);
		}

		return cue;
	}

	/**
	 * @author Victor Potes
	 * @descripción Obtiene el estado activo de la clase
	 * @parametros nombreClase nombre de la clase, se obtiene con el
	 *             NombreClase.class.getName()
	 * @parametros estado si es true el estado de la clase es activo, si es false es
	 *             inactivo
	 * @returns Estado de la clase
	 * @throws Exception Si no encuentra el estado
	 * @fecha 31/10/2018
	 */
	public Estado encontrarEstado(String nombreClase, String estadoEntity) {
		String arreglo[] = nombreClase.split("[.]");
		String nombre = arreglo[arreglo.length - 1];
		for (int i = 1; i < nombre.length(); i++) {
			if (nombre.charAt(i) > 64 && nombre.charAt(i) < 91) {
				nombre = nombre.substring(0, i) + "_" + nombre.substring(i, nombre.length());
				i++;
			}
		}
		String jpql = "SELECT est FROM Estado est" + " INNER JOIN TipoEstado tipoEst ON tipoEst=est.tipoEstado"
				+ " WHERE tipoEst.nombre = '" + nombre.toUpperCase() + "' AND est.estado='" + estadoEntity + "'";
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<Estado> estados = query.getResultList();
		Estado est = null;
		if (!estados.isEmpty()) {
			est = (Estado) estados.get(0);
		}

		return est;
	}

	/**
	 * @descripcion devuelve la lista de los Tipo de documentos de un determinado
	 *              pais del sistema Vtrack
	 * @author Juan Bolaños
	 * @return Lista los tipos de documento pertenecientes a un pais
	 * @throws Exception
	 */
	public List<TipoDocumentoPais> listarTipoDocumentos(Pais pais) {
		String jpql = "SELECT tipo FROM TipoDocumentoPais tipo WHERE tipo.pai.idPais=" + pais.getIdPais();
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<TipoDocumentoPais> tiposDocumento = (List<TipoDocumentoPais>) query.getResultList();
		return tiposDocumento;
	}

	/**
	 * @Descripcion devuelve una lista de vacunas que ha ocultado un usuario
	 * @param usuario usuario sobre el cual se consultaran las vacunas ocultas
	 * @author Diego Lamus
	 * @return vacunas lista de vacunas que tiene ocultas el usuario
	 */
	public List<Vacuna> darVacunasOcultas(Usuario usuario) {
		String jpql = "SELECT vac FROM Vacuna vac" + " INNER JOIN VacunaOculta vacOc ON vac=vacOc.vacuna"
				+ " WHERE vacOc.usuario.idUsuario=" + usuario.getIdUsuario();
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<Vacuna> vacunas = (List<Vacuna>) query.getResultList();
		return vacunas;
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
	public List<Permiso> darPermisosDestinoDeAprobado(Usuario usuarioOrigen) {
		String jpql = "SELECT per FROM Permiso per " + " WHERE per.usuarioOrigen.idUsuario='"
				+ usuarioOrigen.getIdUsuario() + "'" + " AND per.estado.estado='" + ConstantesVtrack.ESTADO_APROBADO
				+ "'";
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<Permiso> permisos = (List<Permiso>) query.getResultList();
		return permisos;
	}

	/**
	 * @description Retorna las dosis aplicada de un usuario
	 * @author Diego Lamus
	 * @param usuario usuario sobre el cual se devuelven las dosis aplicadas
	 * @fecha 03/10/2018
	 * @return dosisAplicada lista de dosis que tiene aplicadas el usuari0
	 */
	public List<DosisAplicada> darDosisAplicadas(Usuario usuario) {
		String jpql = "SELECT dos FROM DosisAplicada dos " + " WHERE dos.usuario.idUsuario=" + usuario.getIdUsuario();
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<DosisAplicada> dosisAplicada = (List<DosisAplicada>) query.getResultList();
		return dosisAplicada;
	}

	/**
	 * @descripcion Retorna la cuenta asociada a un usuario
	 * @param cuenta cuenta de la cual se quiere obtener el usuario
	 * @return usuario pertenceciente a la cuenta
	 */
	public Usuario consultarUsuarioPorCuenta(Cuenta cuenta) {
		String jpql = "SELECT usu FROM Usuario usu WHERE usu.cuenta.idCuenta=" + cuenta.getIdCuenta();
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<Usuario> usuarios = (List<Usuario>) query.getResultList();
		Usuario usu = null;
		if (!usuarios.isEmpty()) {
			usu = (Usuario) usuarios.get(0);
		}
		return usu;
	}

	/**
	 * @descripcion Retorna la cuenta asociada a un usuario
	 * @author Victor Potes
	 * @param cuenta cuenta de la cual se quiere obtener el usuario
	 * @return usuario pertenceciente a la cuenta
	 */
	public Usuario consultarPorDocumentoUnico(Usuario usuario) {
		String jpql = "SELECT usu FROM Usuario usu WHERE usu.tipoDocumentoPai.idTipoDocPais="
				+ usuario.getTipoDocumentoPai().getIdTipoDocPais() + " AND usu.numeroDocumento=" + "'"
				+ usuario.getNumeroDocumento() + "'";
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<Usuario> usuarios = (List<Usuario>) query.getResultList();
		Usuario usu = null;
		if (!usuarios.isEmpty()) {
			usu = (Usuario) usuarios.get(0);
		}
		return usu;
	}

	/**
	 * @descripcion Este metodo determina si un esquema agregado por un usuario
	 *              tiene todas sus vacunas ocultas
	 * @author Juan Bolaños
	 * @fecha 9/11/18
	 * @param usuario que tiene el esquema
	 * @param esquema del usuario del cual se quiere conocer si sus vacunas estan
	 *                todas ocultas
	 * @return boolean que retorna true si todas las vacunas estan ocutlas y false
	 *         en el caso contrario
	 */
	public boolean verificarEsquemaAgregadoVacio(Usuario usuario, Esquema esquema) {
		String jpql = "SELECT vac FROM Esquema esq " + " INNER JOIN VacunaEsquema vacEsq ON esq=vacEsq.esquema"
				+ " INNER JOIN Vacuna vac ON vac=vacEsq.vacuna " + " WHERE esq.idEsquema=" + esquema.getIdEsquema();
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<Vacuna> vacunasEsquema = (List<Vacuna>) query.getResultList();

		jpql = "SELECT vao.vacuna FROM VacunaOculta vao WHERE vao.usuario.idUsuario=" + usuario.getIdUsuario();
		// Instanciar consulta
		query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<Vacuna> vacunasOcultas = (List<Vacuna>) query.getResultList();

		// Se verifica que todas las vacunas de un esquema agregado se encuentren en el
		// conjunto de vacunas ocultas de un usuario
		boolean esquemaVacio = false;
		if (vacunasOcultas.containsAll(vacunasEsquema)) {
			esquemaVacio = true;
		}
		return esquemaVacio;

	}

	/**
	 * @descripcion Retorna el esquema agregado perteneciente a un usuario y un
	 *              esquema
	 * @author Juan Bolaños
	 * @fecha 9/11/18
	 * @param usuario
	 * @param esquema
	 * @return esquema agregado por el usuario
	 */
	public EsquemaAgregado encontrarEsquemaAgregadoPorUsuarioEsquema(Usuario usuario, Esquema esquema) {
		String jpql = "SELECT esqAgr FROM EsquemaAgregado esqAgr WHERE esqAgr.esquema.idEsquema="
				+ esquema.getIdEsquema() + " AND esqAgr.usuario=" + usuario.getIdUsuario();
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<EsquemaAgregado> resultado = (List<EsquemaAgregado>) query.getResultList();
		EsquemaAgregado esquemaAgregado = null;
		if (!resultado.isEmpty()) {
			esquemaAgregado = resultado.get(0);
		}
		return esquemaAgregado;
	}

	/**
	 * @descripcion Se recuperan todas las vacunas eliminadas por un usuario
	 * @author Juan Bolaños
	 * @fecha 10/11/18
	 * @param usuario que recuperara las vacunas
	 */
	public void recuperarVacunasOcultas(Usuario usuario) {
		String jpql = "DELETE FROM VacunaOculta vao WHERE vao.usuario.idUsuario=" + usuario.getIdUsuario();
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		query.executeUpdate();
	}

	/**
	 * @descripcion Devuelve los permisos que concedio el usuario que entra como
	 *              parametro
	 * @author Valery Ibarra
	 * @fecha 21/11/2018
	 * @param usuarioDestino Usuario que ha concedido permisos
	 * @returns Lista de permisos que ha concedido el usuario que se ingresa
	 */
	public List<Permiso> darPermisosOrigenDe(Usuario usuarioDestino) {
		String jpql = "SELECT per FROM Permiso per " + " WHERE per.usuarioDestino.idUsuario='"
				+ usuarioDestino.getIdUsuario() + "'" + "AND per.estado.estado!='" + ConstantesVtrack.ESTADO_INACTIVO
				+ "'";
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<Permiso> permisos = (List<Permiso>) query.getResultList();
		return permisos;
	}

	/**
	 * @descripcion Devuelve los permisos que tengo es decir los permisos recibidos,
	 *              aun los ignorados menos los inactivos y los tipos de permisos
	 *              asociados
	 * @author Valery Ibarra
	 * @fecha 05/12/2018
	 * @param usuarioOrigen Usuario que tiene permisos y se desea saber sobre cuales
	 *                      usuarios tiene dicho permiso
	 * @returns Lista de permisos en los que el usuario es origen
	 */
	public List<Permiso> darPermisosDestinoDe(Usuario usuarioOrigen) {
		String jpql = "SELECT per FROM Permiso per " + " WHERE per.usuarioOrigen.idUsuario='"
				+ usuarioOrigen.getIdUsuario() + "'" + "AND per.estado.estado!='" + ConstantesVtrack.ESTADO_INACTIVO
				+ "'" + "AND per.tipoPermiso!='" + ConstantesVtrack.PERMISO_ASOCIADO + "'";

		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<Permiso> permisos = (List<Permiso>) query.getResultList();
		return permisos;
	}

	/**
	 * @author Valery Ibarra
	 * @fecha 21/11/2018
	 * @descripción Busca el rol de acuerdo al nombre
	 * @parametro nombre nombre del rol
	 * @returns Rol rol que tiene el nombre que se ingreso cuenta que tiene el
	 *          correo ingresado por parametro
	 * @throws Exception En caso que no se encuentre encuentre, significa que aun no
	 *                   se ha registrado un rol con ese nombre
	 */
	public Rol encontrarRolPorNombre(String nombre) {
		String jpql = "SELECT rol FROM Rol rol " + " WHERE rol.nombre='" + nombre + "'";
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		List roles = query.getResultList();
		Rol rol = null;
		if (!roles.isEmpty()) {
			rol = (Rol) roles.get(0);
		}

		return rol;
	}

	/**
	 * @descripcion Sugiere centros de vacunación dependiendo de la ciudad pasada
	 *              por parámetro
	 * @author Ana Arango
	 * @fecha 21/11/2018
	 * @param ciudad ciudad en la que los centros de vacunación deben estar
	 * @throws Exception
	 */
	public List<CentroVacunacion> sugerirCentrosVacunacionCercanos(Ciudad ciudad) {

		String jpql = "SELECT centro FROM CentroVacunacion centro " + " WHERE centro.ciudad=" + ciudad.getIdCiudad();
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<CentroVacunacion> centrosVacunacion = (List<CentroVacunacion>) query.getResultList();
		return centrosVacunacion;

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
	public List<Esquema> encontrarEsquemasPorPais(Long idPais) {

		String sql = "select * " + "from esquema e " + "where e.id_pais = " + idPais + " and e.id_esquema "
				+ "not in (select ea.id_esquema from esquema_agregado ea)";
		// Instanciar consulta
		Query query = entityManager.createNativeQuery(sql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<Esquema> esquemas = (List<Esquema>) query.getResultList();
		return esquemas;

	}

	/**
	 * @author Valery Ibarra
	 * @fecha 3/12/2018
	 * @descripción Busca los permisos con el usuario origen y el usario destino
	 *              dados, deberia ser solo un permiso
	 * @parametro usuarioOrigen usuario que tiene permisos sobre el otro
	 * @parametro usuarioDestino usuario al que usuarioDestino le dio permiso para
	 *            acceder, el que concedio
	 * @returns listaPermisos Lista de permisos que tienen usuario origen en origen
	 *          y usuario destino en destino
	 * @throws Exception En caso que no se encuentre el permiso, significa que aun
	 *                   no
	 * 
	 */
	public List<Permiso> darPermisoConUsuarios(Usuario usuarioOrigen, Usuario usuarioDestino) {
		String jpql = "SELECT per FROM Permiso per " + " WHERE per.usuarioOrigen.idUsuario='"
				+ usuarioOrigen.getIdUsuario() + "'" + " AND per.usuarioDestino.idUsuario='"
				+ usuarioDestino.getIdUsuario() + "'";
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<Permiso> permisos = (List<Permiso>) query.getResultList();
		return permisos;
	}

	/**
	 * @descripcion Devuelve los permisos que concedio el usuario ingresado y que
	 *              son aprobados
	 * @author Valery Ibarra
	 * @fecha 3/11/2018
	 * @param usuarioDestino Usuario que ha concedido permisos
	 * @returns Lista de permisos que ha concedido el usuario que se ingresa
	 */
	public List<Permiso> darPermisosOrigenDeAprobado(Usuario usuarioDestino) {
		String jpql = "SELECT per FROM Permiso per " + " WHERE per.usuarioDestino.idUsuario='"
				+ usuarioDestino.getIdUsuario() + "'" + "AND per.estado.estado='" + ConstantesVtrack.ESTADO_APROBADO
				+ "'";
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<Permiso> permisos = (List<Permiso>) query.getResultList();
		return permisos;
	}

	/**
	 * @descripcion Devuelve los permisos que tengo es decir los permisos recibidos
	 *              que estan en espera
	 * @author Valery Ibarra
	 * @fecha 04/12/2018
	 * @param usuarioOrigen Usuario que tiene permisos y se desea saber sobre cuales
	 *                      usuarios tiene dicho permiso en espera
	 * @returns Lista de permisos en los que el usuario es origen
	 */
	public List<Permiso> darPermisosDestinoDeEnEspera(Usuario usuarioOrigen) {
		String jpql = "SELECT per FROM Permiso per " + " WHERE per.usuarioOrigen.idUsuario='"
				+ usuarioOrigen.getIdUsuario() + "'" + "AND per.estado.estado='" + ConstantesVtrack.ESTADO_EN_ESPERA
				+ "'";
		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<Permiso> permisos = (List<Permiso>) query.getResultList();
		return permisos;
	}

	/**
	 * @descripcion Devuelve las auditorias que se han hecho con respecto a la modificación o creación de un
	 * usuario
	 * @author Victor Potes
	 * @fecha 10/12/2018
	 * @param usuarioCreador usuario quien realizó la acción de modificar el perfil
	 * @param usuarioAfectado usuario a quien se le modificó el perfil
	 * @returns Lista de las auditorías que cumplen con la descripción
	 */
	public List<Auditoria> auditoriaMasCercanaPorFecha(Usuario usuarioCreador, Usuario usuarioAfectado) {

		String jpql = "SELECT au FROM Auditoria au WHERE (au.accion= 'M' OR au.accion ='C') AND au.nombreTablaAfectada="
				+ "'" + Usuario.class.getSimpleName() + "'" + " AND au.usuario.idUsuario="
				+ usuarioCreador.getIdUsuario() + "AND au.idTablaAfectada=" + usuarioAfectado.getIdUsuario()
				+ " ORDER BY au.fecha";

		// Instanciar consulta
		Query query = entityManager.createQuery(jpql);
		// Ejecutar consulta
		@SuppressWarnings("unchecked")
		List<Auditoria> auditorias = (List<Auditoria>) query.getResultList();

		return auditorias;

	}

}
