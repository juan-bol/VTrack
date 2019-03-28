package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ROL database table.
 * 
 */
@Entity
@NamedQuery(name="Rol.findAll", query="SELECT r FROM Rol r")
public class Rol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ROL_IDROL_GENERATOR", sequenceName="ROL_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROL_IDROL_GENERATOR")
	@Column(name="ID_ROL")
	private long idRol;

	private String nombre;

	//bi-directional many-to-one association to MenuRol
	@OneToMany(mappedBy="rol")
	private List<MenuRol> menuRols;

	//bi-directional many-to-one association to RolUsuario
	@OneToMany(mappedBy="rol")
	private List<RolUsuario> rolUsuarios;

	public Rol() {
	}

	public long getIdRol() {
		return this.idRol;
	}

	public void setIdRol(long idRol) {
		this.idRol = idRol;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<MenuRol> getMenuRols() {
		return this.menuRols;
	}

	public void setMenuRols(List<MenuRol> menuRols) {
		this.menuRols = menuRols;
	}

	public MenuRol addMenuRol(MenuRol menuRol) {
		getMenuRols().add(menuRol);
		menuRol.setRol(this);

		return menuRol;
	}

	public MenuRol removeMenuRol(MenuRol menuRol) {
		getMenuRols().remove(menuRol);
		menuRol.setRol(null);

		return menuRol;
	}

	public List<RolUsuario> getRolUsuarios() {
		return this.rolUsuarios;
	}

	public void setRolUsuarios(List<RolUsuario> rolUsuarios) {
		this.rolUsuarios = rolUsuarios;
	}

	public RolUsuario addRolUsuario(RolUsuario rolUsuario) {
		getRolUsuarios().add(rolUsuario);
		rolUsuario.setRol(this);

		return rolUsuario;
	}

	public RolUsuario removeRolUsuario(RolUsuario rolUsuario) {
		getRolUsuarios().remove(rolUsuario);
		rolUsuario.setRol(null);

		return rolUsuario;
	}

}