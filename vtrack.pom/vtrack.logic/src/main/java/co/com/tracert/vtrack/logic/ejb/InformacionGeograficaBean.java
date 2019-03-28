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

import co.com.tracert.vtrack.logic.manager.InformacionGeograficaManager;
import co.com.tracert.vtrack.model.entities.Ciudad;
import co.com.tracert.vtrack.model.entities.Pais;
import co.com.tracert.vtrack.model.entities.Region;
import co.com.tracert.vtrack.model.entities.TipoDocumentoPais;
import co.com.tracert.vtrack.model.interfaces.IInformacionGeograficaLocal;
import co.com.tracert.vtrack.model.interfaces.IInformacionGeograficaRemota;

@Stateless(name="InformacionGeograficaBean", mappedName="informacionGeograficaBean")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Local(IInformacionGeograficaLocal.class)
@Remote(IInformacionGeograficaRemota.class)
public class InformacionGeograficaBean implements IInformacionGeograficaRemota{

	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * @description devuelve la lista de los paises del sistema Vtrack
	 * @author Juan Bolaños
	 * @return Lista de todos los paises del sistema
	 * @throws Exception
	 */
	@Override
	public List<Pais> listarPaises() throws Exception {
		InformacionGeograficaManager manager = new InformacionGeograficaManager(entityManager);
		return manager.listarPaises();
	}
	
	
	/**
	 * @descripcion devuelve la lista de los regiones dado un determinado pais del sistema Vtrack
	 * @author Juan Bolaños
	 * @return Lista de regiones pertenecientes a un pais
	 * @throws Exception
	 */
	@Override
	public List<Region> listarRegiones(Pais pais) throws Exception {
		InformacionGeograficaManager manager = new InformacionGeograficaManager(entityManager);
		return manager.listarRegiones(pais);
	}
	
	
	/**
	 * @description devuelve la lista de las ciudades dado una determinada región del sistema Vtrack
	 * @author Juan Bolaños
	 * @return Lista de las ciudades pertenecientes a una region
	 * @throws Exception
	 */
	@Override
	public List<Ciudad> listarCiudades(Region region) throws Exception {
		InformacionGeograficaManager manager = new InformacionGeograficaManager(entityManager);
		return manager.listarCiudades(region);
	}


	/**
	 * @descripcion devuelve la lista de los Tipo de documentos de un determinado pais del sistema Vtrack
	 * @author Juan Bolaños
	 * @return Lista los tipos de documento pertenecientes a un pais
	 * @throws Exception
	 */
	@Override
	public List<TipoDocumentoPais> listarTipoDocumentos(Pais pais) throws Exception {
		InformacionGeograficaManager manager = new InformacionGeograficaManager(entityManager);
		return manager.listarTipoDocumentos(pais);
	}

	/**@author Juan Bolaños
	 * @description Permitir obtener la distribución politica de cada pais en el sistema Vtrack
	 * @param idPais Identificador del pais
	 * @throws Exception
	 */
	@Override
	public Pais obtenerPais(Long idPais) throws Exception {
		InformacionGeograficaManager manager = new InformacionGeograficaManager(entityManager);
		return manager.obtenerPais(idPais);
	}

	/**@author Victor Potes
	 * @description Permitir obtener la ciudad en el sistema Vtrack
	 * @param idCiudad Identificador de la ciudad
	 * @throws Exception
	 */
	@Override
	public Ciudad obtenerCiudad(Long idCiudad) throws Exception {
		//TODO Agregar en todas sus capas obtenerCiudad
		InformacionGeograficaManager manager = new InformacionGeograficaManager(entityManager);
		return manager.obtenerCiudad(idCiudad);
	}


	@Override
	public void llenarBDDepartamentosMunicipios(Long codDep, String nomDep, Long codMun, String nomMun) throws Exception {
		InformacionGeograficaManager manager = new InformacionGeograficaManager(entityManager);
		manager.llenarBDDepartamentosMunicipios(codDep, nomDep, codMun, nomMun);
		
	}
}
