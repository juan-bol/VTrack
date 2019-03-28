package co.com.tracert.vtrack.model.interfaces;

import java.util.List;

import co.com.tracert.vtrack.model.entities.CentroVacunacion;
import co.com.tracert.vtrack.model.entities.Ciudad;

public interface IControlCentroVacunacionLocal {
	public List<CentroVacunacion> sugerirCentrosVacunacionCercanos(Ciudad ciudad);
}
