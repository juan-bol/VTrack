package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the VACUNA_OCULTA database table.
 * 
 */
@Entity
@Table(name="VACUNA_OCULTA")
@NamedQuery(name="VacunaOculta.findAll", query="SELECT v FROM VacunaOculta v")
public class VacunaOculta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="VACUNA_OCULTA_IDVACUNAOCULTA_GENERATOR", sequenceName="VACUNA_OCULTA_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VACUNA_OCULTA_IDVACUNAOCULTA_GENERATOR")
	@Column(name="ID_VACUNA_OCULTA")
	private long idVacunaOculta;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="ID_USUARIO")
	private Usuario usuario;

	//bi-directional many-to-one association to Vacuna
	@ManyToOne
	@JoinColumn(name="ID_VACUNA")
	private Vacuna vacuna;

	public VacunaOculta() {
	}

	public long getIdVacunaOculta() {
		return this.idVacunaOculta;
	}

	public void setIdVacunaOculta(long idVacunaOculta) {
		this.idVacunaOculta = idVacunaOculta;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Vacuna getVacuna() {
		return this.vacuna;
	}

	public void setVacuna(Vacuna vacuna) {
		this.vacuna = vacuna;
	}

}