package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the PAIS database table.
 * 
 */
@Entity
@NamedQuery(name="Pais.findAll", query="SELECT p FROM Pais p")
public class Pais implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PAIS_IDPAIS_GENERATOR", sequenceName="PAIS_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PAIS_IDPAIS_GENERATOR")
	@Column(name="ID_PAIS")
	private long idPais;

	@Column(name="DISTRIBUCION_REGIONAL")
	private String distribucionRegional;

	private String nombre;

	//bi-directional many-to-one association to Esquema
	@OneToMany(mappedBy="pai")
	private List<Esquema> esquemas;

	//bi-directional many-to-one association to Region
	@OneToMany(mappedBy="pai")
	private List<Region> regions;

	//bi-directional many-to-one association to TipoDocumentoPais
	@OneToMany(mappedBy="pai")
	private List<TipoDocumentoPais> tipoDocumentoPais;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="pai")
	private List<Usuario> usuarios;

	public Pais() {
	}

	public long getIdPais() {
		return this.idPais;
	}

	public void setIdPais(long idPais) {
		this.idPais = idPais;
	}

	public String getDistribucionRegional() {
		return this.distribucionRegional;
	}

	public void setDistribucionRegional(String distribucionRegional) {
		this.distribucionRegional = distribucionRegional;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Esquema> getEsquemas() {
		return this.esquemas;
	}

	public void setEsquemas(List<Esquema> esquemas) {
		this.esquemas = esquemas;
	}

	public Esquema addEsquema(Esquema esquema) {
		getEsquemas().add(esquema);
		esquema.setPai(this);

		return esquema;
	}

	public Esquema removeEsquema(Esquema esquema) {
		getEsquemas().remove(esquema);
		esquema.setPai(null);

		return esquema;
	}

	public List<Region> getRegions() {
		return this.regions;
	}

	public void setRegions(List<Region> regions) {
		this.regions = regions;
	}

	public Region addRegion(Region region) {
		getRegions().add(region);
		region.setPai(this);

		return region;
	}

	public Region removeRegion(Region region) {
		getRegions().remove(region);
		region.setPai(null);

		return region;
	}

	public List<TipoDocumentoPais> getTipoDocumentoPais() {
		return this.tipoDocumentoPais;
	}

	public void setTipoDocumentoPais(List<TipoDocumentoPais> tipoDocumentoPais) {
		this.tipoDocumentoPais = tipoDocumentoPais;
	}

	public TipoDocumentoPais addTipoDocumentoPai(TipoDocumentoPais tipoDocumentoPai) {
		getTipoDocumentoPais().add(tipoDocumentoPai);
		tipoDocumentoPai.setPai(this);

		return tipoDocumentoPai;
	}

	public TipoDocumentoPais removeTipoDocumentoPai(TipoDocumentoPais tipoDocumentoPai) {
		getTipoDocumentoPais().remove(tipoDocumentoPai);
		tipoDocumentoPai.setPai(null);

		return tipoDocumentoPai;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setPai(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setPai(null);

		return usuario;
	}

}