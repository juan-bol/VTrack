package co.com.tracert.vtrack.model.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConstantesVtrack {

	/**
	 * Hace referencia a la clave para obtener en usuario en sesión
	 */
	public final static String USUARIO = "USUARIO";
	
	/**
	 * Hace referencia a la clave para obtener el usuario impersonate en sesión
	 */
	public final static String USUARIO_IMP = "USUARIO_IMP";
	
	/**
	 * Hace referencia a la clave para obtener el permiso seleccionado en el select one menu
	 */
	public final static String PERMISO_SELECCIONADO = "PERMISO_SELECCIONADO";

	/**
	 * Hace referencia a la página contenido que se muestra después del login
	 */
	public static final String PAGINA_INICIO = "/paginas/esquemasSugeridos.xhtml";

	/**
	 * Hace referencia a la página de login para ser únicamente usada en el filtro 
	 * No sirve en para redireccionar debido a el "vtrack.web" en el url
	 */
	public static final String PAGINA_LOGIN = "/vtrack.web/index.xhtml";
	
	/**
	 * Hace referencia a la página de login para ser usada en cualquier clase
	 */
	public static final String PAGINA_LOGIN_SIMPLE = "/index.xhtml";

	/**
	 * Permite redirigir de una página a otra correctamente
	 */
	public static final String REDIRECION_PAGINAS = "?faces-redirect=true";

	/**
	 * Hace referencia a la página en la cual un usuario puede registrarse
	 */
	public static final String PAGINA_REGISTRO = "crearCuentaUsuario.xhtml";
	
	/**
	 * Hace referencia a la página en a cual el usuario puede ver su información
	 */
	public static final String PAGINA_PERFIL = "/paginas/ajustes.xhtml";

	/**
	 * Hace referencia a los tipos de documento del pais por defecto. Esto se
	 * realiza al iniciar el registro. El pais por defecto será Colombia
	 */
	public static final long TIPODOCUMENTO_PAIS = 1L;
	
	/**
	 * Hace referencia al estado Activo que puede tomar cualquier entidad
	 */
	public static final String ESTADO_ACTIVO = "ACTIVO";

	/**
	 * Hace referencia al estado Inactivo que puede tomar cualquier entidad
	 */
	public static final String ESTADO_INACTIVO = "INACTIVO";

	/**
	 * Hace referencia al estado Aprobado que puede tomar el permiso
	 */
	public static final String ESTADO_APROBADO = "APROBADO";

	/**
	 * Hace referencia al estado En Espera que puede tomar el permiso
	 */
	public static final String ESTADO_EN_ESPERA = "EN ESPERA";

	/**
	 * Hace referencia al estado Ignorado que puede tomar el permiso
	 */
	public static final String ESTADO_IGNORADO = "IGNORADO";
	
	/**
	 * Hace referencia al estado ON del componente Switch.
	 */
	public static final Boolean SWITCH_ON = true;
	
	/**
	 * Hace referencia al estado OFF del componente Switch.
	 */
	public static final Boolean SWITCH_OFF = false;
	
	/**
	 * Hace referencia al color del botón cuando la dosis de una vacuna ya fue aplicada y tiene estado ACTIVO.
	 */
	public static final String COLOR_ACTIVO = "#4fc54f";
	
	/**
	 * Hace referencia al color del botón cuando la dosis de una vacuna ya fue aplicada y tiene estado INACTIVO,
	 * es decir, fue aplicada la dosis alguna vez, pero la cancelaron después.
	 */
	public static final String COLOR_INACTIVO = "gray";
	
	/**
	 * Hace referencia al color del círculo de la dosis que son refuerzos
	 */
	public static final String COLOR_REFUERZO = "yellow";
	
	/**
	 * Hace referencia al tipo de dosis que es una dosis normal
	 */
	public static final String TIPO_DOSIS_NORMAL = "D";
	
	/**
	 * Hace referencia al tipo de dosis que es una dosis refuerzo
	 */
	public static final String TIPO_DOSIS_REFUERZO = "R";
	
	/**
	 * Hace referencia al género masculino del sistema Vtrack
	 */
	public static final String MASCULINO = "Masculino";
	
	/**
	 * Hace referencia al género femenino del sistema Vtrack
	 */
	public static final String FEMENINO = "Femenino";
	
	/**
	 * Hace referencia al tipo de permiso de lectura que puede tomar un permiso
	 * estados
	 */
	public static final String PERMISO_LECTURA = "L";
	/**
	 * Hace referencia al tipoo de permiso de lectura y escritura que puede tomar un permiso
	 * estados
	 */
	public static final String PERMISO_ESCRITURA = "E";
	
	/**
	 * Hace referencia al tipoo de permiso de lectura y escritura que puede tomar alguien que tiene un asociado
	 */
	public static final String PERMISO_ASOCIADO = "A";
	
	
	/**
	 * Hace referencia al menejo de los SelectOneMenu en la vista del perfil.
	 */
	public static final long INVALIDO = -99999;

	/**
	 * El pais por defecto será Colombia
	 */
	public static final long PAIS_DEFECTO = 1L;

	/**
	 * La región por defecto será Colombia
	 */
	public static final long REGION_DEFECTO = 5L;
	
	
	/**
	 * La ciudad por defecto será Bogotá.
	 * Allí se mostrará los centros de vacunación 
	 */
	public static final long CIUDAD_DEFECTO = 11001L;
	
	/**
	 * Días por defecto que permitirá realizar el envio de la notificaciones 
	 */
	public static final String NOTIFICAR_DIAS_ANTICIPACION = "7";
	
	/**
	 * Llave de Google Maps necesaria para usar el API
	 */
	public static final String GOOGLE_MAPS_API_KEY = "AIzaSyBioI0V6qQMOQMdNzbbB6rmCAU4rWWpzmo";
	
	/**
	 *Tipo de rol que representa el paciente 
	 */
	public static final String ROL_PACIENTE = "PACIENTE";
	
	/**
	 *Tipo de rol que representa el paciente 
	 */
	public static final String ROL_ADMINISTRADOR = "ADMINISTRADOR";
	
	/**
	 * Latitud por defecto que toma el mapa correspondiente a la ciudad de Cali
	 */
	public static final String LATITUD_DEFECTO = "3.423901";
	
	/**
	 * Longitud por defecto que toma el mapa correspondiente a la ciudad de Cali
	 */
	public static final String LONGITUD_DEFECTO = "-76.522487";
	
	/**
	 * Meses del año en espaniol, se ingresa el mes que genera Date en ingles para devolver su nombre en espaniol
	 */
	public static final Map<String, String> TRADUCTOR_MESES = obtenerMeses();
	
	/**
	 * @retorno meses retorna el hashmap que devuelve meses en espaniol
	 */
	private static Map<String, String> obtenerMeses(){
        Map<String, String> meses = new HashMap<>();
		meses.put("Jan", "01");
		meses.put("Feb", "02");
		meses.put("Mar", "03");
		meses.put("Apr", "04");
		meses.put("May", "05");
		meses.put("Jun", "06");
		meses.put("Jul", "07");
		meses.put("Aug", "08");
		meses.put("Sep", "09");
		meses.put("Oct", "10");
		meses.put("Nov", "11");
		meses.put("Dec", "12");
        return Collections.unmodifiableMap(meses);
    }
	
	/**
	 * String que no tiene letras
	 */
	public static final String NO_TIENE_LETRAS = "N";

	/**
	 * Hace referencia al género masculino en la base de datos del sistema Vtrack
	 */
	public static final String LETRA_MASCULINO = "M";
	
	/**
	 * Hace referencia al género femenino en la base de datos del sistema Vtrack
	 */
	public static final String LETRA_FEMENINO = "F";
	
	/**
	 * Hace referencia al id del menú que corresponde con los permisos recibidos
	 */
	public static final Long ID_PERMISOS_RECIBIDOS = 5L;
}
