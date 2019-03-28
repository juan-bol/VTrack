package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the MENU_PAGINA database table.
 * 
 */
@Entity
@Table(name="MENU_PAGINA")
@NamedQuery(name="MenuPagina.findAll", query="SELECT m FROM MenuPagina m")
public class MenuPagina implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MENU_PAGINA_IDMENUPAGINA_GENERATOR", sequenceName="MENU_PAGINA_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MENU_PAGINA_IDMENUPAGINA_GENERATOR")
	@Column(name="ID_MENU_PAGINA")
	private long idMenuPagina;

	//bi-directional many-to-one association to Menu
	@ManyToOne
	@JoinColumn(name="ID_MENU")
	private Menu menu;

	//bi-directional many-to-one association to Pagina
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_PAGINA")
	private Pagina pagina;

	public MenuPagina() {
	}

	public long getIdMenuPagina() {
		return this.idMenuPagina;
	}

	public void setIdMenuPagina(long idMenuPagina) {
		this.idMenuPagina = idMenuPagina;
	}

	public Menu getMenu() {
		return this.menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Pagina getPagina() {
		return this.pagina;
	}

	public void setPagina(Pagina pagina) {
		this.pagina = pagina;
	}

}