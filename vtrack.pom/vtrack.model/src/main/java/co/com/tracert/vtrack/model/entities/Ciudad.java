package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the CIUDAD database table.
 * 
 */
@Entity
@NamedQuery(name="Ciudad.findAll", query="SELECT c FROM Ciudad c")
public class Ciudad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CIUDAD_IDCIUDAD_GENERATOR", sequenceName="CIUDAD_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CIUDAD_IDCIUDAD_GENERATOR")
	@Column(name="ID_CIUDAD")
	private long idCiudad;

	private String latitud;

	private String longitud;

	private String nombre;

	//bi-directional many-to-one association to CentroVacunacion
	@OneToMany(mappedBy="ciudad")
	private List<CentroVacunacion> centroVacunacions;

	//bi-directional many-to-one association to Region
	@ManyToOne
	@JoinColumn(name="ID_REGION")
	private Region region;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="ciudad")
	private List<Usuario> usuarios;

	public Ciudad() {
	}

	public long getIdCiudad() {
		return this.idCiudad;
	}

	public void setIdCiudad(long idCiudad) {
		this.idCiudad = idCiudad;
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

	public List<CentroVacunacion> getCentroVacunacions() {
		return this.centroVacunacions;
	}

	public void setCentroVacunacions(List<CentroVacunacion> centroVacunacions) {
		this.centroVacunacions = centroVacunacions;
	}

	public CentroVacunacion addCentroVacunacion(CentroVacunacion centroVacunacion) {
		getCentroVacunacions().add(centroVacunacion);
		centroVacunacion.setCiudad(this);

		return centroVacunacion;
	}

	public CentroVacunacion removeCentroVacunacion(CentroVacunacion centroVacunacion) {
		getCentroVacunacions().remove(centroVacunacion);
		centroVacunacion.setCiudad(null);

		return centroVacunacion;
	}

	public Region getRegion() {
		return this.region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setCiudad(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setCiudad(null);

		return usuario;
	}

}