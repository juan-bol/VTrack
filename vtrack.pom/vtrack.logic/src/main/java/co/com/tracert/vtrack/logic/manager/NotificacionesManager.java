package co.com.tracert.vtrack.logic.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.dto.ColumnasVacunaDTO;
import co.com.tracert.vtrack.model.entities.Cuenta;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.exceptions.ExcepcionNegocio;

public class NotificacionesManager {

	private static final Logger log = LoggerFactory.getLogger(NotificacionesManager.class);

	public NotificacionesManager() {

	}

	/**
	 * @descripcion Este metodo redacta el mensaje que se enviara al correo del
	 *              usuario para notificarle sobre la aplicacion de una vacuna
	 * @author Juan Bolanos
	 * @fecha 24/11/18
	 * @param usuario usuario al que le enviara el correo
	 * @param dosis   dosis de la cual se notificara
	 * @return un arreglo de string donde la posicion 0 es el asunto del mensaje y
	 *         la posicion 1 es el cuerpo del mensaje
	 * @throws ParseException Al manipular las fechas y convertirlas en distintos formatos
	 */
	public String[] redactarMensajeAplicacionDosis(Usuario usuario, ColumnasVacunaDTO dosis, Date fechaAplicacion,
			ColumnasVacunaDTO[] dosisDeVacuna, boolean tieneCuenta) throws ParseException {
		// Se define arreglo de dos cadenas, que son el asunto y el cuerpo del correo
		String[] mensaje = new String[2];
		String tipoDosis = dosis.getDosis().getTipoDosis();
		if (tipoDosis.equals("R")) {
			tipoDosis = "Refuerzo";
		} else if (tipoDosis.equals("D")) {
			tipoDosis = "Dosis";
		}
		String tipoDosisLow = tipoDosis.toLowerCase();
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE', 'd' de 'MMMMM' de 'yyyy");
		String fecha = formatter.format(fechaAplicacion);
		String cuerpo = "";
		String asunto = "";
		if (!tieneCuenta) {
			asunto += "VTrack – Siguiente " + tipoDosisLow;
			if (tipoDosis.equals("Dosis"))
				cuerpo += "La siguiente " + tipoDosisLow + " de " + usuario.getNombre() + " está programada ";
			else if (tipoDosis.equals("Refuerzo"))
				cuerpo += "El siguiente " + tipoDosisLow + " de " + usuario.getNombre() + " está programado ";
		} else {
			asunto += "VTrack – Tu siguiente " + tipoDosisLow;
			if (tipoDosis.equals("Dosis"))
				cuerpo += "Tu siguiente " + tipoDosisLow + " está programada ";
			else if (tipoDosis.equals("Refuerzo"))
				cuerpo += "Tu siguiente " + tipoDosisLow + " está programado ";
		}
		cuerpo += " para el día <strong>" + fecha + "</strong> para " + dosis.getNombreVacuna() + ".<br></br><br></br>"
				+ dosis.getDosis().getVacunaEsquema().getVacuna().getVacunaDetalle().getInformacionGeneral()
				+ "<br></br><br></br>";
		boolean primeraLinea = true;
		boolean bandera = false;
		for (int i = 0; i < dosisDeVacuna.length && !bandera; i++) {
			ColumnasVacunaDTO unaDosisdeVacuna = dosisDeVacuna[i];
			if (unaDosisdeVacuna.getDosis() == dosis.getDosis()) {
				bandera = true;
			} else if (unaDosisdeVacuna.getDosisAplicada() != null && unaDosisdeVacuna.getDosisAplicada().getEstado()
					.getEstado().equals(ConstantesVtrack.ESTADO_ACTIVO)) {
				if (primeraLinea) {
					primeraLinea = false;
					cuerpo += "Tus dosis/refuerzos anteriores fueron:";
				}
				tipoDosis = unaDosisdeVacuna.getDosis().getTipoDosis();
				if (tipoDosis.equals("R")) {
					tipoDosis = "Refuerzo";
				} else if (tipoDosis.equals("D")) {
					tipoDosis = "Dosis";
				}
				formatter = new SimpleDateFormat("dd-MMM-yyyy");
				Date fechaAplica = formatter.parse(unaDosisdeVacuna.getFechaAplicacion());
				fecha = formatter.format(fechaAplica);
				cuerpo += "<br></br>" + tipoDosis + " " + (i + 1) + ", el <strong>" + fecha + "</strong>.";
			}
		}
		cuerpo+="<br></br><br></br>Recuerda registrar tu aplicación en vtrack.com.co.";
		mensaje[0] = asunto;
		mensaje[1] = cuerpo;
		return mensaje;
	}

	/**
	 * @descripcion Este metodo se encarga de enviar un correo electronico desde
	 *              lacuenta Gmail vtrack.icesi@gmail.com
	 * @author Juan Bolaños
	 * @fecha 19/11/18
	 * @param cuenta cuaneta de usuario al que se le enviará el correo
	 * @param asunto del correo
	 * @param cuerpo del correo
	 * @throws Excepcion Excepcion en la conexión con el servidor SMTP de Google 
	 */
	public void enviarCorreo(Cuenta cuenta, String asunto, String cuerpo) throws Exception {
		try {
			if (cuenta == null) {
				throw new ExcepcionNegocio("La cuenta a la que se enviará el correo no puede ser nula");
			}
			if (cuenta.getCorreo() == null || cuenta.getCorreo().equals("")) {
				throw new ExcepcionNegocio("El correo destino no puede ser nulo");
			}
			// Esto es lo que va delante de @gmail.com en tu cuenta de correo. Es el
			// remitente también.
			// Esto es lo que va delante de @gmail.com en tu cuenta de correo. Es el
			// remitente también.
			String remitente = "vtrack.icesi"; // Para la dirección nomcuenta@gmail.com
			String clave = "Icesi2018";

			Properties props = System.getProperties();
			props.put("mail.smtp.host", "smtp.gmail.com"); // El servidor SMTP de Google
			props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
			props.put("mail.smtp.auth", "true"); // Usar autenticación mediante usuario y clave
			props.put("mail.smtp.starttls.enable", "true"); // Para conectar de manera segura al servidor SMTP
			props.put("mail.smtp.port", "587"); // El puerto SMTP seguro de Google

			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(remitente, clave);
				}
			});
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(remitente));
			message.addRecipients(Message.RecipientType.TO, cuenta.getCorreo()); // Se podrían añadir varios de la misma
																					// manera
			message.setSubject(asunto);
			String html = "<html>\r\n<head>\r\n</head>\r\n<body>\r\n\t<p>" + cuerpo + "</p>\r\n</body>\r\n</html>";
			message.setText(html, "utf-8", "html");
			Transport transport = session.getTransport("smtp");
			transport.connect("smtp.gmail.com", remitente, clave);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (MessagingException me) {
			log.error("Error al enviar el correo: " + me.getMessage());
		}

	}

}
