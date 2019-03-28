package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the USUARIO database table.
 * 
 */
@Entity
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USUARIO_IDUSUARIO_GENERATOR", sequenceName="USUARIO_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USUARIO_IDUSUARIO_GENERATOR")
	@Column(name="ID_USUARIO")
	private long idUsuario;

	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_NACIMIENTO")
	private Date fechaNacimiento;

	private String genero;

	private String nombre;

	@Column(name="NUMERO_DOCUMENTO")
	private String numeroDocumento;

	@Column(name="ZIP_CODE")
	private String zipCode;

	//bi-directional many-to-one association to Cuenta
	@OneToMany(mappedBy="usuario")
	private List<Cuenta> cuentas;

	//bi-directional many-to-one association to DosisAplicada
	@OneToMany(mappedBy="usuario")
	private List<DosisAplicada> dosisAplicadas;

	//bi-directional many-to-one association to Esquema
	@OneToMany(mappedBy="usuario")
	private List<Esquema> esquemas;

	//bi-directional many-to-one association to EsquemaAgregado
	@OneToMany(mappedBy="usuario")
	private List<EsquemaAgregado> esquemaAgregados;

	//bi-directional many-to-one association to Permiso
	@OneToMany(mappedBy="usuarioDestino")
	private List<Permiso> permisosDestino;

	//bi-directional many-to-one association to Permiso
	@OneToMany(mappedBy="usuarioOrigen")
	private List<Permiso> permisosOrigen;

	//bi-directional many-to-one association to RolUsuario
	@OneToMany(mappedBy="usuario")
	private List<RolUsuario> rolUsuarios;

	//bi-directional many-to-one association to Ciudad
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_CIUDAD")
	private Ciudad ciudad;

	//bi-directional many-to-one association to Cuenta
	@ManyToOne
	@JoinColumn(name="ID_CUENTA")
	private Cuenta cuenta;

	//bi-directional many-to-one association to Estado
	@ManyToOne
	@JoinColumn(name="ID_ESTADO")
	private Estado estado;

	//bi-directional many-to-one association to Pais
	@ManyToOne
	@JoinColumn(name="ID_PAIS")
	private Pais pai;

	//bi-directional many-to-one association to Region
	@ManyToOne
	@JoinColumn(name="ID_REGION")
	private Region region;

	//bi-directional many-to-one association to TipoDocumentoPais
	@ManyToOne
	@JoinColumn(name="ID_TIPO_DOC_PAIS")
	private TipoDocumentoPais tipoDocumentoPai;

	//bi-directional many-to-one association to Vacuna
	@OneToMany(mappedBy="usuario")
	private List<Vacuna> vacunas;

	//bi-directional many-to-one association to VacunaOculta
	@OneToMany(mappedBy="usuario")
	private List<VacunaOculta> vacunaOcultas;

	//bi-directional many-to-one association to Auditoria
	@OneToMany(mappedBy="usuario")
	private List<Auditoria> auditorias;

	//bi-directional many-to-one association to Notificacion
	@OneToMany(mappedBy="usuario")
	private List<Notificacion> notificacions;

	//bi-directional many-to-one association to Notificacion
	@ManyToOne
	@JoinColumn(name="ID_NOTIFICACION")
	private Notificacion notificacion;

	public Usuario() {
	}

	public long getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Date getFechaNacimiento() {
		return this.fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getGenero() {
		return this.genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNumeroDocumento() {
		return this.numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public List<Cuenta> getCuentas() {
		return this.cuentas;
	}

	public void setCuentas(List<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}

	public Cuenta addCuenta(Cuenta cuenta) {
		getCuentas().add(cuenta);
		cuenta.setUsuario(this);

		return cuenta;
	}

	public Cuenta removeCuenta(Cuenta cuenta) {
		getCuentas().remove(cuenta);
		cuenta.setUsuario(null);

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
		dosisAplicada.setUsuario(this);

		return dosisAplicada;
	}

	public DosisAplicada removeDosisAplicada(DosisAplicada dosisAplicada) {
		getDosisAplicadas().remove(dosisAplicada);
		dosisAplicada.setUsuario(null);

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
		esquema.setUsuario(this);

		return esquema;
	}

	public Esquema removeEsquema(Esquema esquema) {
		getEsquemas().remove(esquema);
		esquema.setUsuario(null);

		return esquema;
	}

	public List<EsquemaAgregado> getEsquemaAgregados() {
		return this.esquemaAgregados;
	}

	public void setEsquemaAgregados(List<EsquemaAgregado> esquemaAgregados) {
		this.esquemaAgregados = esquemaAgregados;
	}

	public EsquemaAgregado addEsquemaAgregado(EsquemaAgregado esquemaAgregado) {
		getEsquemaAgregados().add(esquemaAgregado);
		esquemaAgregado.setUsuario(this);

		return esquemaAgregado;
	}

	public EsquemaAgregado removeEsquemaAgregado(EsquemaAgregado esquemaAgregado) {
		getEsquemaAgregados().remove(esquemaAgregado);
		esquemaAgregado.setUsuario(null);

		return esquemaAgregado;
	}

	public List<Permiso> getPermisosDestino() {
		return this.permisosDestino;
	}

	public void setPermisosDestino(List<Permiso> permisosDestino) {
		this.permisosDestino = permisosDestino;
	}

	public Permiso addPermisosDestino(Permiso permisosDestino) {
		getPermisosDestino().add(permisosDestino);
		permisosDestino.setUsuarioDestino(this);

		return permisosDestino;
	}

	public Permiso removePermisosDestino(Permiso permisosDestino) {
		getPermisosDestino().remove(permisosDestino);
		permisosDestino.setUsuarioDestino(null);

		return permisosDestino;
	}

	public List<Permiso> getPermisosOrigen() {
		return this.permisosOrigen;
	}

	public void setPermisosOrigen(List<Permiso> permisosOrigen) {
		this.permisosOrigen = permisosOrigen;
	}

	public Permiso addPermisosOrigen(Permiso permisosOrigen) {
		getPermisosOrigen().add(permisosOrigen);
		permisosOrigen.setUsuarioOrigen(this);

		return permisosOrigen;
	}

	public Permiso removePermisosOrigen(Permiso permisosOrigen) {
		getPermisosOrigen().remove(permisosOrigen);
		permisosOrigen.setUsuarioOrigen(null);

		return permisosOrigen;
	}

	public List<RolUsuario> getRolUsuarios() {
		return this.rolUsuarios;
	}

	public void setRolUsuarios(List<RolUsuario> rolUsuarios) {
		this.rolUsuarios = rolUsuarios;
	}

	public RolUsuario addRolUsuario(RolUsuario rolUsuario) {
		getRolUsuarios().add(rolUsuario);
		rolUsuario.setUsuario(this);

		return rolUsuario;
	}

	public RolUsuario removeRolUsuario(RolUsuario rolUsuario) {
		getRolUsuarios().remove(rolUsuario);
		rolUsuario.setUsuario(null);

		return rolUsuario;
	}

	public Ciudad getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public Cuenta getCuenta() {
		return this.cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
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

	public Region getRegion() {
		return this.region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public TipoDocumentoPais getTipoDocumentoPai() {
		return this.tipoDocumentoPai;
	}

	public void setTipoDocumentoPai(TipoDocumentoPais tipoDocumentoPai) {
		this.tipoDocumentoPai = tipoDocumentoPai;
	}

	public List<Vacuna> getVacunas() {
		return this.vacunas;
	}

	public void setVacunas(List<Vacuna> vacunas) {
		this.vacunas = vacunas;
	}

	public Vacuna addVacuna(Vacuna vacuna) {
		getVacunas().add(vacuna);
		vacuna.setUsuario(this);

		return vacuna;
	}

	public Vacuna removeVacuna(Vacuna vacuna) {
		getVacunas().remove(vacuna);
		vacuna.setUsuario(null);

		return vacuna;
	}

	public List<VacunaOculta> getVacunaOcultas() {
		return this.vacunaOcultas;
	}

	public void setVacunaOcultas(List<VacunaOculta> vacunaOcultas) {
		this.vacunaOcultas = vacunaOcultas;
	}

	public VacunaOculta addVacunaOculta(VacunaOculta vacunaOculta) {
		getVacunaOcultas().add(vacunaOculta);
		vacunaOculta.setUsuario(this);

		return vacunaOculta;
	}

	public VacunaOculta removeVacunaOculta(VacunaOculta vacunaOculta) {
		getVacunaOcultas().remove(vacunaOculta);
		vacunaOculta.setUsuario(null);

		return vacunaOculta;
	}

	public List<Auditoria> getAuditorias() {
		return this.auditorias;
	}

	public void setAuditorias(List<Auditoria> auditorias) {
		this.auditorias = auditorias;
	}

	public Auditoria addAuditoria(Auditoria auditoria) {
		getAuditorias().add(auditoria);
		auditoria.setUsuario(this);

		return auditoria;
	}

	public Auditoria removeAuditoria(Auditoria auditoria) {
		getAuditorias().remove(auditoria);
		auditoria.setUsuario(null);

		return auditoria;
	}

	public List<Notificacion> getNotificacions() {
		return this.notificacions;
	}

	public void setNotificacions(List<Notificacion> notificacions) {
		this.notificacions = notificacions;
	}

	public Notificacion addNotificacion(Notificacion notificacion) {
		getNotificacions().add(notificacion);
		notificacion.setUsuario(this);

		return notificacion;
	}

	public Notificacion removeNotificacion(Notificacion notificacion) {
		getNotificacions().remove(notificacion);
		notificacion.setUsuario(null);

		return notificacion;
	}

	public Notificacion getNotificacion() {
		return this.notificacion;
	}

	public void setNotificacion(Notificacion notificacion) {
		this.notificacion = notificacion;
	}

}