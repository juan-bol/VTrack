package co.com.tracert.vtrack.web.named;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.entities.CentroVacunacion;
import co.com.tracert.vtrack.model.entities.Ciudad;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.web.businessdelegate.DelegadoEJB;
import co.com.tracert.vtrack.web.utils.NamedBeanUtils;

@ViewScoped
@Named
public class CentrosVacunacionNB extends NamedBeanUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5749577156070705709L;

	@EJB
	private DelegadoEJB delegado;

	private Ciudad ciudadAsignada;

	private List<CentroVacunacion> listCentrosVacunacion;

	private Usuario usuarioSesion;

	private String latitudMapa;

	private String longitudMapa;

	private MapModel modeloMapa;

	private Marker marcadorSeleccionado;

	public CentrosVacunacionNB() {
	}

	@PostConstruct
	public void init() {

		asignarUsuarioSesion();

		try {

			if (verificarExisteCiudad()) {
				// El usuario tiene una ciudad asignada, allí se mostrarán los centros de
				// vacunación
				ciudadAsignada = usuarioSesion.getCiudad();
			} else {
				// El usuario no tiene una ciudad asignada, se mostrarán los centros de
				// vacunación de la ciudad por defecto (Bogotá)
				ciudadAsignada = delegado.encontrarCiudad(ConstantesVtrack.CIUDAD_DEFECTO);
			}

			if (verificarLatitudLongitud(ciudadAsignada)) {
				// Si la ciudad tiene latitud y longitud el mapa se muestra en esa ciudad
				latitudMapa = ciudadAsignada.getLatitud();
				longitudMapa = ciudadAsignada.getLongitud();
			} else {
				//Si la ciudad no tiene latitud o longitud, la ciudad cambia a la por defecto y el mapa
				// se muestra allì
				ciudadAsignada = delegado.encontrarCiudad(ConstantesVtrack.CIUDAD_DEFECTO);
				latitudMapa = ciudadAsignada.getLatitud();
				longitudMapa = ciudadAsignada.getLongitud();

			}

			// Se obtienen los centros de vacunación de la ciuda de asignada
			listCentrosVacunacion = delegado.sugerirCentrosVacunacionCercanos(ciudadAsignada);
			
			//Muestra un dialogo cuando no hay centros de vacunación en la ciudad actual
			if(listCentrosVacunacion.size() == 0) {
				mostraDialogoNoCentros();
			}

		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage());
		}

		modeloMapa = new DefaultMapModel();

		agregarMarcadoresAlMapa(listCentrosVacunacion);

	}

	public void marcadorSeleccionado(OverlaySelectEvent event) {
		marcadorSeleccionado = (Marker) event.getOverlay();
	}

	/**
	 * @author Steven M
	 * @descripcion a partir de la lista de centros de vacunación de una ciudad,
	 *              agrega los marcadores al mapa
	 * @param listCentrosVacunacion de la ciudad de residencia del usuario o ciudad
	 *                              defecto
	 * @fecha 28/11/2018
	 */
	public void agregarMarcadoresAlMapa(List<CentroVacunacion> listCentrosVacunacion) {

		CentroVacunacion centroTmp;
		LatLng coordTmp;
		double lat;
		double lon;

		for (int i = 0; i < listCentrosVacunacion.size(); i++) {
			centroTmp = listCentrosVacunacion.get(i);
			lat = Double.parseDouble(centroTmp.getLatitud());
			lon = Double.parseDouble(centroTmp.getLongitud());
			coordTmp = new LatLng(lat, lon);
			modeloMapa.addOverlay(new Marker(coordTmp, centroTmp.getNombre(), centroTmp, "http://maps.google.com/mapfiles/kml/paddle/H.png"));
		}

	}

	/**
	 * @author Steven M
	 * @descripcion asigna a la var usuarioSesion el valor: 1. Correspondiente con
	 *              el usuario que se logeo 2. Correspondiente con el usuario
	 *              impersonate
	 * @fecha 1/11/2018
	 */
	public void asignarUsuarioSesion() {
		Usuario usuarioImp = (Usuario) getFromSession(ConstantesVtrack.USUARIO_IMP);
		// Si hay usuario impersonate entonces el usuario en sesión es el usuario
		// impersonate
		if (usuarioImp != null) {
			usuarioSesion = (Usuario) getFromSession(ConstantesVtrack.USUARIO_IMP);

			// En caso de que no exista usuario impersonate entonces el usuario en sesión es
			// el usuario logeado
		} else {
			usuarioSesion = (Usuario) getFromSession(ConstantesVtrack.USUARIO);
		}
	}

	/**
	 * @author Steven M
	 * @descripcion verifica el usuario en sesión tiene asignado una ciudad de
	 *              residencia
	 * @fecha 28/11/2018
	 * @return true el usuario tiene una ciudad asignada o fase si no
	 */
	public boolean verificarExisteCiudad() {

		boolean existe = false;

		if (usuarioSesion.getPai() != null && usuarioSesion.getRegion() != null && usuarioSesion.getCiudad() != null) {
			existe = true;
		}

		return existe;
	}

	/**
	 * @author Steven M
	 * @descripcion verifica si la ciudad ingresada por parametro tiene latitud y
	 *              longitud
	 * @param ciudad a la cual se le verifica si tiene latitud y longitud
	 * @return true si la ciudad si tiene latitud y longitud, false en caso
	 *         contrario
	 * @fecha 28/11/2018
	 */
	public boolean verificarLatitudLongitud(Ciudad ciudad) {
		boolean existe = false;

		if (ciudad.getLatitud() != null && ciudad.getLongitud() != null) {
			existe = true;
		}
		return existe;
	}
	
	/**
	 * @author Steven M
	 * @descripcion Abre el dialogo cuando no hay centros de vacunación
	 */
	public void mostraDialogoNoCentros() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('dialogoNoCentros').show()");
	}
	
	/**
	 * @author Steven M
	 * @descripcion Cierra el dialogo cuando presiona el botón del dialogo
	 * que se muestra cuando no hay centros de vacunación
	 */
	public void cerrarDialogoNoCentros() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('dialogoNoCentros').hide()");
	}

	public String getLatitudMapa() {
		return latitudMapa;
	}

	public void setLatitudMapa(String latitudMapa) {
		this.latitudMapa = latitudMapa;
	}

	public String getLongitudMapa() {
		return longitudMapa;
	}

	public void setLongitudMapa(String longitudMapa) {
		this.longitudMapa = longitudMapa;
	}

	public MapModel getModeloMapa() {
		return modeloMapa;
	}

	public void setModeloMapa(MapModel modeloMapa) {
		this.modeloMapa = modeloMapa;
	}

	public Marker getMarcadorSeleccionado() {
		return marcadorSeleccionado;
	}

	public void setMarcadorSeleccionado(Marker marcadorSeleccionado) {
		this.marcadorSeleccionado = marcadorSeleccionado;
	}

}
