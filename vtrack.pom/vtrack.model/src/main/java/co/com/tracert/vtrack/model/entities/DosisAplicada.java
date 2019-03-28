package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the DOSIS_APLICADA database table.
 * 
 */
@Entity
@Table(name="DOSIS_APLICADA")
@NamedQuery(name="DosisAplicada.findAll", query="SELECT d FROM DosisAplicada d")
public class DosisAplicada implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DOSIS_APLICADA_IDDOSISAPLICADA_GENERATOR", sequenceName="DOSIS_APLICADA_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DOSIS_APLICADA_IDDOSISAPLICADA_GENERATOR")
	@Column(name="ID_DOSIS_APLICADA")
	private long idDosisAplicada;

//	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_APLICACION")
	private Date fechaAplicacion;

	//bi-directional many-to-one association to Dosis
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_DOSIS")
	private Dosis dosi;

	//bi-directional many-to-one association to Estado
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_ESTADO")
	private Estado estado;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="ID_USUARIO")
	private Usuario usuario;

	public DosisAplicada() {
	}

	public long getIdDosisAplicada() {
		return this.idDosisAplicada;
	}

	public void setIdDosisAplicada(long idDosisAplicada) {
		this.idDosisAplicada = idDosisAplicada;
	}

	public Date getFechaAplicacion() {
		return this.fechaAplicacion;
	}

	public void setFechaAplicacion(Date fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
	}

	public Dosis getDosi() {
		return this.dosi;
	}

	public void setDosi(Dosis dosi) {
		this.dosi = dosi;
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

}