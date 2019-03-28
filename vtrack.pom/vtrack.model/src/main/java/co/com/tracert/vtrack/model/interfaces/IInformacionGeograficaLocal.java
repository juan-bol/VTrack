package co.com.tracert.vtrack.model.interfaces;

import java.util.List;

import co.com.tracert.vtrack.model.entities.Ciudad;
import co.com.tracert.vtrack.model.entities.Pais;
import co.com.tracert.vtrack.model.entities.Region;
import co.com.tracert.vtrack.model.entities.TipoDocumento;

public interface IInformacionGeograficaLocal {

	public List<Pais> listarPaises() throws Exception;
	public List<Region> listarRegiones(Pais pais) throws Exception;
	public List<Ciudad> listarCiudades(Region region) throws Exception;
	public List<TipoDocumento> listarTipoDocumentos(Pais pais) throws Exception;
	public Pais obtenerPais(Long idPais) throws Exception; 
	public Ciudad obtenerCiudad(Long idCiudad) throws Exception;
	public void llenarBDDepartamentosMunicipios (Long codDep, String nomDep, Long codMun, String nomMun) throws Exception;
}
