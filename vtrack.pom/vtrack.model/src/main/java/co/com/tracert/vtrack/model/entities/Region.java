package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the REGION database table.
 * 
 */
@Entity
@NamedQuery(name="Region.findAll", query="SELECT r FROM Region r")
public class Region implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="REGION_IDREGION_GENERATOR", sequenceName="REGION_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="REGION_IDREGION_GENERATOR")
	@Column(name="ID_REGION")
	private long idRegion;

	private String nombre;

	//bi-directional many-to-one association to Ciudad
	@OneToMany(mappedBy="region")
	private List<Ciudad> ciudads;

	//bi-directional many-to-one association to Pais
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_PAIS")
	private Pais pai;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="region")
	private List<Usuario> usuarios;

	public Region() {
	}

	public long getIdRegion() {
		return this.idRegion;
	}

	public void setIdRegion(long idRegion) {
		this.idRegion = idRegion;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Ciudad> getCiudads() {
		return this.ciudads;
	}

	public void setCiudads(List<Ciudad> ciudads) {
		this.ciudads = ciudads;
	}

	public Ciudad addCiudad(Ciudad ciudad) {
		getCiudads().add(ciudad);
		ciudad.setRegion(this);

		return ciudad;
	}

	public Ciudad removeCiudad(Ciudad ciudad) {
		getCiudads().remove(ciudad);
		ciudad.setRegion(null);

		return ciudad;
	}

	public Pais getPai() {
		return this.pai;
	}

	public void setPai(Pais pai) {
		this.pai = pai;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setRegion(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setRegion(null);

		return usuario;
	}

}