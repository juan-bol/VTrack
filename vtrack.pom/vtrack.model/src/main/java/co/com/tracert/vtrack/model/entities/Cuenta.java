package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the CUENTA database table.
 * 
 */
@Entity
@NamedQuery(name="Cuenta.findAll", query="SELECT c FROM Cuenta c")
public class Cuenta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CUENTA_IDCUENTA_GENERATOR", sequenceName="CUENTA_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CUENTA_IDCUENTA_GENERATOR")
	@Column(name="ID_CUENTA")
	private long idCuenta;

	private String contrasenia;

	private String correo;
	
	// Se comenta para poder agregar la hora en la BD
//	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_CIERRE_SESION")
	private Date fechaCierreSesion;

	// Se comenta para poder agregar la hora en la BD
//	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_INICIO_SESION")
	private Date fechaInicioSesion;

	//bi-directional many-to-one association to Estado
	@ManyToOne
	@JoinColumn(name="ID_ESTADO")
	private Estado estado;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="ID_USUARIO")
	private Usuario usuario;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="cuenta")
	private List<Usuario> usuarios;

	public Cuenta() {
	}

	public long getIdCuenta() {
		return this.idCuenta;
	}

	public void setIdCuenta(long idCuenta) {
		this.idCuenta = idCuenta;
	}

	public String getContrasenia() {
		return this.contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}
	
	public Date getFechaCierreSesion() {
		return this.fechaCierreSesion;
	}

	public void setFechaCierreSesion(Date fechaCierreSesion) {
		this.fechaCierreSesion = fechaCierreSesion;
	}
	
	public Date getFechaInicioSesion() {
		return this.fechaInicioSesion;
	}

	public void setFechaInicioSesion(Date fechaInicioSesion) {
		this.fechaInicioSesion = fechaInicioSesion;
	}

	public Estado getEstado() {
		return this.estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setCuenta(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setCuenta(null);

		return usuario;
	}

}