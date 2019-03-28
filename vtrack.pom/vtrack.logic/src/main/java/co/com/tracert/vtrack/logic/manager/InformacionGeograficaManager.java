package co.com.tracert.vtrack.logic.manager;

import java.util.List;

import javax.persistence.EntityManager;

import co.com.tracert.vtrack.model.entities.Ciudad;
import co.com.tracert.vtrack.model.entities.Pais;
import co.com.tracert.vtrack.model.entities.Region;
import co.com.tracert.vtrack.model.entities.TipoDocumento;
import co.com.tracert.vtrack.model.entities.TipoDocumentoPais;
import co.com.tracert.vtrack.model.exceptions.ExcepcionNegocio;
import co.com.tracert.vtrack.persistence.dao.DaoGenerico;
import co.com.tracert.vtrack.persistence.dao.DaoVTrack;

public class InformacionGeograficaManager {
	
	private EntityManager entityManager;

	public InformacionGeograficaManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	/**
	 * @description devuelve la lista de los paises del sistema Vtrack
	 * @author Juan Bolaños
	 * @fecha 27/10/18
	 * @return Lista de todos los paises del sistema
	 * @throws Exception
	 */
	public List<Pais> listarPaises() throws Exception {
		DaoGenerico<Pais> dao = new DaoGenerico<>(entityManager, Pais.class);
		return dao.encontrarTodos();
	}
	
	
	/**
	 * @descripcion devuelve la lista de los regiones dado un determinado pais del sistema Vtrack
	 * @author Juan Bolaños
	 * @fecha 27/10/18
	 * @return Lista de regiones pertenecientes a un pais
	 * @throws Exception
	 */
	public List<Region> listarRegiones(Pais pais) throws Exception {
		DaoVTrack<Region> dao = new DaoVTrack<>(entityManager, Region.class);
		return dao.listarRegiones(pais);
	}
	
	
	/**
	 * @description devuelve la lista de las ciudades dado una determinada región del sistema Vtrack
	 * @author Juan Bolaños
	 * @fecha 27/10/18
	 * @return Lista de las ciudades pertenecientes a una region
	 * @throws Exception
	 */
	public List<Ciudad> listarCiudades(Region region) throws Exception {
		DaoVTrack<Ciudad> dao = new DaoVTrack<>(entityManager, Ciudad.class);
		return dao.listarCiudades(region);
	}

	/**
	 * @descripcion devuelve la lista de los Tipo de documentos de un determinado pais del sistema Vtrack
	 * @author Juan Bolaños
	 * @fecha 27/10/18
	 * @return Lista los tipos de documento pertenecientes a un pais
	 * @throws Exception
	 */
	public List<TipoDocumentoPais> listarTipoDocumentos(Pais pais) {
		DaoVTrack<TipoDocumento> dao = new DaoVTrack<>(entityManager, TipoDocumento.class);
		return dao.listarTipoDocumentos(pais);
	}

	/**@author Juan Bolaños
	 * @description Permitir obtener la distribución politica de cada pais en el sistema Vtrack
	 * @fecha 27/10/18
	 * @param idPais Identificador del pais
	 * @throws Exception
	 */
	public Pais obtenerPais(Long idPais) throws Exception {
		if (idPais <= 0) {
			throw new ExcepcionNegocio("El id del pais no es valido, verifique que se mayor que cero");
		}
		DaoGenerico<Pais> dao = new DaoGenerico<>(entityManager, Pais.class);
		return dao.encontrarPorId(idPais);
	}
	
	/**@author Victor Potes
	 * @description Permitir obtener la ciudad en el sistema Vtrack
	 * @param idCiudad Identificador de la ciudad
	 * @throws Exception
	 */
	public Ciudad obtenerCiudad(Long idCiudad) throws Exception {
		if (idCiudad <= 0) {
			throw new ExcepcionNegocio("El id de la ciudad no es válida, verifique que se mayor que cero");
		}
		DaoGenerico<Ciudad> dao = new DaoGenerico<>(entityManager, Ciudad.class);
		return dao.encontrarPorId(idCiudad);
	}
	
	/**@author Victor Potes
	 * @description Permitir llenar la base de datos con los departamentos y ciudades de Colombia
	 * @param codDep código del departamento según el DANE
	 * @param nomDep nombre del departamento según el DANE
	 * @param codMun código del municipio según el DANE
	 * @param nomMun nombre del municipio según el DANE
	 * @throws Exception Puede ocurrir Exception por múltiples transacciones
	 */
	public void llenarBDDepartamentosMunicipios (Long codDep, String nomDep, Long codMun, String nomMun) throws Exception {
		try {
			
			
			DaoVTrack<Region> daoRegion = new DaoVTrack<>(entityManager, Region.class);
			DaoVTrack<Ciudad> daoCiudad = new DaoVTrack<>(entityManager, Ciudad.class);

			  Region region = daoRegion.encontrarPorId(codDep);		
				if(region == null) {
					DaoVTrack<Pais> daoPais = new DaoVTrack<>(entityManager, Pais.class);
					Pais pais = daoPais.encontrarPorId(1L);
					region = new Region();
					region.setIdRegion(codDep);
					region.setNombre(nomDep);
					region.setPai(pais);
					daoRegion.crear(region);					
				}
				Ciudad ciudad = daoCiudad.encontrarPorId(codMun);
				if(ciudad==null)
				{
					ciudad = new Ciudad();
					ciudad.setIdCiudad(codMun);
					ciudad.setNombre(nomMun);
					ciudad.setRegion(region);
					daoCiudad.crear(ciudad);
				}
				
		}
		catch (Exception e) {
			throw e;
		}
		
	}
	

}
