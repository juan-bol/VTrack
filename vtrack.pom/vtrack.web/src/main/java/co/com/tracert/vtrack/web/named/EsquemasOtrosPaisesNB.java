package co.com.tracert.vtrack.web.named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.dto.EsquemaDTO;
import co.com.tracert.vtrack.model.entities.EsquemaAgregado;
import co.com.tracert.vtrack.model.entities.Pais;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.web.businessdelegate.DelegadoEJB;

/**
 * Entidad que recibe los eventos generados en la vista de esquemas sugeridos de 
 * otros paises.
 * @author Santiago
 *
 */
@ViewScoped
@Named
public class EsquemasOtrosPaisesNB extends EsquemasSugeridosNB implements Serializable {
	
	// -------------------------------------------------------------------------------------------------
	// Versión de serialización
	// -------------------------------------------------------------------------------------------------
	
	/**
	 * Versión de serialización.
	 */
	private static final long serialVersionUID = -3881116312936374662L;
	
	// -------------------------------------------------------------------------------------------------
	// Atributos
	// -------------------------------------------------------------------------------------------------
	
	/**
	 * Representa la relación con el delegado
	 */
	@EJB
	private DelegadoEJB delegado;
	
	/**
	 * Representa la lista de esquemas del país seleccionado.
	 */
	private List<EsquemaDTO> listEsqOtrosPaises;
	
	/**
	 * Representa la lista de países que puede seleccionar el usuario.
	 */
	private List<Pais> listPaises;
	
	/**
	 * Representa el identificador del país que el usuario ha seleccionado para
	 * ver los esquemas sugeridos.
	 */
	private Long idPaisSeleccionado;
	

	// -------------------------------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------------------------------
	
	/**
	 * Construye una instancia del controlador que atiende los eventos de la vista esquemas
	 * sugeridos de otros países.
	 */
	public EsquemasOtrosPaisesNB() { }
	
	// -------------------------------------------------------------------------------------------------
	// Servicios
	// -------------------------------------------------------------------------------------------------
	
	@PostConstruct
	private void inicializar() {
		listEsqOtrosPaises = new ArrayList<>();
		listPaises = new ArrayList<>();
		idPaisSeleccionado = 0L;
		try {
			// Asigna / inicializa a el usuario en sesión
			asignarUsuario();
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessage());
		}
	}
	
	/**
	 * @author Steven M, Santiago Restrepo
	 * @descripcion Llena la lista de paises y muestra el dialogo de esquemas sugeridos por paises
	 * @fecha TODO fecha
	 */
	public void mostrarDialogoEsquemasOtrosPaises() {
		try {
			if (usuario.getPai() == null) {
				throw new Exception("Aún no ha registrado un país de residencia. Para acceder a esta "
						+ "función, por favor registre uno.");
			} 
			listPaises = delegado.encontrarPaisesExcepto(usuario.getPai().getIdPais());
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('countriesDialog').initPosition();PF('countriesDialog').show()");
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_INFO, "Error: " + e.getMessage());
		}
	}
	
	/**
	 * @author Steven M, Santiago Restrepo
	 * @descripcion Llena la lista de paises y muestra el dialogo de esquemas sugeridos por paises
	 * @fecha TODO fecha
	 */
	public void cambioPaisSeleccionado() {
		try {
			if (idPaisSeleccionado != ConstantesVtrack.INVALIDO) {
				actualizarSugerencias();
			} else {
				listEsqOtrosPaises = new ArrayList<>();
			}
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void actualizarSugerencias() {
		try {
			Pais pais = new Pais();
			pais.setIdPais(idPaisSeleccionado);
			listEsqOtrosPaises = delegado.sugerirEsquemasVacunacion(usuario, pais);
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}
	
	public void agregarEsquemaOtroPais() {
		try {
			EsquemaAgregado esquemaAgregado = delegado.agregarEsquemaSugerido(usuario, esquemaSeleccionado);
			Usuario usuarioEnSesion = (Usuario) getFromSession(ConstantesVtrack.USUARIO);
			delegado.auditarAgregarEsquemaAlCarne(esquemaAgregado, usuarioEnSesion);
			addMessage(FacesMessage.SEVERITY_INFO,
					"Se agregó el esquema " + esquemaSeleccionado.getNombre() + " satisfactoriamente");
			actualizarSugerencias();
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "No fue posible agregar el esquema de vacunacion");
		}
	}
	
	// -------------------------------------------------------------------------------------------------
	// Dar y modificar
	// -------------------------------------------------------------------------------------------------

	public List<EsquemaDTO> getListEsqOtrosPaises() {
		return listEsqOtrosPaises;
	}

	public void setListEsqOtrosPaises(List<EsquemaDTO> listEsqOtrosPaises) {
		this.listEsqOtrosPaises = listEsqOtrosPaises;
	}

	public List<Pais> getListPaises() {
		return listPaises;
	}

	public void setListPaises(List<Pais> listPaises) {
		this.listPaises = listPaises;
	}

	public Long getIdPaisSeleccionado() {
		return idPaisSeleccionado;
	}

	public void setIdPaisSeleccionado(Long idPaisSeleccionado) {
		this.idPaisSeleccionado = idPaisSeleccionado;
	}
	
}
