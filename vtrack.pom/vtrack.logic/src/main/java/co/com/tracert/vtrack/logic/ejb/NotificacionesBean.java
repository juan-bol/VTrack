package co.com.tracert.vtrack.logic.ejb;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.tracert.vtrack.logic.manager.EsquemasCarneManager;
import co.com.tracert.vtrack.logic.manager.NotificacionesManager;
import co.com.tracert.vtrack.logic.manager.PermisosManager;
import co.com.tracert.vtrack.logic.manager.UsuarioManager;
import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.dto.ColumnasVacunaDTO;
import co.com.tracert.vtrack.model.dto.EsquemaDTO;
import co.com.tracert.vtrack.model.entities.Cuenta;
import co.com.tracert.vtrack.model.entities.Permiso;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.exceptions.ExcepcionNegocio;
import co.com.tracert.vtrack.model.interfaces.INotificacionesLocal;
import co.com.tracert.vtrack.model.interfaces.INotificacionesRemota;

@Stateless(name = "NotificacionesBean", mappedName = "notificacionesBean")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Local(INotificacionesLocal.class)
@Remote(INotificacionesRemota.class)
public class NotificacionesBean implements INotificacionesRemota {

	@PersistenceContext
	private EntityManager entityManager;

	private static final Logger log = LoggerFactory.getLogger(NotificacionesBean.class);

	/**
	 * @descripcion Timer que se ejecuta todos los días a las 9AM para verificar si
	 *              un usuario dede ser notificado para aplicarse una dosis de una
	 *              vacuna
	 * @author Juan Bolaños
	 * @fecha 21/11/18
	 */
	@Schedules ({
		@Schedule(dayOfWeek = "*", hour = "9", timezone = "America/Bogota", persistent = false, info = "Timer diario a las 9AM"),
//		@Schedule(year="2018", month="12", dayOfMonth="11", hour = "15-23", minute = "*/10", timezone = "America/Bogota", persistent = false, info = "Solo para pruebas 11 dic"),
		@Schedule(year="2018", month="12", dayOfMonth="12", hour = "15-20", minute = "*/10", timezone = "America/Bogota", persistent = false, info = "Solo para pruebas 12 dic")
	})
	public void timerNotificaciones() {
		// Se instancian los manager necesarios para la notificacion
		UsuarioManager usuarioManager = new UsuarioManager(entityManager);
		EsquemasCarneManager esquemasCarneManager = new EsquemasCarneManager(entityManager);

		try {
			// Se obtiene la lista de todos los usuarios
			List<Usuario> usuarios = usuarioManager.listarUsuarios();
			for (Usuario usuario : usuarios) {

				// Se verifica que el usuario tenga activada las notificaciones
				if (usuario.getEstado().getEstado().equals(ConstantesVtrack.ESTADO_ACTIVO)
						&& usuario.getNotificacion().getEstado().getEstado().equals(ConstantesVtrack.ESTADO_ACTIVO)) {
					// Se obtiene la lista de esquemas (carne de vacunacion) de cada usuario
					List<EsquemaDTO> esquemasDTO = esquemasCarneManager.darCarneVacunacion(usuario);
					for (EsquemaDTO esquemaDTO : esquemasDTO) {

						// Se obtiene la lista de vacunas de cada esquema
						List<ColumnasVacunaDTO[]> vacunas = esquemaDTO.getColumnas();
						for (ColumnasVacunaDTO[] dosisDeVacuna : vacunas) {

							// Se recorre la lista de dosis de cada vacuna en el esquema
							for (ColumnasVacunaDTO dosis : dosisDeVacuna) {
								// Se valida que la dosis no sea nula
								if (dosis != null) {
									// Se verifica que la dosis no tenga una aplicación o que esta no se encuentre
									// activa
									if (dosis.getDosisAplicada() == null || dosis.getDosisAplicada().getEstado()
											.getEstado().equals(ConstantesVtrack.ESTADO_INACTIVO)) {

										// Se obtiene la fecha de aplicacion de la dosis
										String strFechaAplicacion = dosis.getFechaAplicacion();
										if (strFechaAplicacion.equals("Hoy")) {
											break;
										}
										SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
										Date fechaAplicacion = formatter.parse(strFechaAplicacion);

										// Se obtiene los dias de anticipacion que el usuairio configuro para ser
										// notificado
										BigDecimal dias = usuario.getNotificacion().getDiasAnticipacion();
										// Se obtiene la fecha de hoy
										Date fechaHoy = new Date();
										Calendar calendar = Calendar.getInstance();
										calendar.setTime(formatter.parse(formatter.format(fechaHoy)));

										// Se le aumentan los dias de anticipacion al dia actual
										calendar.add(Calendar.DAY_OF_YEAR, dias.intValue());
										Date fechaParaNotificar = calendar.getTime();

										// Se verifica que dicha suma sea el mismo dia de la fecha de aplicacion de la
										// dosis, si es asi, se notifica
										if (fechaParaNotificar.compareTo(fechaAplicacion) == 0) {
											boolean tieneCuenta = false;
											// Se verifica que el usuario tenga una cuenta asociada
											if (usuario.getCuenta() != null) {
												tieneCuenta = true;
											}
											// Se instancia el manager de notificaciones
											NotificacionesManager notificacionesManager = new NotificacionesManager();
											// Se obtiene la informacion del mensaje a enviar
											String[] mensaje = notificacionesManager.redactarMensajeAplicacionDosis(usuario, dosis,
													fechaAplicacion, dosisDeVacuna, tieneCuenta);
											String asuntoMensaje = mensaje[0];
											String cuerpoMensaje = mensaje[1];
											// Si el usuario tiene una cuenta asociada se le envía el correo a su cuenta
											if (tieneCuenta) {
												enviarCorreo(usuario.getCuenta(), asuntoMensaje, cuerpoMensaje);
											}
											// Si el usuario no tiene una cuenta asociada, se enviará el mensaje a las
											// cuentas
											// de los usuarios que tienen permisos sobre la cuenta
											else {
												PermisosManager permisosManager = new PermisosManager(entityManager);
												List<Permiso> listaPermisos = permisosManager
														.darPermisosOrigenDeAprobado(usuario);
												// Se verifica que la lista tenga permisos
												if (listaPermisos.size() != 0) {
													// Por cada permiso se envía un correo al usuario que tiene el
													// permiso sobre el usuario en cuestión
													for (Permiso permiso : listaPermisos) {
														Usuario usuarioNotificar = permiso.getUsuarioOrigen();
														enviarCorreo(usuarioNotificar.getCuenta(), asuntoMensaje,
																cuerpoMensaje);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("Error en el Timer: " + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * @descripcion Este metodo se encarga de enviar un correo electronico desde la
	 *              cuenta Gmail vtrack.icesi@gmail.com
	 * @author Juan Bolaños
	 * @fecha 19/11/18
	 * @param cuenta cuaneta de usuario al que se le enviará el correo
	 * @param asunto del correo
	 * @param cuerpo del correo
	 * @throws ExcepcionNegocio
	 */
	@Override
	public void enviarCorreo(Cuenta cuenta, String asunto, String cuerpo) throws Exception {
		// TODO agregar al deployment
		NotificacionesManager manager = new NotificacionesManager();
		manager.enviarCorreo(cuenta, asunto, cuerpo);

	}

}
