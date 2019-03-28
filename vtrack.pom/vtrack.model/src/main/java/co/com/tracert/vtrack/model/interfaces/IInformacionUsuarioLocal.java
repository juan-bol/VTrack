package co.com.tracert.vtrack.model.interfaces;

import java.util.List;

import co.com.tracert.vtrack.model.entities.Cuenta;
import co.com.tracert.vtrack.model.entities.Menu;
import co.com.tracert.vtrack.model.entities.Notificacion;
import co.com.tracert.vtrack.model.entities.Permiso;
import co.com.tracert.vtrack.model.entities.Usuario;

public interface IInformacionUsuarioLocal {

	public Usuario consultarUsuario(long id) throws Exception;
	public List<Menu> consultarMenuUsuario(Usuario usuario) throws Exception;
	public Usuario iniciarSesion(Cuenta cuentaUsuario) throws Exception;
	public Usuario crearUsuario(Usuario usuario) throws Exception;
	public Cuenta crearCuenta(Cuenta cuenta) throws Exception;
	public List<Permiso> darPermisosDestinoDeAprobado(Usuario usuarioOrigen) throws Exception;
	public Cuenta modificarCuenta(Cuenta cuenta) throws Exception;
	public Usuario modificarUsuario(Usuario usuario) throws Exception;
	public Usuario vincularCuentaUsuario(Cuenta cuenta, Usuario usuario) throws Exception;
	public Cuenta encontrarCuentaPorCorreo (String correo) throws Exception;
	public void modificarNotificacion(Notificacion notificacion) throws Exception;
	public Notificacion crearNotificacion(Notificacion notificacion) throws Exception;
	public List<Permiso> darPermisosOrigenDe(Usuario usuarioDestino) throws Exception;
	public List<Permiso> darPermisosDestinoDe(Usuario usuarioOrigen) throws Exception;
	public Permiso configurarPermisoGeneral(Permiso permiso) throws Exception;
	public Permiso configurarPermisoAsociado(Permiso permiso) throws Exception;
	public Usuario crearCuentaUsuario(Usuario usuario, Cuenta centa) throws Exception;
	public Permiso concederPermisoCorreo(Permiso permiso, String correo) throws Exception;
	public void auditarRegistro(Usuario usuarioRegistrado) throws Exception;
	public List<Permiso> darPermisosDestinoDeEnEspera(Usuario usuarioOrigen) throws Exception;
	public void auditarAutenticacion(Cuenta cuentaAnteriorLogin, Cuenta cuentaInicioSesion) throws Exception;
	public void auditarCerrarSesion(Cuenta cuentaAnteriorCerrarSesion, Cuenta cuentaCerrarSesion) throws Exception;
	public Cuenta actualizarFechaCerrarSesion(Cuenta cuenta) throws Exception;
	public void auditarCrearUsuarioAsociado(Usuario usuarioCreador, Usuario usuarioRegistrado) throws Exception;
	public void auditarModificarPerfil (Usuario usuarioAnterior, Usuario usuarioModificado, Usuario usuarioCreador, boolean cambioContrasenia) throws Exception;


}
