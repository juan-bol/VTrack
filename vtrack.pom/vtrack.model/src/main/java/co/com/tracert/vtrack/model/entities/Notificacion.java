package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the NOTIFICACION database table.
 * 
 */
@Entity
@NamedQuery(name="Notificacion.findAll", query="SELECT n FROM Notificacion n")
public class Notificacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="NOTIFICACION_IDNOTIFICACION_GENERATOR", sequenceName="NOTIFICACION_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="NOTIFICACION_IDNOTIFICACION_GENERATOR")
	@Column(name="ID_NOTIFICACION")
	private long idNotificacion;

	@Column(name="DIAS_ANTICIPACION")
	private BigDecimal diasAnticipacion;

	//bi-directional many-to-one association to Estado
	@ManyToOne
	@JoinColumn(name="ID_ESTADO")
	private Estado estado;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="ID_USUARIO")
	private Usuario usuario;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="notificacion")
	private List<Usuario> usuarios;

	public Notificacion() {
	}

	public long getIdNotificacion() {
		return this.idNotificacion;
	}

	public void setIdNotificacion(long idNotificacion) {
		this.idNotificacion = idNotificacion;
	}

	public BigDecimal getDiasAnticipacion() {
		return this.diasAnticipacion;
	}

	public void setDiasAnticipacion(BigDecimal diasAnticipacion) {
		this.diasAnticipacion = diasAnticipacion;
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

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setNotificacion(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setNotificacion(null);

		return usuario;
	}

}