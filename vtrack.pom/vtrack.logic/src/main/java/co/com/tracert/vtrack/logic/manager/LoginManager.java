package co.com.tracert.vtrack.logic.manager;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

import com.google.common.hash.Hashing;

import co.com.tracert.vtrack.model.entities.Auditoria;
import co.com.tracert.vtrack.model.entities.Cuenta;
import co.com.tracert.vtrack.model.entities.Menu;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.exceptions.ExcepcionNegocio;
import co.com.tracert.vtrack.persistence.dao.DaoVTrack;

public class LoginManager {

	private EntityManager entityManager;

	public LoginManager(EntityManager entityManager) {

		this.entityManager = entityManager;
	}

	/**
	 * @author Ana Arango, Santiago Restrepo Prado
	 * @param objeto usuario que se quiere consultar en la base de datos
	 * @return Listado de menus correspondientes al usuario
	 * @throws Exception si se genera algun error en la base de datos o el id del
	 *                   usuario no es valida
	 */

	public List<Menu> consultarMenuUsuario(Usuario usuario) throws Exception {
		if (usuario == null) {
			throw new Exception("No se pudo cargar el menu, usuario no especificado");
		}
		if (usuario.getIdUsuario() <= 0) {
			throw new Exception("No se pudo cargar el menu, usuario con identificacion invalida");
		}
		DaoVTrack<Menu> dao = new DaoVTrack<Menu>(entityManager, Menu.class);
		List<Menu> listaMenus = dao.listarMenuItems(usuario);
		if (listaMenus == null) {
			throw new Exception("El usuario no tiene menus para el rol especificado");
		}
		return listaMenus;
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
	public Usuario autenticarUsuario(Cuenta cuentaUsuario) throws Exception {
		// verificar que la cuenta de usuario no sea nula
		if (cuentaUsuario == null) {
			throw new ExcepcionNegocio("La cuenta de usuario es nula");
		}
		// verificar que el correo de la cuenta de usuario no sea nula
		if (cuentaUsuario.getCorreo() == null || cuentaUsuario.getCorreo().trim().equals("")) {
			throw new ExcepcionNegocio("Ingrese un correo electrónico");
		}
		// verificar que la contrasenia ingresada no sea nula ni vacia
		if (cuentaUsuario.getContrasenia() == null || cuentaUsuario.getContrasenia().trim().equals("")) {
			throw new ExcepcionNegocio("Ingrese una contraseña");
		}
		// TODO encriptar contrasenia de cuentaUsuario
		// Buscar la cuenta de usuario 
		DaoVTrack<Usuario> dao = new DaoVTrack<>(entityManager, Usuario.class);
		Cuenta existente = dao.encontrarCuentaPorCorreo(cuentaUsuario.getCorreo());
		if (existente == null) {
			throw new ExcepcionNegocio("El correo " + cuentaUsuario.getCorreo() + " no está registrado"
					+ " en el sistema. Resgístrate para poder ingresar.");
		}
		// cifrar la contrasenia del usuario que entra por parametro
		String sha256hex = Hashing.sha256()
				  .hashString(cuentaUsuario.getContrasenia(), StandardCharsets.UTF_8)
				  .toString();
		cuentaUsuario.setContrasenia(sha256hex);
		// autenticar al usuario
		Usuario usu = dao.autenticarUsuario(cuentaUsuario);
		if (usu==null) {
			throw new ExcepcionNegocio("La contraseña ingresada es incorrecta, verifícala e intenta de nuevo.");
		}
		
		usu.setCuenta(actualizarCuentaInicioSesion(usu.getCuenta()));
		return usu;
	}
	
	/**
	 * @descripcion Actualiza la fecha de inicio de sesión de una cuenta
	 * @author Victor Potes
	 * @fecha 09/12/2018
	 * @param cuenta cuenta a la que se le actualizará la fecha de inicio de sesión
	 * @throws Exception si no ocurre un error de actualización
	 */
	private Cuenta actualizarCuentaInicioSesion(Cuenta cuenta) throws Exception {
		
		cuenta.setFechaInicioSesion(new Date());
		DaoVTrack<Cuenta> daoCuenta = new DaoVTrack<>(entityManager, Cuenta.class);
		daoCuenta.actualizar(cuenta);
		return cuenta;
		
	}
	
	/**
	 * @descripcion Realiza la auditoria de la autenticación de un usuario
	 * @author Victor Potes
	 * @fecha 09/12/2018
	 * @param cuentaAnteriorLogin Cuenta que contiene la fecha de inicio de sesión anterior
	 * @param cuentaLogin Cuenta que contiene la fecha de inicio de sesión actual
	 * @throws Exception si no es posible auditar la autenticación del usuario
	 */
	public void auditarAutenticacion(Cuenta cuentaAnteriorLogin, Cuenta cuentaLogin) throws Exception {
		
		Auditoria auditarRegistro = new Auditoria();

		//Para el caso del registro, ya que no tendra un inicio de sesion anterior
		if(cuentaAnteriorLogin!=null) {
			String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(cuentaAnteriorLogin.
					getFechaInicioSesion());
			String informacionAnteriorRegistro = ""+cuentaAnteriorLogin.getIdCuenta()+","
					+cuentaAnteriorLogin.getUsuario().getIdUsuario()+",";
			informacionAnteriorRegistro = informacionAnteriorRegistro.concat(date);	
			
			auditarRegistro.setValorAnterior(informacionAnteriorRegistro);
		}
		else {
			auditarRegistro.setValorAnterior(null);
		}

		String informacionRegistro = ""+cuentaLogin.getIdCuenta()+","+cuentaLogin.getUsuario().getIdUsuario()+",";
		
		String dateA = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(cuentaLogin.getFechaInicioSesion().
				getTime());
		informacionRegistro = informacionRegistro.concat(dateA);	

		auditarRegistro.setFecha(new Date());
		auditarRegistro.setAccion("I");
		auditarRegistro.setNombreTablaAfectada(Cuenta.class.getSimpleName());
		auditarRegistro.setIdTablaAfectada(new BigDecimal(cuentaLogin.getIdCuenta()));
		auditarRegistro.setUsuario(cuentaLogin.getUsuario());
		auditarRegistro.setValorNuevo(informacionRegistro);
		

		DaoVTrack<Auditoria> daoAuditoria = new DaoVTrack<>(entityManager, Auditoria.class);
		daoAuditoria.crear(auditarRegistro);
		
	}
	
	/**
	 * @descripcion Realiza la auditoria de la cerrar la sesión del usuario
	 * @author Victor Potes
	 * @fecha 09/12/2018
	 * @param cuentaAnteriorCerrarSesion Cuenta que contiene la fecha de cerrar sesión anterior
	 * @param cuentaCerrarSesion Cuenta que contiene la fecha de cerrar sesión actual
	 * @throws Exception si no es posible auditar el cerrado de sesión del usuario
	 */
	public void auditarCerrarSesion(Cuenta cuentaAnteriorCerrarSesion, Cuenta cuentaCerrarSesion) throws Exception {
		
		Auditoria auditarRegistro = new Auditoria();

		//Para el caso del registro, ya que no tendra un inicio de sesion anterior
		if(cuentaAnteriorCerrarSesion.getFechaCierreSesion()!=null) {
			String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(cuentaAnteriorCerrarSesion.
					getFechaCierreSesion());
			String informacionAnteriorRegistro = ""+cuentaAnteriorCerrarSesion.getIdCuenta()+","+
					cuentaAnteriorCerrarSesion.getUsuario().
					getIdUsuario()+",";
			informacionAnteriorRegistro = informacionAnteriorRegistro.concat(date);	
			
			auditarRegistro.setValorAnterior(informacionAnteriorRegistro);
		}
		else {
			auditarRegistro.setValorAnterior(null);
		}

		String informacionRegistro = ""+cuentaCerrarSesion.getIdCuenta()+","+cuentaCerrarSesion.getUsuario().
				getIdUsuario()+",";
		
		String dateA = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(cuentaCerrarSesion.
				getFechaCierreSesion().getTime());
		informacionRegistro = informacionRegistro.concat(dateA);	

		auditarRegistro.setFecha(new Date());
		auditarRegistro.setAccion("O");
		auditarRegistro.setNombreTablaAfectada(Cuenta.class.getSimpleName());
		auditarRegistro.setIdTablaAfectada(new BigDecimal(cuentaCerrarSesion.getIdCuenta()));
		auditarRegistro.setUsuario(cuentaCerrarSesion.getUsuario());
		auditarRegistro.setValorNuevo(informacionRegistro);
		

		DaoVTrack<Auditoria> daoAuditoria = new DaoVTrack<>(entityManager, Auditoria.class);
		daoAuditoria.crear(auditarRegistro);
		
		
	}
	
	/**
	 * @descripcion actualiza la fecha de la cuenta, se usa cuando se vaya a cerrar sesión
	 * @author Victor Potes
	 * @fecha 09/12/2018
	 * @param cuenta cuenta a la que se le actualizará la fecha de cerrado de sesión
	 * @throws Exception si no es posible actualizar la fecha de cerrar sesión de la cuenta
	 */
	public Cuenta actualizarFechaCerrarSesion(Cuenta cuenta) throws Exception{
		
		cuenta.setFechaCierreSesion(new Date());
		DaoVTrack<Cuenta> dao = new DaoVTrack<>(entityManager, Cuenta.class);
		cuenta = dao.actualizar(cuenta);
		
		return cuenta;
		
	}

	

}
