package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the MENU database table.
 * 
 */
@Entity
@NamedQuery(name="Menu.findAll", query="SELECT m FROM Menu m")
public class Menu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MENU_IDMENU_GENERATOR", sequenceName="MENU_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MENU_IDMENU_GENERATOR")
	@Column(name="ID_MENU")
	private long idMenu;

	private String icono;

	private String nombre;

	//bi-directional many-to-one association to Menu
	@ManyToOne
	@JoinColumn(name="ID_MENU_PADRE")
	private Menu menu;

	//bi-directional many-to-one association to Menu
	@OneToMany(mappedBy="menu")
	private List<Menu> menus;

	//bi-directional many-to-one association to MenuPagina
	@OneToMany(mappedBy="menu",fetch=FetchType.EAGER)
	private List<MenuPagina> menuPaginas;

	//bi-directional many-to-one association to MenuRol
	@OneToMany(mappedBy="menu")
	private List<MenuRol> menuRols;

	public Menu() {
	}

	public long getIdMenu() {
		return this.idMenu;
	}

	public void setIdMenu(long idMenu) {
		this.idMenu = idMenu;
	}

	public String getIcono() {
		return this.icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Menu getMenu() {
		return this.menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public List<Menu> getMenus() {
		return this.menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public Menu addMenus(Menu menus) {
		getMenus().add(menus);
		menus.setMenu(this);

		return menus;
	}

	public Menu removeMenus(Menu menus) {
		getMenus().remove(menus);
		menus.setMenu(null);

		return menus;
	}

	public List<MenuPagina> getMenuPaginas() {
		return this.menuPaginas;
	}

	public void setMenuPaginas(List<MenuPagina> menuPaginas) {
		this.menuPaginas = menuPaginas;
	}

	public MenuPagina addMenuPagina(MenuPagina menuPagina) {
		getMenuPaginas().add(menuPagina);
		menuPagina.setMenu(this);

		return menuPagina;
	}

	public MenuPagina removeMenuPagina(MenuPagina menuPagina) {
		getMenuPaginas().remove(menuPagina);
		menuPagina.setMenu(null);

		return menuPagina;
	}

	public List<MenuRol> getMenuRols() {
		return this.menuRols;
	}

	public void setMenuRols(List<MenuRol> menuRols) {
		this.menuRols = menuRols;
	}

	public MenuRol addMenuRol(MenuRol menuRol) {
		getMenuRols().add(menuRol);
		menuRol.setMenu(this);

		return menuRol;
	}

	public MenuRol removeMenuRol(MenuRol menuRol) {
		getMenuRols().remove(menuRol);
		menuRol.setMenu(null);

		return menuRol;
	}

}