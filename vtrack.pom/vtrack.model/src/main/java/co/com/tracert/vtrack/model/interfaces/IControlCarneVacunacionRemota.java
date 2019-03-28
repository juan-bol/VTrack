package co.com.tracert.vtrack.model.interfaces;

import java.util.Date;
import java.util.List;

import co.com.tracert.vtrack.model.dto.EsquemaDTO;
import co.com.tracert.vtrack.model.entities.Dosis;
import co.com.tracert.vtrack.model.entities.DosisAplicada;
import co.com.tracert.vtrack.model.entities.Esquema;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.entities.Vacuna;
import co.com.tracert.vtrack.model.entities.VacunaOculta;

public interface IControlCarneVacunacionRemota {
	
	public void registrarAplicacionDosis(Usuario usuario, Dosis dosis, Date fecha) throws Exception;
	public VacunaOculta ocultarVacunaCarne(Usuario usuario, Vacuna vacuna) throws Exception;
	public List<EsquemaDTO> darCarneVacunacion(Usuario usuario) throws Exception;
	public DosisAplicada modificarAplicacionDosis(DosisAplicada dosisAplicada, String estado) throws Exception;
	public void verificarEsquemaAgregadoVacio(Usuario usuario, Esquema esquema) throws Exception;
	public void auditarEliminarVacunaDeEsquemaEnCarne(VacunaOculta vacunaOculta, Usuario usuarioEnSesion)
			throws Exception;
	public void auditarModificarAplicacionDosis(DosisAplicada dosisAplicadaAnterior, DosisAplicada 
			dosisAplicadaActual, Usuario usuarioEnSesion)throws Exception;
}
