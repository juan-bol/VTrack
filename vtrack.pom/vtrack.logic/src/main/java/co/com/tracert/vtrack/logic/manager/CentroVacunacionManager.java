package co.com.tracert.vtrack.logic.manager;



import java.util.List;

import javax.persistence.EntityManager;

import co.com.tracert.vtrack.model.entities.CentroVacunacion;
import co.com.tracert.vtrack.model.entities.Ciudad;
import co.com.tracert.vtrack.persistence.dao.DaoVTrack;

public class CentroVacunacionManager {
	
	private EntityManager entityManager;

	public CentroVacunacionManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	
	/**
	 * @descripcion Sugiere centros de vacunación dependiendo de la ciudad pasada por parámetro
	 * @author Ana Arango
	 * @fecha 21/11/2018
	 * @param ciudad ciudad en la que los centros de vacunación deben estar
	 * @throws Exception
	 */
	public List<CentroVacunacion> sugerirCentrosVacunacionCercanos(Ciudad ciudad){
		
		//Instanciar DAO
		DaoVTrack<CentroVacunacion> dao = new DaoVTrack<>(entityManager, CentroVacunacion.class);
		//Realizar consulta
		List<CentroVacunacion> centros=dao.sugerirCentrosVacunacionCercanos(ciudad);
		return centros;
		
		
	}
	

}
