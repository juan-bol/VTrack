package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the PERMISO database table.
 * 
 */
@Entity
@NamedQuery(name="Permiso.findAll", query="SELECT p FROM Permiso p")
public class Permiso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PERMISO_IDPERMISO_GENERATOR", sequenceName="PERMISO_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PERMISO_IDPERMISO_GENERATOR")
	@Column(name="ID_PERMISO")
	private long idPermiso;

	@Column(name="NOMBRE_CUENTA")
	private String nombreCuenta;

	@Column(name="TIPO_PERMISO")
	private String tipoPermiso;

	//bi-directional many-to-one association to Estado
	@ManyToOne
	@JoinColumn(name="ID_ESTADO")
	private Estado estado;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="ID_USUARIO_DESTINO")
	private Usuario usuarioDestino;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="ID_USUARIO_ORIGEN")
	private Usuario usuarioOrigen;

	public Permiso() {
	}

	public long getIdPermiso() {
		return this.idPermiso;
	}

	public void setIdPermiso(long idPermiso) {
		this.idPermiso = idPermiso;
	}

	public String getNombreCuenta() {
		return this.nombreCuenta;
	}

	public void setNombreCuenta(String nombreCuenta) {
		this.nombreCuenta = nombreCuenta;
	}

	public String getTipoPermiso() {
		return this.tipoPermiso;
	}

	public void setTipoPermiso(String tipoPermiso) {
		this.tipoPermiso = tipoPermiso;
	}

	public Estado getEstado() {
		return this.estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Usuario getUsuarioDestino() {
		return this.usuarioDestino;
	}

	public void setUsuarioDestino(Usuario usuarioDestino) {
		this.usuarioDestino = usuarioDestino;
	}

	public Usuario getUsuarioOrigen() {
		return this.usuarioOrigen;
	}

	public void setUsuarioOrigen(Usuario usuarioOrigen) {
		this.usuarioOrigen = usuarioOrigen;
	}

}