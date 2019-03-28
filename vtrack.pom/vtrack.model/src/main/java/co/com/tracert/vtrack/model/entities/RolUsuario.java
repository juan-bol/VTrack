package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ROL_USUARIO database table.
 * 
 */
@Entity
@Table(name="ROL_USUARIO")
@NamedQuery(name="RolUsuario.findAll", query="SELECT r FROM RolUsuario r")
public class RolUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ROL_USUARIO_IDROLUSUARIO_GENERATOR", sequenceName="ROL_USUARIO_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROL_USUARIO_IDROLUSUARIO_GENERATOR")
	@Column(name="ID_ROL_USUARIO")
	private long idRolUsuario;

	//bi-directional many-to-one association to Rol
	@ManyToOne
	@JoinColumn(name="ID_ROL")
	private Rol rol;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="ID_USUARIO")
	private Usuario usuario;

	public RolUsuario() {
	}

	public long getIdRolUsuario() {
		return this.idRolUsuario;
	}

	public void setIdRolUsuario(long idRolUsuario) {
		this.idRolUsuario = idRolUsuario;
	}

	public Rol getRol() {
		return this.rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}