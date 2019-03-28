package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the MENU_ROL database table.
 * 
 */
@Entity
@Table(name="MENU_ROL")
@NamedQuery(name="MenuRol.findAll", query="SELECT m FROM MenuRol m")
public class MenuRol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MENU_ROL_IDMENUROL_GENERATOR", sequenceName="MENU_ROL_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MENU_ROL_IDMENUROL_GENERATOR")
	@Column(name="ID_MENU_ROL")
	private long idMenuRol;

	//bi-directional many-to-one association to Menu
	@ManyToOne
	@JoinColumn(name="ID_MENU")
	private Menu menu;

	//bi-directional many-to-one association to Rol
	@ManyToOne
	@JoinColumn(name="ID_ROL")
	private Rol rol;

	public MenuRol() {
	}

	public long getIdMenuRol() {
		return this.idMenuRol;
	}

	public void setIdMenuRol(long idMenuRol) {
		this.idMenuRol = idMenuRol;
	}

	public Menu getMenu() {
		return this.menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Rol getRol() {
		return this.rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

}