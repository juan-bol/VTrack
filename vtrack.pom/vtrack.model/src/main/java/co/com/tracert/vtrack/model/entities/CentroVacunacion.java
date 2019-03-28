package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the CENTRO_VACUNACION database table.
 * 
 */
@Entity
@Table(name="CENTRO_VACUNACION")
@NamedQuery(name="CentroVacunacion.findAll", query="SELECT c FROM CentroVacunacion c")
public class CentroVacunacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CENTRO_VACUNACION_IDCENTROVACUNACION_GENERATOR", sequenceName="CENTRO_VACUNACION_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CENTRO_VACUNACION_IDCENTROVACUNACION_GENERATOR")
	@Column(name="ID_CENTRO_VACUNACION")
	private long idCentroVacunacion;

	private String direccion;

	private String latitud;

	private String longitud;

	private String nombre;

	private String telefono;

	//bi-directional many-to-one association to Ciudad
	@ManyToOne
	@JoinColumn(name="ID_CIUDAD")
	private Ciudad ciudad;

	//bi-directional many-to-one association to Estado
	@ManyToOne
	@JoinColumn(name="ID_ESTADO")
	private Estado estado;

	public CentroVacunacion() {
	}

	public long getIdCentroVacunacion() {
		return this.idCentroVacunacion;
	}

	public void setIdCentroVacunacion(long idCentroVacunacion) {
		this.idCentroVacunacion = idCentroVacunacion;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getLatitud() {
		return this.latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	public String getLongitud() {
		return this.longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Ciudad getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public Estado getEstado() {
		return this.estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

}