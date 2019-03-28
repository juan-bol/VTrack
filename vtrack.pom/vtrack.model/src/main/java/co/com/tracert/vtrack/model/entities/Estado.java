package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ESTADO database table.
 * 
 */
@Entity
@NamedQuery(name="Estado.findAll", query="SELECT e FROM Estado e")
public class Estado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ESTADO_IDESTADO_GENERATOR", sequenceName="ESTADO_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ESTADO_IDESTADO_GENERATOR")
	@Column(name="ID_ESTADO")
	private long idEstado;

	private String estado;

	//bi-directional many-to-one association to CentroVacunacion
	@OneToMany(mappedBy="estado")
	private List<CentroVacunacion> centroVacunacions;

	//bi-directional many-to-one association to Cuenta
	@OneToMany(mappedBy="estado")
	private List<Cuenta> cuentas;

	//bi-directional many-to-one association to DosisAplicada
	@OneToMany(mappedBy="estado")
	private List<DosisAplicada> dosisAplicadas;

	//bi-directional many-to-one association to Esquema
	@OneToMany(mappedBy="estado")
	private List<Esquema> esquemas;

	//bi-directional many-to-one association to TipoEstado
	@ManyToOne
	@JoinColumn(name="ID_TIPO_ESTADO")
	private TipoEstado tipoEstado;

	//bi-directional many-to-one association to Permiso
	@OneToMany(mappedBy="estado")
	private List<Permiso> permisos;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="estado")
	private List<Usuario> usuarios;

	//bi-directional many-to-one association to Vacuna
	@OneToMany(mappedBy="estado")
	private List<Vacuna> vacunas;

	//bi-directional many-to-one association to VacunaEsquema
	@OneToMany(mappedBy="estado")
	private List<VacunaEsquema> vacunaEsquemas;

	//bi-directional many-to-one association to Notificacion
	@OneToMany(mappedBy="estado")
	private List<Notificacion> notificacions;

	public Estado() {
	}

	public long getIdEstado() {
		return this.idEstado;
	}

	public void setIdEstado(long idEstado) {
		this.idEstado = idEstado;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<CentroVacunacion> getCentroVacunacions() {
		return this.centroVacunacions;
	}

	public void setCentroVacunacions(List<CentroVacunacion> centroVacunacions) {
		this.centroVacunacions = centroVacunacions;
	}

	public CentroVacunacion addCentroVacunacion(CentroVacunacion centroVacunacion) {
		getCentroVacunacions().add(centroVacunacion);
		centroVacunacion.setEstado(this);

		return centroVacunacion;
	}

	public CentroVacunacion removeCentroVacunacion(CentroVacunacion centroVacunacion) {
		getCentroVacunacions().remove(centroVacunacion);
		centroVacunacion.setEstado(null);

		return centroVacunacion;
	}

	public List<Cuenta> getCuentas() {
		return this.cuentas;
	}

	public void setCuentas(List<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}

	public Cuenta addCuenta(Cuenta cuenta) {
		getCuentas().add(cuenta);
		cuenta.setEstado(this);

		return cuenta;
	}

	public Cuenta removeCuenta(Cuenta cuenta) {
		getCuentas().remove(cuenta);
		cuenta.setEstado(null);

		return cuenta;
	}

	public List<DosisAplicada> getDosisAplicadas() {
		return this.dosisAplicadas;
	}

	public void setDosisAplicadas(List<DosisAplicada> dosisAplicadas) {
		this.dosisAplicadas = dosisAplicadas;
	}

	public DosisAplicada addDosisAplicada(DosisAplicada dosisAplicada) {
		getDosisAplicadas().add(dosisAplicada);
		dosisAplicada.setEstado(this);

		return dosisAplicada;
	}

	public DosisAplicada removeDosisAplicada(DosisAplicada dosisAplicada) {
		getDosisAplicadas().remove(dosisAplicada);
		dosisAplicada.setEstado(null);

		return dosisAplicada;
	}

	public List<Esquema> getEsquemas() {
		return this.esquemas;
	}

	public void setEsquemas(List<Esquema> esquemas) {
		this.esquemas = esquemas;
	}

	public Esquema addEsquema(Esquema esquema) {
		getEsquemas().add(esquema);
		esquema.setEstado(this);

		return esquema;
	}

	public Esquema removeEsquema(Esquema esquema) {
		getEsquemas().remove(esquema);
		esquema.setEstado(null);

		return esquema;
	}

	public TipoEstado getTipoEstado() {
		return this.tipoEstado;
	}

	public void setTipoEstado(TipoEstado tipoEstado) {
		this.tipoEstado = tipoEstado;
	}

	public List<Permiso> getPermisos() {
		return this.permisos;
	}

	public void setPermisos(List<Permiso> permisos) {
		this.permisos = permisos;
	}

	public Permiso addPermiso(Permiso permiso) {
		getPermisos().add(permiso);
		permiso.setEstado(this);

		return permiso;
	}

	public Permiso removePermiso(Permiso permiso) {
		getPermisos().remove(permiso);
		permiso.setEstado(null);

		return permiso;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setEstado(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setEstado(null);

		return usuario;
	}

	public List<Vacuna> getVacunas() {
		return this.vacunas;
	}

	public void setVacunas(List<Vacuna> vacunas) {
		this.vacunas = vacunas;
	}

	public Vacuna addVacuna(Vacuna vacuna) {
		getVacunas().add(vacuna);
		vacuna.setEstado(this);

		return vacuna;
	}

	public Vacuna removeVacuna(Vacuna vacuna) {
		getVacunas().remove(vacuna);
		vacuna.setEstado(null);

		return vacuna;
	}

	public List<VacunaEsquema> getVacunaEsquemas() {
		return this.vacunaEsquemas;
	}

	public void setVacunaEsquemas(List<VacunaEsquema> vacunaEsquemas) {
		this.vacunaEsquemas = vacunaEsquemas;
	}

	public VacunaEsquema addVacunaEsquema(VacunaEsquema vacunaEsquema) {
		getVacunaEsquemas().add(vacunaEsquema);
		vacunaEsquema.setEstado(this);

		return vacunaEsquema;
	}

	public VacunaEsquema removeVacunaEsquema(VacunaEsquema vacunaEsquema) {
		getVacunaEsquemas().remove(vacunaEsquema);
		vacunaEsquema.setEstado(null);

		return vacunaEsquema;
	}

	public List<Notificacion> getNotificacions() {
		return this.notificacions;
	}

	public void setNotificacions(List<Notificacion> notificacions) {
		this.notificacions = notificacions;
	}

	public Notificacion addNotificacion(Notificacion notificacion) {
		getNotificacions().add(notificacion);
		notificacion.setEstado(this);

		return notificacion;
	}

	public Notificacion removeNotificacion(Notificacion notificacion) {
		getNotificacions().remove(notificacion);
		notificacion.setEstado(null);

		return notificacion;
	}

}