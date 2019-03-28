package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the ESQUEMA database table.
 * 
 */
@Entity
@NamedQuery(name="Esquema.findAll", query="SELECT e FROM Esquema e")
public class Esquema implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ESQUEMA_IDESQUEMA_GENERATOR", sequenceName="ESQUEMA_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ESQUEMA_IDESQUEMA_GENERATOR")
	@Column(name="ID_ESQUEMA")
	private long idEsquema;

	private String descripcion;

	@Column(name="ES_RECURRENTE")
	private String esRecurrente;

	private String genero;

	@Column(name="MES_FIN")
	private BigDecimal mesFin;

	@Column(name="MES_INICIO")
	private BigDecimal mesInicio;

	private String nombre;

	//bi-directional many-to-one association to Estado
	@ManyToOne
	@JoinColumn(name="ID_ESTADO")
	private Estado estado;

	//bi-directional many-to-one association to Pais
	@ManyToOne
	@JoinColumn(name="ID_PAIS")
	private Pais pai;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="ID_USUARIO")
	private Usuario usuario;

	//bi-directional many-to-one association to EsquemaAgregado
	@OneToMany(mappedBy="esquema")
	private List<EsquemaAgregado> esquemaAgregados;

	//bi-directional many-to-one association to VacunaEsquema
	@OneToMany(mappedBy="esquema",fetch= FetchType.EAGER)
	private List<VacunaEsquema> vacunaEsquemas;

	public Esquema() {
	}

	public long getIdEsquema() {
		return this.idEsquema;
	}

	public void setIdEsquema(long idEsquema) {
		this.idEsquema = idEsquema;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEsRecurrente() {
		return this.esRecurrente;
	}

	public void setEsRecurrente(String esRecurrente) {
		this.esRecurrente = esRecurrente;
	}

	public String getGenero() {
		return this.genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public BigDecimal getMesFin() {
		return this.mesFin;
	}

	public void setMesFin(BigDecimal mesFin) {
		this.mesFin = mesFin;
	}

	public BigDecimal getMesInicio() {
		return this.mesInicio;
	}

	public void setMesInicio(BigDecimal mesInicio) {
		this.mesInicio = mesInicio;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Estado getEstado() {
		return this.estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Pais getPai() {
		return this.pai;
	}

	public void setPai(Pais pai) {
		this.pai = pai;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<EsquemaAgregado> getEsquemaAgregados() {
		return this.esquemaAgregados;
	}

	public void setEsquemaAgregados(List<EsquemaAgregado> esquemaAgregados) {
		this.esquemaAgregados = esquemaAgregados;
	}

	public EsquemaAgregado addEsquemaAgregado(EsquemaAgregado esquemaAgregado) {
		getEsquemaAgregados().add(esquemaAgregado);
		esquemaAgregado.setEsquema(this);

		return esquemaAgregado;
	}

	public EsquemaAgregado removeEsquemaAgregado(EsquemaAgregado esquemaAgregado) {
		getEsquemaAgregados().remove(esquemaAgregado);
		esquemaAgregado.setEsquema(null);

		return esquemaAgregado;
	}

	public List<VacunaEsquema> getVacunaEsquemas() {
		return this.vacunaEsquemas;
	}

	public void setVacunaEsquemas(List<VacunaEsquema> vacunaEsquemas) {
		this.vacunaEsquemas = vacunaEsquemas;
	}

	public VacunaEsquema addVacunaEsquema(VacunaEsquema vacunaEsquema) {
		getVacunaEsquemas().add(vacunaEsquema);
		vacunaEsquema.setEsquema(this);

		return vacunaEsquema;
	}

	public VacunaEsquema removeVacunaEsquema(VacunaEsquema vacunaEsquema) {
		getVacunaEsquemas().remove(vacunaEsquema);
		vacunaEsquema.setEsquema(null);

		return vacunaEsquema;
	}

}