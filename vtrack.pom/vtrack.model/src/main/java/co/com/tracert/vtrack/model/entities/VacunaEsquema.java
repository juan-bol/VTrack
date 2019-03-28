package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the VACUNA_ESQUEMA database table.
 * 
 */
@Entity
@Table(name="VACUNA_ESQUEMA")
@NamedQuery(name="VacunaEsquema.findAll", query="SELECT v FROM VacunaEsquema v")
public class VacunaEsquema implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="VACUNA_ESQUEMA_IDVACUNAESQ_GENERATOR", sequenceName="VACUNA_ESQUEMA_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VACUNA_ESQUEMA_IDVACUNAESQ_GENERATOR")
	@Column(name="ID_VACUNA_ESQ")
	private long idVacunaEsq;

	//bi-directional many-to-one association to Dosis
	@OneToMany(mappedBy="vacunaEsquema",fetch=FetchType.EAGER)
	private List<Dosis> dosis;

	//bi-directional many-to-one association to Esquema
	@ManyToOne
	@JoinColumn(name="ID_ESQUEMA")
	private Esquema esquema;

	//bi-directional many-to-one association to Estado
	@ManyToOne
	@JoinColumn(name="ID_ESTADO")
	private Estado estado;

	//bi-directional many-to-one association to Vacuna
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_VACUNA")
	private Vacuna vacuna;

	public VacunaEsquema() {
	}

	public long getIdVacunaEsq() {
		return this.idVacunaEsq;
	}

	public void setIdVacunaEsq(long idVacunaEsq) {
		this.idVacunaEsq = idVacunaEsq;
	}

	public List<Dosis> getDosis() {
		return this.dosis;
	}

	public void setDosis(List<Dosis> dosis) {
		this.dosis = dosis;
	}

	public Dosis addDosi(Dosis dosi) {
		getDosis().add(dosi);
		dosi.setVacunaEsquema(this);

		return dosi;
	}

	public Dosis removeDosi(Dosis dosi) {
		getDosis().remove(dosi);
		dosi.setVacunaEsquema(null);

		return dosi;
	}

	public Esquema getEsquema() {
		return this.esquema;
	}

	public void setEsquema(Esquema esquema) {
		this.esquema = esquema;
	}

	public Estado getEstado() {
		return this.estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Vacuna getVacuna() {
		return this.vacuna;
	}

	public void setVacuna(Vacuna vacuna) {
		this.vacuna = vacuna;
	}

}