package co.com.tracert.vtrack.model.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.entities.Dosis;
import co.com.tracert.vtrack.model.entities.DosisAplicada;

/**
 * Clase que facilita la gestión de las dosis en la interfaz de usuario
 */
public class ColumnasVacunaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 707070095362554374L;

	/**
	 * atributo que representa el nommbre de la vacuna
	 */
	private String nombreVacuna;

	/**
	 * fecha que representa la estimación de la fecha de aplicación
	 */
	private String fechaAplicacion;

	/**
	 * ocurrencia que se muestra en la vista de esquemas sugeridos
	 */
	private String ocurrenciaEsquemaSugeridos;

	/**
	 * atributo que representa la dosis a la que hace referencia el dto
	 */
	private Dosis dosis;

	/**
	 * atributo que representa la dosis aplicada a la que hace referencia el dto
	 */
	private DosisAplicada dosisAplicada;

	/**
	 * atributo que representa el boolean que determina si la dosis se puede aplicar
	 * o no
	 */
	private Boolean puedeAplicarse;

	/**
	 * atributo que representa el boolean que determina si la dosis se puede
	 * eliminar o no
	 */
	private Boolean puedeEliminarse;

	public ColumnasVacunaDTO(Dosis dosis, Date fechaNacimiento, String nombreVacuna, boolean tipoFecha) {
		this.dosis = dosis;
		this.nombreVacuna = nombreVacuna;
		this.puedeAplicarse = false;
		this.puedeEliminarse = false;
		estimarFechaAplicacion(fechaNacimiento);
		determinarOcurrencia(tipoFecha);
	}

	public ColumnasVacunaDTO(Dosis dosis, Date fechaNacimiento, String nombreVacuna, DosisAplicada dosisAplicada,
			DosisAplicada primeraDosisAplicada, boolean tipoFecha) {
		this.dosis = dosis;
		this.nombreVacuna = nombreVacuna;
		this.dosisAplicada = dosisAplicada;
		this.puedeAplicarse = false;
		this.puedeEliminarse = false;
		if (tipoFecha) {
			estimarFechaAplicacionPorFechaNacimiento(fechaNacimiento);
		} else {
			if (primeraDosisAplicada != null
					&& primeraDosisAplicada.getEstado().getEstado().equals(ConstantesVtrack.ESTADO_ACTIVO)) {
				estimarFechaAplicacion(primeraDosisAplicada.getFechaAplicacion());
			} else {
				estimarFechaAplicacion(null);
			}

		}

	}

	/**
	 * @descripcion Método que estima la fecha de aplicacion de la dosis
	 * @author Ana Arango
	 * @fecha 3/11/2018
	 * @param fechaNacimiento fecha de nacimiento del usuario
	 */
	public void estimarFechaAplicacionPorFechaNacimiento(Date fechaNacimiento) {
		if (dosisAplicada != null && dosisAplicada.getEstado().getEstado().equals(ConstantesVtrack.ESTADO_ACTIVO)) {

			SimpleDateFormat smp = new SimpleDateFormat("dd-MMM-yyyy", new Locale("es", "ES"));
			Calendar cal = Calendar.getInstance();
			cal.setTime(dosisAplicada.getFechaAplicacion());
			fechaAplicacion = smp.format(cal.getTime());

		} else {

			SimpleDateFormat smp = new SimpleDateFormat("dd-MMM-yyyy", new Locale("es", "ES"));
			Calendar cal = Calendar.getInstance();
			if (fechaNacimiento != null) {
				cal.setTime(fechaNacimiento);

				cal.add(Calendar.DATE, dosis.getDiaAplicacion().intValue());

				fechaAplicacion = smp.format(cal.getTime());
			} else {
				cal.setTime(new Date());
				cal.add(Calendar.DATE, dosis.getDiaAplicacion().intValue());
				fechaAplicacion = smp.format(cal.getTime());

			}

		}

	}

	/**
	 * @descripcion Método que estima la fecha de aplicacion de la dosis
	 * @author Ana Arango
	 * @fecha 3/11/2018
	 * @param fechaNacimiento fecha de nacimiento del usuario
	 */
	public void estimarFechaAplicacion(Date fechaPrimeraAplicacion) {
		if (dosisAplicada != null && dosisAplicada.getEstado().getEstado().equals(ConstantesVtrack.ESTADO_ACTIVO)) {

			SimpleDateFormat smp = new SimpleDateFormat("dd-MMM-yyyy", new Locale("es", "ES"));
			Calendar cal = Calendar.getInstance();
			cal.setTime(dosisAplicada.getFechaAplicacion());
			fechaAplicacion = smp.format(cal.getTime());

		} else {

			SimpleDateFormat smp = new SimpleDateFormat("dd-MMM-yyyy", new Locale("es", "ES"));
			Calendar cal = Calendar.getInstance();
			if (fechaPrimeraAplicacion != null) {
				cal.setTime(fechaPrimeraAplicacion);
				if (dosis.getDiaAplicacion().intValue() == -1) {
					cal.add(Calendar.DATE, 0);
				} else {
					cal.add(Calendar.DATE, dosis.getDiaAplicacion().intValue());
				}
				fechaAplicacion = smp.format(cal.getTime());
			} else {

				if (dosis.getDiaAplicacion().intValue() == -1) {
					fechaAplicacion = "Hoy";
				} else if (dosis.getDiaAplicacion().intValue() == 1) {
					fechaAplicacion = "Pasado " + dosis.getDiaAplicacion().intValue() / 30 + " mes";
				} else {

					fechaAplicacion = "Pasados " + dosis.getDiaAplicacion().intValue() / 30 + " meses";
				}

			}

		}

	}

	/**
	 * @descripcion Método que estima la fecha de aplicacion de la dosis
	 * @author Ana Arango
	 * @fecha 12/12/2018
	 * @param tipoFecha boolean que determina si la fecha se estima de acuerdo a la fecha de nscimiento o si de acuerdo 
	 * a la fecha de aplicación de la primera dosis.
	 */
	public void determinarOcurrencia(boolean tipoFecha) {
		// si se estima de acuerdo a la fecha de nacimiento
		if (tipoFecha) {

			if (dosis.getDiaAplicacion().intValue() / 30 == 1) {
				ocurrenciaEsquemaSugeridos = "1 mes";
			}else if(dosis.getDiaAplicacion().intValue() / 30==0) {
				if(dosis.getDiaAplicacion().intValue()==1) {
					ocurrenciaEsquemaSugeridos = "1 día";
				}else {
					ocurrenciaEsquemaSugeridos = dosis.getDiaAplicacion().intValue()  + " días";
				}
				
			} else {
				ocurrenciaEsquemaSugeridos = dosis.getDiaAplicacion().intValue() / 30 + " meses";
			}

			// si se estima de acuerdo a la primera dosis
		} else {

			if (dosis.getDiaAplicacion().intValue() == -1) {
				ocurrenciaEsquemaSugeridos = "Hoy";

			}
			else if (dosis.getDiaAplicacion().intValue() / 30 == 1) {
				ocurrenciaEsquemaSugeridos = "Pasado 1 mes";
			}else if(dosis.getDiaAplicacion().intValue() / 30 == 0) {
				
				if(dosis.getDiaAplicacion().intValue()==1) {
					ocurrenciaEsquemaSugeridos = "Pasado 1 día";
				}else {
					ocurrenciaEsquemaSugeridos = "Pasados " + dosis.getDiaAplicacion().intValue()  + " días";
				}
				
			}else {
				ocurrenciaEsquemaSugeridos = "Pasados " + dosis.getDiaAplicacion().intValue() / 30 + " meses";
			}

		}

	}

	public String getNombreVacuna() {
		return nombreVacuna;
	}

	public void setNombreVacuna(String nombreVacuna) {
		this.nombreVacuna = nombreVacuna;
	}

	public String getFechaAplicacion() {
		return fechaAplicacion;
	}

	public void setFechaAplicacion(String fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
	}

	public Dosis getDosis() {
		return dosis;
	}

	public void setDosis(Dosis dosis) {
		this.dosis = dosis;
	}

	public DosisAplicada getDosisAplicada() {
		return dosisAplicada;
	}

	public void setDosisAplicada(DosisAplicada dosisAplicada) {
		this.dosisAplicada = dosisAplicada;
	}

	public Boolean getPuedeAplicarse() {
		return puedeAplicarse;
	}

	public void setPuedeAplicarse(Boolean puedeAplicarse) {
		this.puedeAplicarse = puedeAplicarse;
	}

	public Boolean getPuedeEliminarse() {
		return puedeEliminarse;
	}

	public void setPuedeEliminarse(Boolean puedeEliminarse) {
		this.puedeEliminarse = puedeEliminarse;
	}

	public String getOcurrenciaEsquemaSugeridos() {
		return ocurrenciaEsquemaSugeridos;
	}

	public void setOcurrenciaEsquemaSugeridos(String ocurrenciaEsquemaSugeridos) {
		this.ocurrenciaEsquemaSugeridos = ocurrenciaEsquemaSugeridos;
	}

}
