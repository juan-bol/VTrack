package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the DOSIS database table.
 * 
 */
@Entity
@NamedQuery(name="Dosis.findAll", query="SELECT d FROM Dosis d")
public class Dosis implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DOSIS_IDDOSIS_GENERATOR", sequenceName="DOSIS_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DOSIS_IDDOSIS_GENERATOR")
	@Column(name="ID_DOSIS")
	private long idDosis;

	@Column(name="DIA_APLICACION")
	private BigDecimal diaAplicacion;

	@Column(name="TIPO_DOSIS")
	private String tipoDosis;

	//bi-directional many-to-one association to VacunaEsquema
	@ManyToOne
	@JoinColumn(name="ID_VACUNA_ESQ")
	private VacunaEsquema vacunaEsquema;

	//bi-directional many-to-one association to DosisAplicada
	@OneToMany(mappedBy="dosi")
	private List<DosisAplicada> dosisAplicadas;

	public Dosis() {
	}

	public long getIdDosis() {
		return this.idDosis;
	}

	public void setIdDosis(long idDosis) {
		this.idDosis = idDosis;
	}

	public BigDecimal getDiaAplicacion() {
		return this.diaAplicacion;
	}

	public void setDiaAplicacion(BigDecimal diaAplicacion) {
		this.diaAplicacion = diaAplicacion;
	}

	public String getTipoDosis() {
		return this.tipoDosis;
	}

	public void setTipoDosis(String tipoDosis) {
		this.tipoDosis = tipoDosis;
	}

	public VacunaEsquema getVacunaEsquema() {
		return this.vacunaEsquema;
	}

	public void setVacunaEsquema(VacunaEsquema vacunaEsquema) {
		this.vacunaEsquema = vacunaEsquema;
	}

	public List<DosisAplicada> getDosisAplicadas() {
		return this.dosisAplicadas;
	}

	public void setDosisAplicadas(List<DosisAplicada> dosisAplicadas) {
		this.dosisAplicadas = dosisAplicadas;
	}

	public DosisAplicada addDosisAplicada(DosisAplicada dosisAplicada) {
		getDosisAplicadas().add(dosisAplicada);
		dosisAplicada.setDosi(this);

		return dosisAplicada;
	}

	public DosisAplicada removeDosisAplicada(DosisAplicada dosisAplicada) {
		getDosisAplicadas().remove(dosisAplicada);
		dosisAplicada.setDosi(null);

		return dosisAplicada;
	}

}