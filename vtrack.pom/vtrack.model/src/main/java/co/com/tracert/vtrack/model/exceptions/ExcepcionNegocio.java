package co.com.tracert.vtrack.model.exceptions;

public class ExcepcionNegocio extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6821021536786155039L;

	/**@Descripcion Excepcion que se genera cuando no se cumplen las validaciones de la logica
	 * @author Diego Lamus
	 * @param mensaje mensaja indicando por que se lanza la excepcion
	 */
	public ExcepcionNegocio(String mensaje) {
		super(mensaje);
	}
}
