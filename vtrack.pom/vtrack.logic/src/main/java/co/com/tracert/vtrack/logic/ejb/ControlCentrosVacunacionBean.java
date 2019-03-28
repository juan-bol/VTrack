package co.com.tracert.vtrack.logic.ejb;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.com.tracert.vtrack.logic.manager.CentroVacunacionManager;
import co.com.tracert.vtrack.model.entities.CentroVacunacion;
import co.com.tracert.vtrack.model.entities.Ciudad;
import co.com.tracert.vtrack.model.interfaces.IControlCentroVacunacionLocal;
import co.com.tracert.vtrack.model.interfaces.IControlCentroVacunacionRemota;


@Stateless(name="ControlCentrosVacunacionBean", mappedName="controlCentrosVacunacionBean")

@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Local(IControlCentroVacunacionLocal.class)
@Remote(IControlCentroVacunacionRemota.class)
public class ControlCentrosVacunacionBean implements IControlCentroVacunacionLocal, IControlCentroVacunacionRemota {

	@PersistenceContext
	private EntityManager entityManager;
	
	
	/**
	 * @descripcion Sugiere centros de vacunación dependiendo de la ciudad pasada por parámetro
	 * @author Ana Arango
	 * @fecha 21/11/2018
	 * @param ciudad ciudad en la que los centros de vacunación deben estar
	 * @throws Exception
	 */
	@Override
	public List<CentroVacunacion> sugerirCentrosVacunacionCercanos(Ciudad ciudad) {
		CentroVacunacionManager manager= new CentroVacunacionManager(entityManager);
		return manager.sugerirCentrosVacunacionCercanos(ciudad);
	}

}
