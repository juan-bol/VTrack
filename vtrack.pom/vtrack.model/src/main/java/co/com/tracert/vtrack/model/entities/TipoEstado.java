package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the TIPO_ESTADO database table.
 * 
 */
@Entity
@Table(name="TIPO_ESTADO")
@NamedQuery(name="TipoEstado.findAll", query="SELECT t FROM TipoEstado t")
public class TipoEstado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TIPO_ESTADO_IDTIPOESTADO_GENERATOR", sequenceName="TIPO_ESTADO_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_ESTADO_IDTIPOESTADO_GENERATOR")
	@Column(name="ID_TIPO_ESTADO")
	private long idTipoEstado;

	private String nombre;

	//bi-directional many-to-one association to Estado
	@OneToMany(mappedBy="tipoEstado")
	private List<Estado> estados;

	public TipoEstado() {
	}

	public long getIdTipoEstado() {
		return this.idTipoEstado;
	}

	public void setIdTipoEstado(long idTipoEstado) {
		this.idTipoEstado = idTipoEstado;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Estado> getEstados() {
		return this.estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public Estado addEstado(Estado estado) {
		getEstados().add(estado);
		estado.setTipoEstado(this);

		return estado;
	}

	public Estado removeEstado(Estado estado) {
		getEstados().remove(estado);
		estado.setTipoEstado(null);

		return estado;
	}

}