package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the TIPO_DOCUMENTO database table.
 * 
 */
@Entity
@Table(name="TIPO_DOCUMENTO")
@NamedQuery(name="TipoDocumento.findAll", query="SELECT t FROM TipoDocumento t")
public class TipoDocumento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TIPO_DOCUMENTO_IDTIPODOCUMENTO_GENERATOR", sequenceName="TIPO_DOCUMENTO_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_DOCUMENTO_IDTIPODOCUMENTO_GENERATOR")
	@Column(name="ID_TIPO_DOCUMENTO")
	private long idTipoDocumento;

	private String nombre;

	//bi-directional many-to-one association to TipoDocumentoPais
	@OneToMany(mappedBy="tipoDocumento")
	private List<TipoDocumentoPais> tipoDocumentoPais;

	public TipoDocumento() {
	}

	public long getIdTipoDocumento() {
		return this.idTipoDocumento;
	}

	public void setIdTipoDocumento(long idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<TipoDocumentoPais> getTipoDocumentoPais() {
		return this.tipoDocumentoPais;
	}

	public void setTipoDocumentoPais(List<TipoDocumentoPais> tipoDocumentoPais) {
		this.tipoDocumentoPais = tipoDocumentoPais;
	}

	public TipoDocumentoPais addTipoDocumentoPai(TipoDocumentoPais tipoDocumentoPai) {
		getTipoDocumentoPais().add(tipoDocumentoPai);
		tipoDocumentoPai.setTipoDocumento(this);

		return tipoDocumentoPai;
	}

	public TipoDocumentoPais removeTipoDocumentoPai(TipoDocumentoPais tipoDocumentoPai) {
		getTipoDocumentoPais().remove(tipoDocumentoPai);
		tipoDocumentoPai.setTipoDocumento(null);

		return tipoDocumentoPai;
	}

}