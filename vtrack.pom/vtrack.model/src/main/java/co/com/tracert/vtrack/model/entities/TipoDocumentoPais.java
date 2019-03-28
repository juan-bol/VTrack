package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the TIPO_DOCUMENTO_PAIS database table.
 * 
 */
@Entity
@Table(name="TIPO_DOCUMENTO_PAIS")
@NamedQuery(name="TipoDocumentoPais.findAll", query="SELECT t FROM TipoDocumentoPais t")
public class TipoDocumentoPais implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TIPO_DOCUMENTO_PAIS_IDTIPODOCPAIS_GENERATOR", sequenceName="TIPO_DOCUMENTO_PAIS_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_DOCUMENTO_PAIS_IDTIPODOCPAIS_GENERATOR")
	@Column(name="ID_TIPO_DOC_PAIS")
	private long idTipoDocPais;

	@Column(name="TIENE_LETRAS")
	private String tieneLetras;

	//bi-directional many-to-one association to Pais
	@ManyToOne
	@JoinColumn(name="ID_PAIS")
	private Pais pai;

	//bi-directional many-to-one association to TipoDocumento
	@ManyToOne
	@JoinColumn(name="ID_TIPO_DOCUMENTO")
	private TipoDocumento tipoDocumento;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="tipoDocumentoPai")
	private List<Usuario> usuarios;

	public TipoDocumentoPais() {
	}

	public long getIdTipoDocPais() {
		return this.idTipoDocPais;
	}

	public void setIdTipoDocPais(long idTipoDocPais) {
		this.idTipoDocPais = idTipoDocPais;
	}

	public String getTieneLetras() {
		return this.tieneLetras;
	}

	public void setTieneLetras(String tieneLetras) {
		this.tieneLetras = tieneLetras;
	}

	public Pais getPai() {
		return this.pai;
	}

	public void setPai(Pais pai) {
		this.pai = pai;
	}

	public TipoDocumento getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setTipoDocumentoPai(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setTipoDocumentoPai(null);

		return usuario;
	}

}