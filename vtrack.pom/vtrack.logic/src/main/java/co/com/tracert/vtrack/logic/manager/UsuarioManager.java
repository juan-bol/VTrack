package co.com.tracert.vtrack.logic.manager;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import co.com.tracert.vtrack.model.entities.Auditoria;
import co.com.tracert.vtrack.model.entities.Ciudad;
import co.com.tracert.vtrack.model.entities.Cuenta;
import co.com.tracert.vtrack.model.entities.Estado;
import co.com.tracert.vtrack.model.entities.Notificacion;
import co.com.tracert.vtrack.model.entities.Pais;
import co.com.tracert.vtrack.model.entities.Region;
import co.com.tracert.vtrack.model.entities.Rol;
import co.com.tracert.vtrack.model.entities.RolUsuario;
import co.com.tracert.vtrack.model.entities.TipoDocumentoPais;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.exceptions.ExcepcionNegocio;
import co.com.tracert.vtrack.persistence.dao.DaoGenerico;
import co.com.tracert.vtrack.persistence.dao.DaoVTrack;
import co.com.tracert.vtrack.model.constants.ConstantesVtrack;

import org.apache.commons.validator.routines.EmailValidator;

import com.google.common.hash.Hashing;

public class UsuarioManager {

	private EntityManager entityManager;

	public UsuarioManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * @author Diego Lamus
	 * @Descripcion busca un usuario en la base de datos
	 * @fecha 31/10/18
	 * @param id id del usuario que se quiere buscar en la base de datos
	 * @return usuario - instancia del usuario que se encontro en la base de datos
	 * @throws Exception si se genera algun error en la base de datos o el id del
	 *                   usuario no es valida
	 */
	public Usuario consultarUsuario(long id) throws Exception {
		if (id <= 0) {
			throw new ExcepcionNegocio("El id del usuario no es válido, verifique que se mayor que cero");
		}
		DaoGenerico<Usuario> dao = new DaoGenerico<>(entityManager, Usuario.class);
		Usuario usuario = dao.encontrarPorId(id);
		return usuario;
	}

	/**
	 * @author Juan Bolaños - Valery Ibarra
	 * @description Se registra un usuario
	 * @param usuario que se desea crear
	 * @fecha 30/10/18
	 * @throws Exception
	 */
	public Usuario crearUsuario(Usuario usuario) throws Exception {
		try {
			// Validar que el usuario no sea nulo
			if (usuario == null) {
				throw new ExcepcionNegocio("El usuario es nulo");
			}
			// Validar si el usuario ingreso un pais
			if (usuario.getPai() != null && usuario.getPai().getIdPais() != 0L) {
				DaoVTrack<Pais> daoPais = new DaoVTrack<Pais>(entityManager, Pais.class);
				usuario.setPai(daoPais.encontrarPorId(usuario.getPai().getIdPais()));
			}

			// Validar si el usuario ingreso una region
			if (usuario.getRegion() != null && usuario.getRegion().getIdRegion() != 0L) {
				DaoVTrack<Region> daoRegion = new DaoVTrack<Region>(entityManager, Region.class);
				usuario.setRegion(daoRegion.encontrarPorId(usuario.getRegion().getIdRegion()));
			}

			// Validar si el usuario ingreso una ciudad
			if (usuario.getCiudad() != null && usuario.getCiudad().getIdCiudad() != 0L) {
				DaoVTrack<Ciudad> daoCiudad = new DaoVTrack<Ciudad>(entityManager, Ciudad.class);
				usuario.setCiudad(daoCiudad.encontrarPorId(usuario.getCiudad().getIdCiudad()));
			}
			// Validar si se ingreso un tipo de documento
			if (usuario.getTipoDocumentoPai() != null && usuario.getTipoDocumentoPai().getIdTipoDocPais() != 0L) {
				DaoVTrack<TipoDocumentoPais> daoDocumento = new DaoVTrack<TipoDocumentoPais>(entityManager,
						TipoDocumentoPais.class);
				usuario.setTipoDocumentoPai(
						daoDocumento.encontrarPorId(usuario.getTipoDocumentoPai().getIdTipoDocPais()));
			}
			if (usuario.getNumeroDocumento() != null && usuario.getNumeroDocumento().trim().contains(",")) {
				throw new ExcepcionNegocio("El número de documento no puede contener comas (,)");
			}

			if (usuario.getNumeroDocumento() != null && usuario.getNumeroDocumento().length() > 20) {
				throw new ExcepcionNegocio("El número de documento no debe exceder los 20 caracteres");
			}

			if (usuario.getNumeroDocumento().equals("") && usuario.getTipoDocumentoPai() != null) {
				throw new ExcepcionNegocio("Si selecciona el tipo de documento, debe ingresar el número de documento");
			}

			if (!usuario.getNumeroDocumento().equals("") && usuario.getTipoDocumentoPai() == null) {
				throw new ExcepcionNegocio("Si ingresa el número de documento, debe seleccionar el tipo de documento");
			}

			// Validar si un numero de documento tiene letras dependiendo de su pais
			if (usuario.getNumeroDocumento() != null && !usuario.getNumeroDocumento().equals("")
					&& tieneLetras(usuario.getNumeroDocumento())) {
				if (usuario.getTipoDocumentoPai() != null
						&& usuario.getTipoDocumentoPai().getTieneLetras().equals(ConstantesVtrack.NO_TIENE_LETRAS)) {
					throw new ExcepcionNegocio("El número de documento no puede tener letras");
				}
			}

			// Validar que el numero de documento sea unico
			if (usuario.getNumeroDocumento() != null && !usuario.getNumeroDocumento().equals("")
					&& usuario.getTipoDocumentoPai() != null) {
				DaoVTrack<Usuario> daoDocumentoUnico = new DaoVTrack<>(entityManager, Usuario.class);
				Usuario usuarioBd = daoDocumentoUnico.consultarPorDocumentoUnico(usuario);
				if (usuarioBd != null) {
					throw new ExcepcionNegocio("El número de documento del tipo de documento "
							+ usuario.getTipoDocumentoPai().getTipoDocumento().getNombre() + " ya existe");
				}
			}
			if (usuario.getNombre() != null && usuario.getNombre().trim().contains(",")) {
				throw new ExcepcionNegocio("El nombre del usuario no puede contener comas (,)");
			}

			if (usuario.getNombre() != null && usuario.getNombre().length() > 50) {
				throw new ExcepcionNegocio("El nombre del usuario no debe exceder los 50 caracteres");
			}
			if (usuario.getFechaNacimiento() != null) {
				validarFecha(usuario.getFechaNacimiento());
			}
			if (usuario.getGenero() != null && !usuario.getGenero().equals("M") && !usuario.getGenero().equals("F")) {
				throw new ExcepcionNegocio("El género debe ser 'M' para masculino o 'F' para femenino");
			}

			if (usuario.getZipCode() != null && usuario.getZipCode().trim().contains(",")) {
				throw new ExcepcionNegocio("El zip code no puede contener comas (,)");
			}

			if (usuario.getZipCode() != null && usuario.getZipCode().length() > 20) {
				throw new ExcepcionNegocio("El zip code no debe exceder los 20 caracteres");
			}

			DaoVTrack<Usuario> dao = new DaoVTrack<Usuario>(entityManager, Usuario.class);
			// Verificar que la cuenta tenga su estado en la base de datos para poderse
			Estado estado = dao.encontrarEstado(Usuario.class.getName(), ConstantesVtrack.ESTADO_ACTIVO);
			usuario.setEstado(estado);
			// crear
			usuario = dao.crear(usuario);

			DaoVTrack<RolUsuario> daoRolUsuario = new DaoVTrack<RolUsuario>(entityManager, RolUsuario.class);
			Rol rol = daoRolUsuario.encontrarRolPorNombre(ConstantesVtrack.ROL_PACIENTE);
			RolUsuario rolUsuario = new RolUsuario();
			rolUsuario.setRol(rol);
			rolUsuario.setUsuario(usuario);
			daoRolUsuario.crear(rolUsuario);
		} catch (Exception e) {
			throw e;
		}
		return usuario;
	}

	/**
	 * @descripcion verifica si una cadena de caracteres tiene letras
	 * @author Victor Potes
	 * @fecha 3/11/2018
	 * @param text cadena de caracteres que se va a verificar
	 * @returns false si no tiene letras, true en caso contrario
	 */
	private boolean tieneLetras(String text) {
		boolean tieneLetras = false;
		for (int i = 0; i < text.length(); i++) {
			Character caracter = text.charAt(i);
			if (caracter > 57 || caracter < 48) {
				tieneLetras = true;
			}
		}
		return tieneLetras;

	}

	/**
	 * @descripcion Valida que la fecha sea valida
	 * @author Juan Bolaños
	 * @fecha 03/11/2018
	 * @param fecha fecha que el usuario ingreso
	 */
	public void validarFecha(Date fecha) throws Exception {
		try {
			String strFecha = fecha.toString();
			String[] splitted = strFecha.split(" ");
			int anio = Integer.parseInt(splitted[splitted.length - 1]);
			Calendar cal = Calendar.getInstance();
			if (anio < 1900) {
				throw new ExcepcionNegocio("El año de nacimiento ingresado no debe ser menor a 1900");
			}
			if (anio > cal.get(Calendar.YEAR)) {
				throw new ExcepcionNegocio("El año de nacimiento ingresado no debe ser mayor al actual");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @author Victor Potes
	 * @Descripcion Crea una nueva cuenta
	 * @param cuenta cuenta cuenta que se va a crear
	 * @return boolean - false si la cuenta no fue creada y true si la cuenta fue
	 *         creada
	 * @throws Exception Exepcion del negocio por problemas de bases de datos o
	 *                   reglas del negocio
	 */
	public Cuenta crearCuenta(Cuenta cuenta) throws Exception {

		// Verificar que la cuenta sea diferente de null
		if (cuenta == null) {
			throw new ExcepcionNegocio("La cuenta es nula");
		}
		// verificar que el correo de la cuenta de usuario no sea nula y no debe ser
		// vacía
		if (cuenta.getCorreo() == null || cuenta.getCorreo().trim().equals("")) {
			throw new ExcepcionNegocio("Debe ingresar un correo");
		}
		// verificar que el correo no tenga más de 50 caracteres
		if (cuenta.getCorreo().length() > 50) {
			throw new ExcepcionNegocio("El correo no debe exceder los 50 caracteres");
		}

		// verificar que sea una dirección de correo valida
		if (!EmailValidator.getInstance().isValid(cuenta.getCorreo())) {
			throw new ExcepcionNegocio("Por favor ingrese un correo válido");
		}

		DaoVTrack<Cuenta> dao = new DaoVTrack<>(entityManager, Cuenta.class);
		Cuenta cuentaEnBD = dao.encontrarCuentaPorCorreo(cuenta.getCorreo());
		// verificar que no haya otra cuenta con el correo ingresado
		if (cuentaEnBD != null) {
			throw new ExcepcionNegocio("El correo ingresado ya se encuentra registrado");
		}

		/**
		 * La verificacion de la contrasenia con confirmar contrasenia se hace a nivel
		 * de front-end
		 */

		// verificar que la contrasenia ingresada no sea nula ni vacia
		if (cuenta.getContrasenia() == null || cuenta.getContrasenia().trim().equals("")) {
			throw new ExcepcionNegocio("Debe ingresar una contraseña");
		}
		// Verificar que la contraseña tenga entre 8 y 16 caracteres
		if (cuenta.getContrasenia().length() < 8 || cuenta.getContrasenia().length() > 16) {
			throw new ExcepcionNegocio("La constraseña debe tener entre 8 y 16 caracteres");
		}
		// Verificar que la contraseña tenga al menos una letra mayuscula y una letra
		// minuscula
		if (!tieneMayusculaYMinuscula(cuenta.getContrasenia())) {
			throw new ExcepcionNegocio("La contraseña debe tener al menos una letra mayúscula y una letra minúscula");
		}
		// Verificar que la contraseña no contenga el correo
		if (cuenta.getContrasenia().contains(cuenta.getCorreo())) {
			throw new ExcepcionNegocio("La contraseña no puede contener el correo.");
		}

		// Verificar que la cuenta tenga su estado en la base de datos para poderse
		// crear
		try {
			Estado estado = dao.encontrarEstado(Cuenta.class.getName(), ConstantesVtrack.ESTADO_ACTIVO);
			cuenta.setEstado(estado);
		} catch (Exception e) {
			throw new ExcepcionNegocio("El estado de la entidad cuenta no existe");
		}

		// cifrar la contrasenia del usuario que entra por parametro
		String sha256hex = Hashing.sha256().hashString(cuenta.getContrasenia(), StandardCharsets.UTF_8).toString();
		cuenta.setContrasenia(sha256hex);

		return dao.crear(cuenta);

	}

	/**
	 * @author Victor Potes
	 * @descripción Busca la cuenta de acuerdo a su correo
	 * @parametro correo correo de la cuenta que se piensa retornar
	 * @returns Cuenta cuenta que tiene el correo ingresado por parametro
	 * @throws Exception En caso que no se encuentre encuentre la cuenta, significa
	 *                   que aun no se ha registrado con ese correo
	 * @fecha 10/11/2018
	 */
	public Cuenta encontrarCuentaPorCorreo(String correo) {
		// TODO Agregar encontrarCuentaPorCorreo al deployment
		DaoVTrack<Usuario> dao = new DaoVTrack<>(entityManager, Usuario.class);
		Cuenta cuenta = dao.encontrarCuentaPorCorreo(correo);
		return cuenta;

	}

	/**
	 * @descripcion verifica si una cadena de caracteres tiene al menos una
	 *              Mayuscula y Minuscula
	 * @author Victor Potes
	 * @fecha 3/11/2018
	 * @param text cadena de caracteres que se va a verificar
	 * @returns false si no tiene al menos una mayuscula y una minuscula y true en
	 *          caso contrario
	 */
	private boolean tieneMayusculaYMinuscula(String text) {
		boolean tieneMaYMin = true;

		boolean tieneMayuscula = false;
		boolean tieneMinuscula = false;
		for (int i = 0; i < text.length(); i++) {
			Character caracter = text.charAt(i);
			if (caracter > 64 && caracter < 91 && !tieneMayuscula) {
				tieneMayuscula = true;
			}
			if (caracter > 96 && caracter < 123 && !tieneMinuscula) {
				tieneMinuscula = true;
			}
			if (tieneMayuscula && tieneMinuscula) {
				break;
			}
		}

		if (!tieneMayuscula || !tieneMinuscula) {
			tieneMaYMin = false;
		}

		return tieneMaYMin;
	}

	/**
	 * @descripcion modifica la cuenta ingresada por parametro
	 * @author Victor Potes
	 * @fecha 3/11/2018
	 * @param cuenta cuenta que se va a modificar
	 * @returns la cuenta modificada
	 * @throws Exception Excepciones de reglas de negocio
	 */
	public Cuenta modificarCuenta(Cuenta cuenta) throws Exception {

		// Verificar que la cuenta sea diferente de null
		if (cuenta == null) {
			throw new ExcepcionNegocio("La cuenta es nula");
		}

		DaoVTrack<Cuenta> dao = new DaoVTrack<>(entityManager, Cuenta.class);
		Cuenta cuentaEnBD = dao.encontrarPorId(cuenta.getIdCuenta());

		// verificar que la cuenta que se va a modificar este en la BD
		if (cuentaEnBD == null) {
			throw new ExcepcionNegocio("La cuenta no existe");
		}

		// Validar que el usuario no este inactivo
		if (cuenta.getEstado().getEstado().equals(ConstantesVtrack.ESTADO_INACTIVO)) {
			throw new ExcepcionNegocio("La cuenta se encuentra inactiva");
		}

		// Verificar que no se modifique el correo
		if ((cuenta.getCorreo() == null || cuenta.getCorreo().trim().equals(""))) {
			throw new ExcepcionNegocio("El correo no se puede modificar");
		}

		if (!cuenta.getCorreo().equals(cuentaEnBD.getCorreo())) {
			throw new ExcepcionNegocio("El correo no se puede modificar");
		}

		/**
		 * La verificacion de la contrasenia con confirmar contrasenia se hace a nivel
		 * de front-end
		 */

		// verificar que la contrasenia ingresada no sea nula ni vacia
		if (cuenta.getContrasenia() == null || cuenta.getContrasenia().trim().equals("")) {
			throw new ExcepcionNegocio("Debe ingresar una contraseña");
		}
		// Verificar que la contraseña tenga entre 8 y 16 caracteres
		if (cuenta.getContrasenia().length() < 6 || cuenta.getContrasenia().length() > 16) {
			throw new ExcepcionNegocio("La constraseña debe tener entre 8 y 16 caracteres");
		}
		// Verificar que la contraseña tenga al menos una letra mayuscula y una letra
		// minuscula
		if (!tieneMayusculaYMinuscula(cuenta.getContrasenia())) {
			throw new ExcepcionNegocio("La contraseña debe tener al menos una letra mayúscula y una letra minúscula");
		}
		// Verificar que la contraseña no contenga el correo
		if (cuenta.getContrasenia().contains(cuenta.getCorreo())) {
			throw new ExcepcionNegocio("La contraseña no debe contener el correo");
		}
		// cifrar la contrasenia del usuario que entra por parametro
		String sha256hex = Hashing.sha256().hashString(cuenta.getContrasenia(), StandardCharsets.UTF_8).toString();
		cuenta.setContrasenia(sha256hex);
		// Se actualiza la cuenta
		return dao.actualizar(cuenta);

	}

	/**
	 * @descripcion Modifica el usuario ingresado
	 * @author Valery Ibarra
	 * @fecha 3/11/2018
	 * @param usuario Usuario que se va a modificar
	 * @returns El usuario modificado
	 * @throws Exception Excepciones de reglas de negocio
	 */
	public Usuario modificarUsuario(Usuario usuario) throws Exception {
		// Validar que el usuario no sea nulo
		if (usuario == null) {
			throw new ExcepcionNegocio("El usuario es nulo");
		}
		Usuario usuarioBd = consultarUsuario(usuario.getIdUsuario());
		// verificar que la cuenta que se va a modificar este en la BD
		if (usuarioBd == null) {
			throw new ExcepcionNegocio("El usuario no existe, no se encuentra creado previamente");
		}
		// Validar que el usuario en bd no este inactivo
		if (usuarioBd.getEstado().getEstado().equals(ConstantesVtrack.ESTADO_INACTIVO)) {
			throw new ExcepcionNegocio("El usuario se encuentra creado, pero inactivo");
		}
		// Validaciones en cada campo

		// Validar si el usuario ingreso un pais
		Pais pais = usuario.getPai();
		if (pais != null && pais.getIdPais() != 0L) {
			// Como si ingreso algo en el campo entonces lo cambio en la base de datos
			DaoVTrack<Pais> daoPais = new DaoVTrack<Pais>(entityManager, Pais.class);
			usuario.setPai(daoPais.encontrarPorId(usuario.getPai().getIdPais()));
		} else {
			usuario.setPai(null);
		}

		// Validar si el usuario ingreso una region
		Region region = usuario.getRegion();
		if (region != null && region.getIdRegion() != 0L) {
			// Como si ingreso algo en el campo entonces lo cambio en la base de datos
			DaoVTrack<Region> daoRegion = new DaoVTrack<Region>(entityManager, Region.class);
			usuario.setRegion(daoRegion.encontrarPorId(usuario.getRegion().getIdRegion()));
		} else {
			usuario.setRegion(null);
		}

		// Validar si el usuario ingreso una ciudad
		Ciudad ciudad = usuario.getCiudad();
		if (ciudad != null && ciudad.getIdCiudad() != 0L) {
			// Como si ingreso algo en el campo entonces lo cambio en la base de datos
			DaoVTrack<Ciudad> daoCiudad = new DaoVTrack<Ciudad>(entityManager, Ciudad.class);
			usuario.setCiudad(daoCiudad.encontrarPorId(usuario.getCiudad().getIdCiudad()));
		} else {
			usuario.setCiudad(null);
		}

		// Validar si se ingreso un tipo de documento
		TipoDocumentoPais tipoDocumentoPai = usuario.getTipoDocumentoPai();
		if (tipoDocumentoPai != null && tipoDocumentoPai.getIdTipoDocPais() != 0L) {
			// Como si ingreso algo en el campo entonces lo cambio en la base de datos
			DaoVTrack<TipoDocumentoPais> daoDocumento = new DaoVTrack<TipoDocumentoPais>(entityManager,
					TipoDocumentoPais.class);
			usuario.setTipoDocumentoPai(daoDocumento.encontrarPorId(tipoDocumentoPai.getIdTipoDocPais()));
		} else {
			usuario.setTipoDocumentoPai(null);
		}
		// Para validar usuario impersonate
		Cuenta cuentaUsuario = usuario.getCuenta();

		// Validar si se ingreso una fecha de nacimiento
		Date fechaNacimiento = usuario.getFechaNacimiento();
		if (fechaNacimiento != null) {
			// Validar la fecha de nacimiento del usuario
			validarFecha(usuario.getFechaNacimiento());
			// Como si ingreso algo en el campo entonces lo cambio en la base de datos
			usuario.setFechaNacimiento(fechaNacimiento);
		} else {
			// Validar que si es usuario impersonate se cumpla la restriccion de fecha de
			// nacimiento
			if (cuentaUsuario == null) {
				throw new ExcepcionNegocio(
						"El usuario " + usuario.getNombre() + " al no tener cuenta debe tener fecha de nacimiento");
			} else {
				usuario.setFechaNacimiento(null);
			}
		}

		// Validar si el usuario ingreso un genero
		String genero = usuario.getGenero();
		if (genero != null && !genero.trim().equals("")) {
			// Validar el genero del usuario
			if (usuario.getGenero() != null && !usuario.getGenero().equals("M") && !usuario.getGenero().equals("F")) {
				throw new ExcepcionNegocio("El genero debe ser 'M' para masulino o 'F' para femenino");
			}
			// Como si ingreso algo en el campo entonces lo cambio en la base de datos
			usuario.setGenero(genero);
		} else {
			// Validar que si es usuario impersonate se cumpla la restriccion de genero
			if (cuentaUsuario == null) {
				throw new ExcepcionNegocio(
						"El usuario " + usuario.getNombre() + " al no tener cuenta debe tener género");
			} else {
				usuario.setGenero(null);
			}
		}

		// Validar si el usuario ingreso un nombre
		String nombre = usuario.getNombre();
		if (nombre != null && !nombre.trim().equals("")) {
			// Validar el nombre del usuario
			if (usuario.getNombre() != null && usuario.getNombre().length() > 50) {
				throw new ExcepcionNegocio("El nombre del usuario no debe exceder los 50 caracteres");
			}
			if (usuario.getNombre() != null && usuario.getNombre().trim().contains(",")) {
				throw new ExcepcionNegocio("El nombre del usuario no puede contener comas (,)");
			}
			// Como si ingreso algo en el campo entonces lo cambio en la base de datos
			usuario.setNombre(nombre);
		} else {
			if (cuentaUsuario == null) {
				throw new ExcepcionNegocio(
						"El usuario " + usuario.getNombre() + " al no tener cuenta debe tener nombre");
			} else {
				usuario.setNombre("");
			}
		}

		// Validar si se ingreso un codigo postal
		String zipCode = usuario.getZipCode();
		if (zipCode != null && !zipCode.trim().equals("")) {
			// Validar el codigo de area del usuario
			if (usuario.getZipCode() != null && usuario.getZipCode().length() > 20) {
				throw new ExcepcionNegocio("El zip code no debe exceder los 20 caracteres");
			}
			if (usuario.getZipCode() != null && usuario.getZipCode().trim().contains(",")) {
				throw new ExcepcionNegocio("El zip code no puede contener comas (,)");
			}
			// Como si ingreso algo en el campo entonces lo cambio en la base de datos
			usuario.setZipCode(zipCode);
		} else {
			usuario.setZipCode("");
		}

		// Validar si el usuario tiene un numero de documento
		String numeroDocumento = usuario.getNumeroDocumento();
		if (numeroDocumento != null && !numeroDocumento.trim().equals("")) {
			// Validar el numero de documento
			if (numeroDocumento.length() > 20) {
				throw new ExcepcionNegocio("El número de documento no debe exceder los 20 caracteres");
			}
			if (numeroDocumento.trim().contains(",")) {
				throw new ExcepcionNegocio("El número de documento no puede contener comas (,)");
			}
			// Como si ingreso algo en el campo entonces lo cambio en la base de datos
			usuario.setNumeroDocumento(numeroDocumento);
		} else {
			usuario.setNumeroDocumento("");
		}

		if (usuario.getNumeroDocumento().trim().equals("") && usuario.getTipoDocumentoPai() != null) {
			throw new ExcepcionNegocio("Si selecciona el tipo de documento, debe ingresar el número de documento");
		}

		if (!usuario.getNumeroDocumento().trim().equals("") && usuario.getTipoDocumentoPai() == null) {
			throw new ExcepcionNegocio("Si ingresa el número de documento, debe seleccionar el tipo de documento");
		}

		// Validar si un numero de documento tiene letras dependiendo de su pais
		if (usuario.getNumeroDocumento() != null && !usuario.getNumeroDocumento().equals("")
				&& tieneLetras(usuario.getNumeroDocumento())) {
			if (usuario.getTipoDocumentoPai() != null
					&& usuario.getTipoDocumentoPai().getTieneLetras().equals(ConstantesVtrack.NO_TIENE_LETRAS)) {
				throw new ExcepcionNegocio("El número de documento no puede tener letras");
			}
		}

		DaoVTrack<Usuario> dao = new DaoVTrack<>(entityManager, Usuario.class);

		// Validar que el numero de documento sea unico
		if (usuario.getNumeroDocumento() != null && !usuario.getNumeroDocumento().equals("")
				&& usuario.getTipoDocumentoPai() != null) {
			Usuario usuarioBd2 = dao.consultarPorDocumentoUnico(usuario);
			if (usuarioBd2 != null && (usuarioBd2.getIdUsuario() != usuario.getIdUsuario())) {
				throw new ExcepcionNegocio("El número de documento del tipo de documento "
						+ usuario.getTipoDocumentoPai().getTipoDocumento().getNombre() + " ya existe");
			}
		}

		// Agregar notificacion al usuario
		usuario.setNotificacion(usuario.getNotificacion());

		// Se actualiza el usuario
		usuario = dao.actualizar(usuario);
		return usuario;
	}

	/**
	 * @descripcion Vincula usuarios, es decir, cambia en usuario su cuenta y cambia
	 *              en cuenta su usuario
	 * @author Valery Ibarra
	 * @fecha 6/11/2018
	 * @param cuenta  que se va a vincular, es decir, que se le va asignar su
	 *                usuario
	 * @param usuario que se va a vincular, es decir, que se le va asignar su cuenta
	 * @returns usuario que ya tiene la cuenta vinculada
	 * @throws Exception Excepciones de la base de datos
	 */
	public Usuario vincularCuentaUsuario(Cuenta cuenta, Usuario usuario) throws Exception {
		try {
			// Validar que la cuenta no sea nulo
			if (cuenta == null) {
				throw new ExcepcionNegocio("La cuenta a vincular es nula");
			}
			// Validar que la cuenta tenga id
			if (cuenta.getIdCuenta() <= 0L) {
				throw new Exception("La cuenta presenta identificacion invalida");
			}
			// Vincular usuario y cuenta
			cuenta.setUsuario(usuario);
			usuario.setCuenta(cuenta);
			// Creo el dao de la cuenta
			DaoVTrack<Cuenta> daoCuenta = new DaoVTrack<Cuenta>(entityManager, Cuenta.class);
			cuenta.setFechaInicioSesion(new Date(System.currentTimeMillis()));
			daoCuenta.actualizar(cuenta);
			// Creo el dao del usuario
			DaoVTrack<Usuario> daoUsuario = new DaoVTrack<Usuario>(entityManager, Usuario.class);
			usuario = daoUsuario.actualizar(usuario);
		} catch (Exception e) {
			throw e;
		}
		return usuario;
	}

	/**
	 * @descripcion Se modifica una notificaion
	 * @author Diego Lamus
	 * @fecha 19/11/2018
	 * @param notificacion notificacion que se va a modificar en la base de datos
	 * @throws Exception en caso de existir lguna inconsistencia en los datos
	 */
	public Notificacion modificarNotificacion(Notificacion notificacion) throws Exception {
		// Validar que la notificacion no sea nula
		if (notificacion == null) {
			throw new ExcepcionNegocio("Se debe ingresar una notificación");
		}
		// Validar que e usuario no sea nulo
		if (notificacion.getUsuario() == null) {
			throw new ExcepcionNegocio("La notificación debe estar asociada a un usuario");
		}
		// Validar que los dias de anticipacion no sean nulos
		if (notificacion.getDiasAnticipacion() == null) {
			throw new ExcepcionNegocio("Se deben ingresar los dias de anticipación");
		}
		// Validar que los dias de anticiapacion esten entre 0 y 365
		if (notificacion.getDiasAnticipacion().intValue() < 0 || notificacion.getDiasAnticipacion().intValue() > 365) {
			throw new ExcepcionNegocio("Los dias de anticipación deben estar entre 0 y 365 dias");
		}
		// Validar que el estado no sea nulo
		if (notificacion.getEstado() == null) {
			throw new ExcepcionNegocio("Se debe ingresar un estado para la notificación");

		}
		DaoVTrack<Notificacion> dao = new DaoVTrack<>(entityManager, Notificacion.class);
		// Encontrar estado en la base de datos

		Estado estado = dao.encontrarEstado(Notificacion.class.getName(), notificacion.getEstado().getEstado());

		notificacion.setEstado(estado);

		return dao.actualizar(notificacion);
	}

	/**
	 * @descripcion Se crea una notificaion
	 * @author Diego Lamus
	 * @fecha 19/11/2018
	 * @param notificacion notificacion que se va a crear en la base de datos
	 * @return notificacion notificacion creada en la base de datos
	 * @throws Exception en caso de existir lguna inconsistencia en los datos
	 */
	public Notificacion crearNotificacion(Notificacion notificacion) throws Exception {
		// Validar que la notificacion no sea nula
		if (notificacion == null) {
			throw new ExcepcionNegocio("Se debe ingresar una notificación");
		}
		// Validar que e usuario no sea nulo
		if (notificacion.getUsuario() == null) {
			throw new ExcepcionNegocio("La notificación debe estar asociada a un usuario");
		}
		// Validar que los dias de anticipacion no sean nulos
		if (notificacion.getDiasAnticipacion() == null) {
			throw new ExcepcionNegocio("Se deben ingresar los dias de anticipación");
		}
		// Validar que los dias de anticiapacion esten entre 0 y 365
		if (notificacion.getDiasAnticipacion().intValue() < 0 || notificacion.getDiasAnticipacion().intValue() > 365) {
			throw new ExcepcionNegocio("Los dias de anticipación deben estar entre 0 y 365 dias");
		}
		DaoVTrack<Notificacion> dao = new DaoVTrack<>(entityManager, Notificacion.class);
		// establecer el estado
		Estado estado = dao.encontrarEstado(Notificacion.class.getName(), ConstantesVtrack.ESTADO_ACTIVO);
		notificacion.setEstado(estado);
		return dao.crear(notificacion);
	}

	/**
	 * @descripcion Listar todos usuarios del sistema
	 * @return lista de todos usuarios
	 * @throws Exception
	 */
	public List<Usuario> listarUsuarios() throws Exception {
		DaoVTrack<Usuario> dao = new DaoVTrack<>(entityManager, Usuario.class);
		List<Usuario> usuarios = dao.encontrarTodos();
		return usuarios;
	}

	/**
	 * @descripcion Crea un usuario y una cuenta
	 * @author Diego Lamus
	 * @fecha 28/11/2018
	 * @param usuario Usuario que quiere crearse
	 * @param cuenta  Cuenta del usuario que se quiere crear
	 * @returns Usuario creado con la cuenta asignada
	 * @throws Exception si no es posible crear la cuenta o le usuario
	 */
	public Usuario crearCuentaUsuario(Usuario usuario, Cuenta cuenta) throws Exception {
		usuario = crearUsuario(usuario);
		cuenta = crearCuenta(cuenta);
		usuario.setCuenta(cuenta);
		return usuario;
	}

	/**
	 * @descripcion Realiza la auditoria del registro de un usuario
	 * @author Victor Potes
	 * @fecha 30/11/2018
	 * @param usuarioRegistrado Usuario al que se le va a auditar su registro
	 * @throws Exception si no es posible auditar el registro del usuario
	 */
	public void auditarRegistro(Usuario usuarioRegistrado) throws Exception {

		Cuenta cuentaRegistrada = usuarioRegistrado.getCuenta();

		if (cuentaRegistrada == null) {
			throw new ExcepcionNegocio("No se puede auditar el registro del usuario porque su cuenta es nula.");
		}
		Auditoria auditarRegistro = new Auditoria();

		String informacionRegistro = "";

		// Orden en que se ingresan los parametros en valor nuevo, el orden es de
		// acuerdo a como aparecen en el modelo de datos

		// Id del usuario
		if (usuarioRegistrado.getIdUsuario() <= 0L) {
			informacionRegistro = informacionRegistro.concat("<null>" + ",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getIdUsuario() + ",");
		}
		// Valor de la contrasenia, solo cambia de null cuando se cambia la contrasenia
		informacionRegistro = informacionRegistro.concat("<null>,");

		// Numero de documento del usuario
		if (usuarioRegistrado.getNumeroDocumento() == null
				|| usuarioRegistrado.getNumeroDocumento().trim().equals("")) {
			informacionRegistro = informacionRegistro.concat(",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getNumeroDocumento() + ",");
		}

		// Nombre del usuario
		if (usuarioRegistrado.getNombre() == null || usuarioRegistrado.getNombre().trim().equals("")) {
			informacionRegistro = informacionRegistro.concat(",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getNombre() + ",");
		}

		// fecha de nacimiento del usuario
		if (usuarioRegistrado.getFechaNacimiento() == null) {
			informacionRegistro = informacionRegistro.concat("<null>" + ",");
		} else {
			String[] fecha = usuarioRegistrado.getFechaNacimiento().toString().split(" ");
			informacionRegistro = informacionRegistro
					.concat("" + fecha[2] + "/" + ConstantesVtrack.TRADUCTOR_MESES.get(fecha[1]) + "/"
							+ fecha[5].charAt(2) + fecha[5].charAt(3) + ",");
		}

		// Genero del usuario
		if (usuarioRegistrado.getGenero() == null || usuarioRegistrado.getGenero().equals("")) {
			informacionRegistro = informacionRegistro.concat(",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getGenero() + ",");
		}

		// Zipcode del usuario
		if (usuarioRegistrado.getZipCode() == null || usuarioRegistrado.getZipCode().trim().equals("")) {
			informacionRegistro = informacionRegistro.concat(",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getZipCode() + ",");
		}

		// id de la ciudad del usuario
		if (usuarioRegistrado.getCiudad() == null || usuarioRegistrado.getCiudad().getIdCiudad() <= 0L) {
			informacionRegistro = informacionRegistro.concat("<null>" + ",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getCiudad().getIdCiudad() + ",");
		}

		// id del tipo de documento del usuario
		if (usuarioRegistrado.getTipoDocumentoPai() == null
				|| usuarioRegistrado.getTipoDocumentoPai().getIdTipoDocPais() <= 0L) {
			informacionRegistro = informacionRegistro.concat("<null>" + ",");
		} else {
			informacionRegistro = informacionRegistro
					.concat("" + usuarioRegistrado.getTipoDocumentoPai().getIdTipoDocPais() + ",");
		}

		// id del estado del usuario
		if (usuarioRegistrado.getEstado() == null) {
			informacionRegistro = informacionRegistro.concat("<null>" + ",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getEstado().getIdEstado() + ",");
		}

		// id de la region del usuario
		if (usuarioRegistrado.getRegion() == null || usuarioRegistrado.getRegion().getIdRegion() <= 0L) {
			informacionRegistro = informacionRegistro.concat("<null>" + ",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getRegion().getIdRegion() + ",");
		}

		// id del pais del usuario
		if (usuarioRegistrado.getPai() == null || usuarioRegistrado.getPai().getIdPais() <= 0L) {
			informacionRegistro = informacionRegistro.concat("<null>" + ",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getPai().getIdPais() + ",");
		}

		// id de la cuenta del usuario
		if (usuarioRegistrado.getCuenta() == null || usuarioRegistrado.getCuenta().getIdCuenta() <= 0L) {
			informacionRegistro = informacionRegistro.concat("<null>" + ",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getCuenta().getIdCuenta() + ",");
		}

		// id de la notificacion del usuario
		if (usuarioRegistrado.getNotificacion() == null
				|| usuarioRegistrado.getNotificacion().getIdNotificacion() <= 0L) {
			informacionRegistro = informacionRegistro.concat("<null>");
		} else {
			informacionRegistro = informacionRegistro
					.concat("" + usuarioRegistrado.getNotificacion().getIdNotificacion() + "");
		}

		auditarRegistro.setFecha(new Date());
		auditarRegistro.setAccion("C");
		auditarRegistro.setNombreTablaAfectada(Usuario.class.getSimpleName());
		auditarRegistro.setIdTablaAfectada(new BigDecimal(usuarioRegistrado.getIdUsuario()));
		auditarRegistro.setUsuario(usuarioRegistrado);
		auditarRegistro.setValorAnterior(null);
		auditarRegistro.setValorNuevo(informacionRegistro);

		DaoVTrack<Auditoria> daoAuditoria = new DaoVTrack<>(entityManager, Auditoria.class);
		daoAuditoria.crear(auditarRegistro);

	}

	/**
	 * @descripcion Se audita la creación de un usuario asociado
	 * @author Victor Potes
	 * @fecha 09/12/2018
	 * @param usuarioRegistrado Es el usuario asociado que se crea
	 * @param usuarioCreador    Es el usuario quien realiza la acción de crear el
	 *                          usuario asociado
	 * @throws Exception en caso de existir lguna inconsistencia en los datos
	 */
	public void auditarCrearUsuarioAsociado(Usuario usuarioCreador, Usuario usuarioRegistrado) throws Exception {

		Cuenta cuentaLogueada = usuarioCreador.getCuenta();

		if (cuentaLogueada == null) {
			throw new ExcepcionNegocio(
					"No se puede auditar el registro del usuario porque la cuenta del usuario logueado es null.");
		}
		Auditoria auditarRegistro = new Auditoria();

		String informacionRegistro = "";

		// Orden en que se ingresan los parametros en valor nuevo, el orden es de
		// acuerdo a como aparecen en el modelo de datos

		// Id del usuario
		if (usuarioRegistrado.getIdUsuario() <= 0L) {
			informacionRegistro = informacionRegistro.concat("<null>" + ",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getIdUsuario() + ",");
		}

		// Valor de la contrasenia, solo cambia de null cuando se cambia la contrasenia
		informacionRegistro = informacionRegistro.concat("<null>,");

		// Numero de documento del usuario
		if (usuarioRegistrado.getNumeroDocumento() == null
				|| usuarioRegistrado.getNumeroDocumento().trim().equals("")) {
			informacionRegistro = informacionRegistro.concat(",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getNumeroDocumento() + ",");
		}

		// Nombre del usuario es obligatorio

		informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getNombre() + ",");

		// fecha de nacimiento del usuario es obligatorio
		String[] fecha = usuarioRegistrado.getFechaNacimiento().toString().split(" ");
		informacionRegistro = informacionRegistro.concat("" + fecha[2] + "/"
				+ ConstantesVtrack.TRADUCTOR_MESES.get(fecha[1]) + "/" + fecha[5].charAt(2) + fecha[5].charAt(3) + ",");

		// Genero del usuario es obligatorio
		informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getGenero() + ",");

		// Zipcode del usuario
		if (usuarioRegistrado.getZipCode() == null || usuarioRegistrado.getZipCode().trim().equals("")) {
			informacionRegistro = informacionRegistro.concat(",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getZipCode() + ",");
		}

		// id de la ciudad del usuario
		if (usuarioRegistrado.getCiudad() == null || usuarioRegistrado.getCiudad().getIdCiudad() <= 0L) {
			informacionRegistro = informacionRegistro.concat("<null>" + ",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getCiudad().getIdCiudad() + ",");
		}

		// id del tipo de documento del usuario
		if (usuarioRegistrado.getTipoDocumentoPai() == null
				|| usuarioRegistrado.getTipoDocumentoPai().getIdTipoDocPais() <= 0L) {
			informacionRegistro = informacionRegistro.concat("<null>" + ",");
		} else {
			informacionRegistro = informacionRegistro
					.concat("" + usuarioRegistrado.getTipoDocumentoPai().getIdTipoDocPais() + ",");
		}

		// id del estado del usuario
		if (usuarioRegistrado.getEstado() == null) {
			informacionRegistro = informacionRegistro.concat("<null>" + ",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getEstado().getIdEstado() + ",");
		}

		// id de la region del usuario
		if (usuarioRegistrado.getRegion() == null || usuarioRegistrado.getRegion().getIdRegion() <= 0L) {
			informacionRegistro = informacionRegistro.concat("<null>" + ",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getRegion().getIdRegion() + ",");
		}

		// id del pais del usuario
		if (usuarioRegistrado.getPai() == null || usuarioRegistrado.getPai().getIdPais() <= 0L) {
			informacionRegistro = informacionRegistro.concat("<null>" + ",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getPai().getIdPais() + ",");
		}

		// id de la cuenta del usuario
		if (usuarioRegistrado.getCuenta() == null || usuarioRegistrado.getCuenta().getIdCuenta() <= 0L) {
			informacionRegistro = informacionRegistro.concat("<null>" + ",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioRegistrado.getCuenta().getIdCuenta() + ",");
		}

		// id de la notificacion del usuario
		if (usuarioRegistrado.getNotificacion() == null
				|| usuarioRegistrado.getNotificacion().getIdNotificacion() <= 0L) {
			informacionRegistro = informacionRegistro.concat("<null>");
		} else {
			informacionRegistro = informacionRegistro
					.concat("" + usuarioRegistrado.getNotificacion().getIdNotificacion() + "");
		}

		auditarRegistro.setFecha(new Date());
		auditarRegistro.setAccion("C");
		auditarRegistro.setNombreTablaAfectada(Usuario.class.getSimpleName());
		auditarRegistro.setIdTablaAfectada(new BigDecimal(usuarioRegistrado.getIdUsuario()));
		auditarRegistro.setUsuario(usuarioCreador);
		auditarRegistro.setValorAnterior(null);
		auditarRegistro.setValorNuevo(informacionRegistro);

		DaoVTrack<Auditoria> daoAuditoria = new DaoVTrack<>(entityManager, Auditoria.class);
		daoAuditoria.crear(auditarRegistro);

	}

	/**
	 * @descripcion Se audita la modificación de perfil de un usuario
	 * @author Victor Potes
	 * @fecha 09/12/2018
	 * @param usuarioAnterior   Es el usuario antes de efectuarle los cambios de la
	 *                          modificación
	 * @param usuarioModificado Es el usuario después de efectuarle los cambios de
	 *                          la modificación
	 * @param usuarioCreador    Es el usuario quien realiza la acción de modificar
	 *                          el perfil
	 * @param cambioContrasenia true si el usuario cambió la contraseña, false en
	 *                          caso contrario
	 * @throws Exception en caso de existir lguna inconsistencia en los datos
	 */
	public void auditarModificarPerfil(Usuario usuarioAnterior, Usuario usuarioModificado, Usuario usuarioCreador,
			boolean cambioContrasenia) throws Exception {

		Cuenta cuentaRegistrada = usuarioCreador.getCuenta();

		if (cuentaRegistrada == null) {
			throw new ExcepcionNegocio("No se puede auditar el registro del usuario porque la cuenta del "
					+ "usuario logueado está null.");
		}
		Auditoria auditarRegistro = new Auditoria();

		String informacionRegistro = "";
		String informacionRegistroAnterior = "";

		// Orden en que se ingresan los parametros en valor nuevo, el orden es de
		// acuerdo a como aparecen en el modelo de datos

		DaoVTrack<Auditoria> daoAuditoria = new DaoVTrack<>(entityManager, Auditoria.class);

		List<Auditoria> auditoriasAnteriores = daoAuditoria.auditoriaMasCercanaPorFecha(usuarioCreador,
				usuarioModificado);
		if (auditoriasAnteriores != null && auditoriasAnteriores.size() != 0) {
			Auditoria auditoriaAnterior = auditoriasAnteriores.get(auditoriasAnteriores.size() - 1);
			String valorNuevoAnteriorAuditorio = auditoriaAnterior.getValorNuevo();
			informacionRegistroAnterior = valorNuevoAnteriorAuditorio;

		}

		// Id del usuario -> Se supone que los usuarios que entran no deben tener id
		// null, por lo que no entrarian a ese condicional
		if (usuarioModificado.getIdUsuario() <= 0L) {
			informacionRegistro = informacionRegistro.concat("<null>" + ",");
		} else {
			informacionRegistro = informacionRegistro.concat("" + usuarioModificado.getIdUsuario() + ",");
		}

		if (cambioContrasenia) {
			informacionRegistro = informacionRegistro.concat("cambio contraseña,");
		} else {
			informacionRegistro = informacionRegistro.concat("<null>,");
		}

		// Numero de documento del usuario
		if (usuarioAnterior.getNumeroDocumento() == null) {
			if (usuarioModificado.getNumeroDocumento() == null) {
				informacionRegistro = informacionRegistro.concat("<null>,");
			} else {
				informacionRegistro = informacionRegistro
						.concat("" + usuarioModificado.getNumeroDocumento().trim() + ",");
			}
		} else {
			if (usuarioModificado.getNumeroDocumento() == null) {
				informacionRegistro = informacionRegistro.concat(",");
			} else {
				informacionRegistro = informacionRegistro
						.concat("" + usuarioModificado.getNumeroDocumento().trim() + ",");
			}
		}

		// Nombre del usuario
		if (usuarioAnterior.getNombre() == null) {
			if (usuarioModificado.getNombre() == null) {
				informacionRegistro = informacionRegistro.concat("<null>,");
			} else {
				informacionRegistro = informacionRegistro.concat("" + usuarioModificado.getNombre().trim() + ",");
			}
		} else {
			if (usuarioModificado.getNombre() == null) {
				informacionRegistro = informacionRegistro.concat(",");
			} else {
				informacionRegistro = informacionRegistro.concat("" + usuarioModificado.getNombre().trim() + ",");
			}
		}

		// Fecha de nacimiento del usuario
		if (usuarioAnterior.getFechaNacimiento() == null) {
			if (usuarioModificado.getFechaNacimiento() == null) {
				informacionRegistro = informacionRegistro.concat("<null>,");
			} else {
				String dateA = new java.text.SimpleDateFormat("dd/MM/yyyy")
						.format(usuarioModificado.getFechaNacimiento().getTime());
				informacionRegistro = informacionRegistro.concat("" + dateA + ",");
			}
		} else {
			if (usuarioModificado.getNombre() == null) {
				informacionRegistro = informacionRegistro.concat(",");
			} else {
				String dateB = new java.text.SimpleDateFormat("dd/MM/yyyy")
						.format(usuarioModificado.getFechaNacimiento().getTime());
				informacionRegistro = informacionRegistro.concat("" + dateB + ",");
			}
		}

		// Genero del usuario
		if (usuarioAnterior.getGenero() == null || usuarioAnterior.getGenero().equals("")) {
			if (usuarioModificado.getGenero() == null || usuarioModificado.getGenero().equals("")) {
				informacionRegistro = informacionRegistro.concat(",");
			} else {
				informacionRegistro = informacionRegistro.concat("" + usuarioModificado.getGenero().trim() + ",");
			}
		} else {
			if (usuarioModificado.getGenero() == null) {
				informacionRegistro = informacionRegistro.concat(",");
			} else {
				informacionRegistro = informacionRegistro.concat("" + usuarioModificado.getGenero().trim() + ",");
			}
		}

		// Genero del usuario
		if (usuarioAnterior.getZipCode() == null) {
			if (usuarioModificado.getZipCode() == null) {
				informacionRegistro = informacionRegistro.concat("<null>,");
			} else {
				informacionRegistro = informacionRegistro.concat("" + usuarioModificado.getZipCode().trim() + ",");
			}
		} else {
			if (usuarioModificado.getZipCode() == null) {
				informacionRegistro = informacionRegistro.concat(",");
			} else {
				informacionRegistro = informacionRegistro.concat("" + usuarioModificado.getZipCode().trim() + ",");
			}
		}

		// id de la ciudad del usuario
		if (usuarioAnterior.getCiudad() == null || usuarioAnterior.getCiudad().getIdCiudad() <= 0L) {
			if (usuarioModificado.getCiudad() == null || usuarioModificado.getCiudad().getIdCiudad() <= 0L) {
				informacionRegistro = informacionRegistro.concat("<null>" + ",");
			} else {
				informacionRegistro = informacionRegistro
						.concat("" + usuarioModificado.getCiudad().getIdCiudad() + ",");
			}
		} else {
			if (usuarioModificado.getCiudad() == null || usuarioModificado.getCiudad().getIdCiudad() <= 0L) {
				informacionRegistro = informacionRegistro.concat(",");
			} else {
				informacionRegistro = informacionRegistro
						.concat("" + usuarioModificado.getCiudad().getIdCiudad() + ",");
			}
		}

		// id del tipo de documento del usuario
		if (usuarioAnterior.getTipoDocumentoPai() == null
				|| usuarioAnterior.getTipoDocumentoPai().getIdTipoDocPais() <= 0L) {
			if (usuarioModificado.getTipoDocumentoPai() == null
					|| usuarioModificado.getTipoDocumentoPai().getIdTipoDocPais() <= 0L) {
				informacionRegistro = informacionRegistro.concat("<null>" + ",");
			} else {
				informacionRegistro = informacionRegistro
						.concat("" + usuarioModificado.getTipoDocumentoPai().getIdTipoDocPais() + ",");
			}
		} else {
			if (usuarioModificado.getTipoDocumentoPai() == null
					|| usuarioModificado.getTipoDocumentoPai().getIdTipoDocPais() <= 0L) {
				informacionRegistro = informacionRegistro.concat(",");
			} else {
				informacionRegistro = informacionRegistro
						.concat("" + usuarioModificado.getTipoDocumentoPai().getIdTipoDocPais() + ",");
			}
		}

		// id del estado del usuario
		if (usuarioAnterior.getEstado() == null || usuarioAnterior.getEstado().getIdEstado() <= 0L) {
			if (usuarioModificado.getEstado() == null || usuarioModificado.getEstado().getIdEstado() <= 0L) {
				informacionRegistro = informacionRegistro.concat("<null>" + ",");
			} else {
				informacionRegistro = informacionRegistro
						.concat("" + usuarioModificado.getEstado().getIdEstado() + ",");
			}
		} else {
			if (usuarioModificado.getEstado() == null || usuarioModificado.getEstado().getIdEstado() <= 0L) {
				informacionRegistro = informacionRegistro.concat(",");
			} else {
				informacionRegistro = informacionRegistro
						.concat("" + usuarioModificado.getEstado().getIdEstado() + ",");
			}
		}

		// id de la region del usuario
		if (usuarioAnterior.getRegion() == null || usuarioAnterior.getRegion().getIdRegion() <= 0L) {
			if (usuarioModificado.getRegion() == null || usuarioModificado.getRegion().getIdRegion() <= 0L) {
				informacionRegistro = informacionRegistro.concat("<null>" + ",");
			} else {
				informacionRegistro = informacionRegistro
						.concat("" + usuarioModificado.getRegion().getIdRegion() + ",");
			}
		} else {
			if (usuarioModificado.getRegion() == null || usuarioModificado.getRegion().getIdRegion() <= 0L) {
				informacionRegistro = informacionRegistro.concat(",");
			} else {
				informacionRegistro = informacionRegistro
						.concat("" + usuarioModificado.getRegion().getIdRegion() + ",");
			}
		}

		// id del pais del usuario
		if (usuarioAnterior.getPai() == null || usuarioAnterior.getPai().getIdPais() <= 0L) {
			if (usuarioModificado.getPai() == null || usuarioModificado.getPai().getIdPais() <= 0L) {
				informacionRegistro = informacionRegistro.concat("<null>" + ",");
			} else {
				informacionRegistro = informacionRegistro.concat("" + usuarioModificado.getPai().getIdPais() + ",");
			}
		} else {
			if (usuarioModificado.getPai() == null || usuarioModificado.getPai().getIdPais() <= 0L) {
				informacionRegistro = informacionRegistro.concat(",");
			} else {
				informacionRegistro = informacionRegistro.concat("" + usuarioModificado.getPai().getIdPais() + ",");
			}
		}

		// id de la cuenta del usuario
		if (usuarioAnterior.getCuenta() == null || usuarioAnterior.getCuenta().getIdCuenta() <= 0L) {
			if (usuarioModificado.getCuenta() == null || usuarioModificado.getCuenta().getIdCuenta() <= 0L) {
				informacionRegistro = informacionRegistro.concat("<null>" + ",");
			} else {
				informacionRegistro = informacionRegistro
						.concat("" + usuarioModificado.getCuenta().getIdCuenta() + ",");
			}
		} else {
			if (usuarioModificado.getCuenta() == null || usuarioModificado.getCuenta().getIdCuenta() <= 0L) {
				informacionRegistro = informacionRegistro.concat(",");
			} else {
				informacionRegistro = informacionRegistro
						.concat("" + usuarioModificado.getCuenta().getIdCuenta() + ",");
			}
		}

		// id de la notificacion del usuario
		if (usuarioAnterior.getNotificacion() == null || usuarioAnterior.getNotificacion().getIdNotificacion() <= 0L) {
			if (usuarioModificado.getNotificacion() == null
					|| usuarioModificado.getNotificacion().getIdNotificacion() <= 0L) {
				informacionRegistro = informacionRegistro.concat("<null>");
			} else {
				informacionRegistro = informacionRegistro
						.concat("" + usuarioModificado.getNotificacion().getIdNotificacion() + "");
			}
		} else {
			if (usuarioModificado.getNotificacion() == null
					|| usuarioModificado.getNotificacion().getIdNotificacion() <= 0L) {
				informacionRegistro = informacionRegistro.concat(" ");
			} else {
				informacionRegistro = informacionRegistro
						.concat("" + usuarioModificado.getNotificacion().getIdNotificacion() + "");
			}
		}

		auditarRegistro.setFecha(new Date());
		auditarRegistro.setAccion("M");
		auditarRegistro.setNombreTablaAfectada(Usuario.class.getSimpleName());
		auditarRegistro.setIdTablaAfectada(new BigDecimal(usuarioModificado.getIdUsuario()));
		auditarRegistro.setUsuario(usuarioCreador);
		auditarRegistro.setValorAnterior(informacionRegistroAnterior);
		auditarRegistro.setValorNuevo(informacionRegistro);

		daoAuditoria.crear(auditarRegistro);

	}

}