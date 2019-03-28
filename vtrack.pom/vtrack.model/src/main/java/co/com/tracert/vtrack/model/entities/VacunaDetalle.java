package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the VACUNA_DETALLE database table.
 * 
 */
@Entity
@Table(name="VACUNA_DETALLE")
@NamedQuery(name="VacunaDetalle.findAll", query="SELECT v FROM VacunaDetalle v")
public class VacunaDetalle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="VACUNA_DETALLE_IDVACUNA_GENERATOR", sequenceName="VACUNA_DETALLE_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VACUNA_DETALLE_IDVACUNA_GENERATOR")
	@Column(name="ID_VACUNA")
	private long idVacuna;

	@Lob
	@Column(name="INFORMACION_GENERAL")
	private String informacionGeneral;

	//bi-directional one-to-one association to Vacuna
	@OneToOne
	@JoinColumn(name="ID_VACUNA")
	private Vacuna vacuna;

	public VacunaDetalle() {
	}

	public long getIdVacuna() {
		return this.idVacuna;
	}

	public void setIdVacuna(long idVacuna) {
		this.idVacuna = idVacuna;
	}

	public String getInformacionGeneral() {
		return this.informacionGeneral;
	}

	public void setInformacionGeneral(String informacionGeneral) {
		this.informacionGeneral = informacionGeneral;
	}

	public Vacuna getVacuna() {
		return this.vacuna;
	}

	public void setVacuna(Vacuna vacuna) {
		this.vacuna = vacuna;
	}

}