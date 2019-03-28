package co.com.tracert.vtrack.model.interfaces;

import java.util.List;

import co.com.tracert.vtrack.model.dto.EsquemaDTO;
import co.com.tracert.vtrack.model.entities.Esquema;
import co.com.tracert.vtrack.model.entities.EsquemaAgregado;
import co.com.tracert.vtrack.model.entities.Pais;
import co.com.tracert.vtrack.model.entities.Usuario;

public interface IControlEsquemaVacunacionRemota {

	public List<EsquemaDTO> sugerirEsquemasVacunacion(Usuario usuario, Pais pais) throws Exception;
	public EsquemaAgregado agregarEsquemaSugerido(Usuario usuario, Esquema esquema) throws Exception;
	public int darDosisMaxima(Esquema esquema) throws Exception;
	public void llenarBDVacunas (Long idVacuna, String nombreVacuna, Long idUsuario, String detalleVacuna) 
			throws Exception;
	public List<EsquemaDTO> encontrarEsquemasPorPais (Long idPais) throws Exception;
	public List<Pais> encontrarPaisExcepto(Long idPais) throws Exception ;
	public void auditarAgregarEsquemaAlCarne(EsquemaAgregado esquemaAgregado, Usuario usuarioEnSesion) throws Exception;

}
