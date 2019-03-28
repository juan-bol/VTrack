package co.com.tracert.vtrack.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.entities.Dosis;
import co.com.tracert.vtrack.model.entities.DosisAplicada;
import co.com.tracert.vtrack.model.entities.Esquema;
import co.com.tracert.vtrack.model.entities.Vacuna;
import co.com.tracert.vtrack.model.entities.VacunaEsquema;


/**
 * Clase que facilita la gestión de los esquemas en la vista
 * 
 */
public class EsquemaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -375413233250057817L;
	
	/**
	 * esquema al que se refiere el DTO
	 */
	private Esquema esquema;
	
	/**
	 * Lista de las dosis que pertenecen al esquema
	 */
	private List<ColumnasVacunaDTO[]> columnas;
	
	/**
	 * Lista de las cabeceras de refuerzo de este esquema
	 */
	private List<CabeceraDTO> cabecerasRefuerzos;
	
	/**
	 * Lista de las cabeceras de dosis de este esquema
	 */
	private List<CabeceraDTO> cabecerasDosis;
	
	/**
	 * Lista de las cabeceras del esquema
	 */
	private List<CabeceraDTO> cabeceras;
	
	/**
	 * identificador utilizado para el collapse
	 */
	private String identificador;


	/**
	 * @descripcion Constructor del esquema DTO que se utiliza para la visualización de los esquemas sugeridos
	 * @author Ana Arango
 	 * @fecha 3/11/2018
	 * @param esquema al que se refiere el DTO
	 * @param fechaNacimiento del usuario
	 */
	public EsquemaDTO(Esquema esquema, Date fechaNacimiento) {
		this.esquema = esquema;
		crearIdentificador();
		columnas = new ArrayList<ColumnasVacunaDTO[]>();

		crearCabecerasColumnas(esquema.getVacunaEsquemas());
		for (VacunaEsquema vacunaEsquema : esquema.getVacunaEsquemas()) {
			int tempNumDosis = 0;
			int tempNumRefuerzos = 0;
			ColumnasVacunaDTO[] vacunaFilaDosis = new ColumnasVacunaDTO[cabecerasDosis.size()];
			ColumnasVacunaDTO[] vacunaFilaRefuerzos = new ColumnasVacunaDTO[cabecerasRefuerzos.size()];
			String nombreVacuna = vacunaEsquema.getVacuna().getNombre();
			boolean tipoFecha=true;
			if(vacunaEsquema.getDosis().get(0).getDiaAplicacion().intValue()==-1) {
				tipoFecha=false;
			}

			for (Dosis dosis : vacunaEsquema.getDosis()) {

				
				
				ColumnasVacunaDTO columna = new ColumnasVacunaDTO(dosis, fechaNacimiento, nombreVacuna,tipoFecha);

				if (dosis.getTipoDosis().equalsIgnoreCase("D")) {
					vacunaFilaDosis[tempNumDosis]= columna;
					tempNumDosis++;
				} else {
					vacunaFilaRefuerzos[tempNumRefuerzos] = columna;
					tempNumRefuerzos++;
				}

			}

			ColumnasVacunaDTO[] vacunaFila = new ColumnasVacunaDTO[vacunaFilaDosis.length+vacunaFilaRefuerzos.length];
			for (int i = 0; i < vacunaFilaDosis.length; i++) {
				vacunaFila[i]=vacunaFilaDosis[i];
			}
			for (int i = vacunaFilaDosis.length; i < vacunaFila.length; i++) {
				vacunaFila[i]=vacunaFilaRefuerzos[i-vacunaFilaDosis.length];
			}
			columnas.add(vacunaFila);
		}
	}

	/**
	 * 
	 * @descripcion Constructor del esquema DTO que se utiliza para la visualización de los esquemas sugeridos
	 * @author Ana Arango
 	 * @fecha 3/11/2018
	 * @param esquema al que se refiere el DTO
	 * @param fechaNacimiento del usuario
	 * @param vacunasOcultas vacunas ocultas del usuario
	 * @param dosisAplicadas dosis que ya se ha aplicado el usuario
	 */
	public EsquemaDTO(Esquema esquema, Date fechaNacimiento, List<Vacuna> vacunasOcultas,
			List<DosisAplicada> dosisAplicadas) {
		this.esquema = esquema;
		crearIdentificador();
		columnas = new ArrayList<ColumnasVacunaDTO[]>();
		List<VacunaEsquema> vacunasEsquemasCabeceras = darVacunasAMostrar(vacunasOcultas);
		crearCabecerasColumnas(vacunasEsquemasCabeceras);
		for (VacunaEsquema vacunaEsquema : vacunasEsquemasCabeceras) {
			int tempNumDosis = 0;
			int tempNumRefuerzos = 0;
			ColumnasVacunaDTO[] vacunaFilaDosis = new ColumnasVacunaDTO[cabecerasDosis.size()];
			ColumnasVacunaDTO[] vacunaFilaRefuerzos = new ColumnasVacunaDTO[cabecerasRefuerzos.size()];
			String nombreVacuna = vacunaEsquema.getVacuna().getNombre();
			Boolean primeraDosisNull=false;
			ColumnasVacunaDTO ultimaActiva=null;
			DosisAplicada primeraDosis=darDosisAplicadaDeDosis(vacunaEsquema.getDosis().get(0), dosisAplicadas);
			for (Dosis dosis : vacunaEsquema.getDosis()) {

				boolean tipoFecha=true;
				DosisAplicada dosisAplicada = darDosisAplicadaDeDosis(dosis, dosisAplicadas);
				if(vacunaEsquema.getDosis().get(0).getDiaAplicacion().intValue()==-1) {
					tipoFecha=false;
				}
				ColumnasVacunaDTO columna = new ColumnasVacunaDTO(dosis, fechaNacimiento, nombreVacuna, dosisAplicada,primeraDosis,tipoFecha);
				if((!primeraDosisNull&&(dosisAplicada==null||dosisAplicada.getEstado().getEstado().equals(ConstantesVtrack.ESTADO_INACTIVO)))) {
					primeraDosisNull=true;
					columna.setPuedeAplicarse(true);
				}else if((dosisAplicada!=null&&dosisAplicada.getEstado().getEstado().equals(ConstantesVtrack.ESTADO_ACTIVO))) {
					columna.setPuedeAplicarse(true);
					ultimaActiva=columna;
				}
				
				if (dosis.getTipoDosis().equalsIgnoreCase("D")) {
					vacunaFilaDosis[tempNumDosis]= columna;
					tempNumDosis++;
				} else {
					vacunaFilaRefuerzos[tempNumRefuerzos] = columna;
					tempNumRefuerzos++;
				}

			}

			if(ultimaActiva!=null) {
				ultimaActiva.setPuedeEliminarse(true);
			}

			ColumnasVacunaDTO[] vacunaFila = new ColumnasVacunaDTO[vacunaFilaDosis.length+vacunaFilaRefuerzos.length];
			for (int i = 0; i < vacunaFilaDosis.length; i++) {
				vacunaFila[i]=vacunaFilaDosis[i];
			}
			for (int i = vacunaFilaDosis.length; i < vacunaFila.length; i++) {
				vacunaFila[i]=vacunaFilaRefuerzos[i-vacunaFilaDosis.length];
			}
			columnas.add(vacunaFila);
		}
	}

	public Esquema getEsquema() {
		return esquema;
	}

	public void setEsquema(Esquema esquema) {
		this.esquema = esquema;
	}

	public List<ColumnasVacunaDTO[]> getColumnas() {
		return columnas;
	}

	public void setColumnas(List<ColumnasVacunaDTO[]> columnas) {
		this.columnas = columnas;
	}
	
	/**
	 * @descripcion Crea las cabeceras del esquema que se requieren mostrar en la vista
	 * @author Ana Arango
 	 * @fecha 3/11/2018
	 * @param lista de vaunas que pertenecen al esquema del esquema Dto 
	 */
	public void crearCabecerasColumnas(List<VacunaEsquema> vacunasEsquemas) {
		cabecerasDosis = new ArrayList<CabeceraDTO>();
		cabecerasRefuerzos = new ArrayList<CabeceraDTO>();
		int maxNumRefuerzos = 0;
		int maxNumDosis = 0;
		for (VacunaEsquema vacunaEsquema : vacunasEsquemas) {
			int tempNumDosis = 0;
			int tempNumRefuerzos = 0;
			for (Dosis dosis : vacunaEsquema.getDosis()) {
				if (dosis.getTipoDosis().equalsIgnoreCase("D")) {
					tempNumDosis++;
				} else {
					tempNumRefuerzos++;
				}
			}

			if (maxNumDosis < tempNumDosis) {
				maxNumDosis = tempNumDosis;
			}
			if (maxNumRefuerzos < tempNumRefuerzos) {
				maxNumRefuerzos = tempNumRefuerzos;
			}

		}
		int tmp = 0;

		for (int i = 0; i < maxNumDosis; i++) {

			cabecerasDosis.add(new CabeceraDTO(("Dosis " + (i + 1)), tmp));
			tmp++;

		}

		for (int i = 0; i < maxNumRefuerzos; i++) {
			cabecerasRefuerzos.add(new CabeceraDTO("Refuerzos " + (i + 1), tmp));
			tmp++;

		}

		cabeceras = new ArrayList<CabeceraDTO>();
		cabeceras.addAll(cabecerasDosis);
		cabeceras.addAll(cabecerasRefuerzos);
	}
	
	/**
	 * @descripcion Crea el identificador de la esquema que se utilizar para gestionar las colisiones
	 * @author Ana Arango
 	 * @fecha 3/11/2018
	 */
	private void crearIdentificador() {
		String[] nombreLista = esquema.getNombre().split(" ");
		String id = "";
		for (String string : nombreLista) {
			id += string;
		}
		identificador = id;

	}

	/**
	 * @descripcion Método que retorna, si existe, la dosis aplicada de la dosis del esquema
	 * @author Ana Arango
 	 * @fecha 3/11/2018
	 * @param dosis dosis del esquema de vacunación
	 * @param dosisAplicadas lista de dosis que pertenecen al usuario del esquema
	 * @return dosisAplicada la dosisAplicada que pertenece al usuario y a la dosis del esquema
	 */
	public DosisAplicada darDosisAplicadaDeDosis(Dosis dosis, List<DosisAplicada> dosisAplicadas) {
		DosisAplicada dosisAplicadaEncontrada = null;

		for (int j = 0; j < dosisAplicadas.size(); j++) {
			if (dosis.getIdDosis() == dosisAplicadas.get(j).getDosi().getIdDosis()) {
				dosisAplicadaEncontrada = dosisAplicadas.get(j);
			}

		}
		return dosisAplicadaEncontrada;
	}

	
	/**
	 * @descripcion Método que verifica si una vacuna del esquema es una vacuna oculta
	 * @author Ana Arango
 	 * @fecha 3/11/2018
	 * @param vacunaEsquema vacuna del esquema que se va a verificar
	 * @param vacunasOcultas lista de vacunas ocultas del usuario
	 * @return boolean que es false si la vacuna del esquema no es una vacuna oculta, y true, en caso contrario
	 */
	public boolean perteneceAVacunaOculta(VacunaEsquema vacunaEsquema, List<Vacuna> vacunasOcultas) {

		boolean pertenece = false;

		for (int i = 0; i < vacunasOcultas.size() && !pertenece; i++) {
			if (vacunasOcultas.get(i).getIdVacuna() == vacunaEsquema.getVacuna().getIdVacuna()) {
				pertenece = true;
			}
		}

		return pertenece;

	}

	
	/**
	 * @descripcion Método que retorna la lista de vacunas del esquema que se debe mostrar
	 * @author Ana Arango
 	 * @fecha 3/11/2018
	 * @param vacunasOcultas lista de vacunas ocultas del usuario
	 * @return List<VacunaEsquema> lista de vacunas del esquema que se deben mostrar
	 */
	public List<VacunaEsquema> darVacunasAMostrar(List<Vacuna> vacunasOcultas) {
		List<VacunaEsquema> vacunasAMostrar = new ArrayList<VacunaEsquema>();
		List<VacunaEsquema> vacunasEsquemas = esquema.getVacunaEsquemas();
		for (int i = 0; i < vacunasEsquemas.size(); i++) {
			if (!perteneceAVacunaOculta(vacunasEsquemas.get(i), vacunasOcultas)) {
				vacunasAMostrar.add(vacunasEsquemas.get(i));
			}
		}
		return vacunasAMostrar;
	}

	public List<CabeceraDTO> getCabecerasRefuerzos() {
		return cabecerasRefuerzos;
	}

	public void setCabecerasRefuerzos(List<CabeceraDTO> cabecerasRefuerzos) {
		this.cabecerasRefuerzos = cabecerasRefuerzos;
	}

	public List<CabeceraDTO> getCabecerasDosis() {
		return cabecerasDosis;
	}

	public void setCabecerasDosis(List<CabeceraDTO> cabecerasDosis) {
		this.cabecerasDosis = cabecerasDosis;
	}

	public List<CabeceraDTO> getCabeceras() {
		return cabeceras;
	}

	public void setCabeceras(List<CabeceraDTO> cabeceras) {
		this.cabeceras = cabeceras;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

}
