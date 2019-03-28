package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ESQUEMA_AGREGADO database table.
 * 
 */
@Entity
@Table(name="ESQUEMA_AGREGADO")
@NamedQuery(name="EsquemaAgregado.findAll", query="SELECT e FROM EsquemaAgregado e")
public class EsquemaAgregado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ESQUEMA_AGREGADO_IDESQUEMAAGREGADO_GENERATOR", sequenceName="ESQUEMA_AGREGADO_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ESQUEMA_AGREGADO_IDESQUEMAAGREGADO_GENERATOR")
	@Column(name="ID_ESQUEMA_AGREGADO")
	private long idEsquemaAgregado;

	//bi-directional many-to-one association to Esquema
	@ManyToOne
	@JoinColumn(name="ID_ESQUEMA")
	private Esquema esquema;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="ID_USUARIO")
	private Usuario usuario;

	public EsquemaAgregado() {
	}

	public long getIdEsquemaAgregado() {
		return this.idEsquemaAgregado;
	}

	public void setIdEsquemaAgregado(long idEsquemaAgregado) {
		this.idEsquemaAgregado = idEsquemaAgregado;
	}

	public Esquema getEsquema() {
		return this.esquema;
	}

	public void setEsquema(Esquema esquema) {
		this.esquema = esquema;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}