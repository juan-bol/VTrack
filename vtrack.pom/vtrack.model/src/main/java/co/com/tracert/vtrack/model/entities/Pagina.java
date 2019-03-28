package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the PAGINA database table.
 * 
 */
@Entity
@NamedQuery(name="Pagina.findAll", query="SELECT p FROM Pagina p")
public class Pagina implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PAGINA_IDPAGINA_GENERATOR", sequenceName="PAGINA_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PAGINA_IDPAGINA_GENERATOR")
	@Column(name="ID_PAGINA")
	private long idPagina;

	private String url;

	//bi-directional many-to-one association to MenuPagina
	@OneToMany(mappedBy="pagina")
	private List<MenuPagina> menuPaginas;

	public Pagina() {
	}

	public long getIdPagina() {
		return this.idPagina;
	}

	public void setIdPagina(long idPagina) {
		this.idPagina = idPagina;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<MenuPagina> getMenuPaginas() {
		return this.menuPaginas;
	}

	public void setMenuPaginas(List<MenuPagina> menuPaginas) {
		this.menuPaginas = menuPaginas;
	}

	public MenuPagina addMenuPagina(MenuPagina menuPagina) {
		getMenuPaginas().add(menuPagina);
		menuPagina.setPagina(this);

		return menuPagina;
	}

	public MenuPagina removeMenuPagina(MenuPagina menuPagina) {
		getMenuPaginas().remove(menuPagina);
		menuPagina.setPagina(null);

		return menuPagina;
	}

}