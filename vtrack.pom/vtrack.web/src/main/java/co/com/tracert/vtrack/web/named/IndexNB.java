package co.com.tracert.vtrack.web.named;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.entities.Cuenta;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.web.businessdelegate.DelegadoEJB;
import co.com.tracert.vtrack.web.utils.NamedBeanUtils;

@ViewScoped
@Named
public class IndexNB extends NamedBeanUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2665235968687266194L;

	@EJB
	private DelegadoEJB delegado;

	private String correo;
	private String password;
	private Cuenta cuentaUsuario;

	private Usuario usuario;

	public IndexNB() {

	}

	@PostConstruct
	public void init() {

	}

	/**
	 * @author Steven M
	 * @descripcion A partir de los datos digitados se crea una cuenta y se verifica
	 *              si existe un usuario con esa cuenta asociada. En caso
	 *              afirmativo, se sube ese usuario a sesión y se redirige a la
	 *              página esq sugeridos En caso negativo, se muestra mensaje de
	 *              error y no se redirige.
	 * @fecha 24/10/2018
	 * @return url de la página esq sugeridos o url vacio
	 */
	public String validarSesionUsuario() {
		String retorno = "";
		try {
			cuentaUsuario = new Cuenta();
			cuentaUsuario.setCorreo(correo);
			cuentaUsuario.setContrasenia(password);
			
			Cuenta cuentaAnteriorLogin = delegado.encontrarCuentaPorCorreo(correo);
			
			usuario = delegado.iniciarSesion(cuentaUsuario);

			if (usuario != null) {
				// Se sube a la sesión el usuario
				putInSession(ConstantesVtrack.USUARIO, usuario);
				addMessage(FacesMessage.SEVERITY_ERROR, "Inicio de sesión satisfactorio");
				retorno = ConstantesVtrack.PAGINA_INICIO + ConstantesVtrack.REDIRECION_PAGINAS;
				delegado.auditarAutenticacion(cuentaAnteriorLogin, usuario.getCuenta());
			}

		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
		return retorno;

	}

	/**
	 * @author Steven M
	 * @descipcion redigirge a la página donde se crea una cuenta de usuario
	 * @fecha 1/11/2018
	 * @return la url de la página crear cuenta usuario
	 */
	public String redirigirRegistro() {
		return ConstantesVtrack.PAGINA_REGISTRO + ConstantesVtrack.REDIRECION_PAGINAS;
	}

	public DelegadoEJB getDelegado() {
		return delegado;
	}

	public void setDelegado(DelegadoEJB delegado) {
		this.delegado = delegado;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
