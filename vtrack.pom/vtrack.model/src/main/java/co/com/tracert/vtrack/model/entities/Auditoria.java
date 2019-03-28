package co.com.tracert.vtrack.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the AUDITORIA database table.
 * 
 */
@Entity
@NamedQuery(name="Auditoria.findAll", query="SELECT a FROM Auditoria a")
public class Auditoria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="AUDITORIA_IDAUDITORIA_GENERATOR", sequenceName="AUDITORIA_SEC", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AUDITORIA_IDAUDITORIA_GENERATOR")
	@Column(name="ID_AUDITORIA")
	private long idAuditoria;

	private String accion;

	// Se comenta para poder agregar la hora en la BD
//	@Temporal(TemporalType.DATE)
	private Date fecha;

	@Column(name="ID_TABLA_AFECTADA")
	private BigDecimal idTablaAfectada;

	@Column(name="NOMBRE_TABLA_AFECTADA")
	private String nombreTablaAfectada;

	@Lob
	@Column(name="VALOR_ANTERIOR")
	private String valorAnterior;

	@Lob
	@Column(name="VALOR_NUEVO")
	private String valorNuevo;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="ID_USUARIO_CREADOR")
	private Usuario usuario;

	public Auditoria() {
	}

	public long getIdAuditoria() {
		return this.idAuditoria;
	}

	public void setIdAuditoria(long idAuditoria) {
		this.idAuditoria = idAuditoria;
	}

	public String getAccion() {
		return this.accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getIdTablaAfectada() {
		return this.idTablaAfectada;
	}

	public void setIdTablaAfectada(BigDecimal idTablaAfectada) {
		this.idTablaAfectada = idTablaAfectada;
	}

	public String getNombreTablaAfectada() {
		return this.nombreTablaAfectada;
	}

	public void setNombreTablaAfectada(String nombreTablaAfectada) {
		this.nombreTablaAfectada = nombreTablaAfectada;
	}

	public String getValorAnterior() {
		return this.valorAnterior;
	}

	public void setValorAnterior(String valorAnterior) {
		this.valorAnterior = valorAnterior;
	}

	public String getValorNuevo() {
		return this.valorNuevo;
	}

	public void setValorNuevo(String valorNuevo) {
		this.valorNuevo = valorNuevo;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}