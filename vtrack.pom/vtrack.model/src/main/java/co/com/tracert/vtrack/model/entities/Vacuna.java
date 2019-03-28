package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the VACUNA database table.
 * 
 */
@Entity
@NamedQuery(name="Vacuna.findAll", query="SELECT v FROM Vacuna v")
public class Vacuna implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="VACUNA_IDVACUNA_GENERATOR", sequenceName="VACUNA_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VACUNA_IDVACUNA_GENERATOR")
	@Column(name="ID_VACUNA")
	private long idVacuna;

	private String nombre;

	//bi-directional many-to-one association to Estado
	@ManyToOne
	@JoinColumn(name="ID_ESTADO")
	private Estado estado;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="ID_USUARIO")
	private Usuario usuario;

	//bi-directional one-to-one association to VacunaDetalle
	@OneToOne(mappedBy="vacuna")
	private VacunaDetalle vacunaDetalle;

	//bi-directional many-to-one association to VacunaEsquema
	@OneToMany(mappedBy="vacuna")
	private List<VacunaEsquema> vacunaEsquemas;

	//bi-directional many-to-one association to VacunaOculta
	@OneToMany(mappedBy="vacuna")
	private List<VacunaOculta> vacunaOcultas;

	public Vacuna() {
	}

	public long getIdVacuna() {
		return this.idVacuna;
	}

	public void setIdVacuna(long idVacuna) {
		this.idVacuna = idVacuna;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Estado getEstado() {
		return this.estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public VacunaDetalle getVacunaDetalle() {
		return this.vacunaDetalle;
	}

	public void setVacunaDetalle(VacunaDetalle vacunaDetalle) {
		this.vacunaDetalle = vacunaDetalle;
	}

	public List<VacunaEsquema> getVacunaEsquemas() {
		return this.vacunaEsquemas;
	}

	public void setVacunaEsquemas(List<VacunaEsquema> vacunaEsquemas) {
		this.vacunaEsquemas = vacunaEsquemas;
	}

	public VacunaEsquema addVacunaEsquema(VacunaEsquema vacunaEsquema) {
		getVacunaEsquemas().add(vacunaEsquema);
		vacunaEsquema.setVacuna(this);

		return vacunaEsquema;
	}

	public VacunaEsquema removeVacunaEsquema(VacunaEsquema vacunaEsquema) {
		getVacunaEsquemas().remove(vacunaEsquema);
		vacunaEsquema.setVacuna(null);

		return vacunaEsquema;
	}

	public List<VacunaOculta> getVacunaOcultas() {
		return this.vacunaOcultas;
	}

	public void setVacunaOcultas(List<VacunaOculta> vacunaOcultas) {
		this.vacunaOcultas = vacunaOcultas;
	}

	public VacunaOculta addVacunaOculta(VacunaOculta vacunaOculta) {
		getVacunaOcultas().add(vacunaOculta);
		vacunaOculta.setVacuna(this);

		return vacunaOculta;
	}

	public VacunaOculta removeVacunaOculta(VacunaOculta vacunaOculta) {
		getVacunaOcultas().remove(vacunaOculta);
		vacunaOculta.setVacuna(null);

		return vacunaOculta;
	}

}