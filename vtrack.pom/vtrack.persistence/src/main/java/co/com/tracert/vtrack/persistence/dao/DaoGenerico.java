package co.com.tracert.vtrack.persistence.dao;


import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import co.com.tracert.vtrack.model.exceptions.ExcepcionNegocio;

/**
 * @Nombre: DaoGenerico
 * @Fecha: 02/10/2018
 * @Descripcion: DAO generico para operaciones de escritura, lectura, borrado y consultas.
 * @author Diego Lamus
 * @param <T> Tipo de dato sobre el cual se instancia el DAO
 */
public class DaoGenerico <T> {

	private Class<T> tipoClase;
	
	protected EntityManager entityManager;
	
	/**
	 * @author Diego Lamus
	 * @param entityManager instancia de EntityManager para persistir datos
	 * @param tipoClase tipo de dato (.class) sobre el cual se van a realizar operaciones en la base de datos.
	 */
	public DaoGenerico(EntityManager entityManager, Class<T> tipoClase) {
		this.entityManager = entityManager;
		this.tipoClase = tipoClase;
	}
	
	/**
	 * @author Diego Lamus
	 * @param entidad instancia de la entidad que sera persistida en la base de datos
	 * @throws Exception si se generan errores al escribir en la base de datos
	 */
	public T crear(T entidad) throws Exception {
		entityManager.persist(entidad);
		return entidad;
	}
	
	/**
	 * @author Diego Lamus
	 * @param id identificador del registro que se esta buscando en la base de datos
	 * @return instancia del tipo de dato que se esta buscando
	 * @throws Exception si se genera error en la lectura del dato
	 */
	public T encontrarPorId(Object id) throws Exception {
		return entityManager.find(tipoClase, id);
	}
	
	/**
	 * @author Diego lamus
	 * @param entidad instanca de la entidad que sera actualizada en la base de datos
	 * @return 
	 * @throws Exception si se genera algun error al actualizar el registro en la base de datos.
	 */
	public T actualizar(T entidad) throws Exception {
		return (T)entityManager.merge(entidad);
	}
	
	/**
	 * @author Diego Lamus
	 * @param entidad instancia de la entidad que sera eliminada de la base de datos
	 * @throws Exception si se genera algun error en el borrado del registro de la base de datos
	 */
	public T eliminar(T entidad) throws Exception {
		entityManager.remove(entidad);
		return entidad;
	}
	
	/**
	 * @author 
	 * @param entidad instancia de la entidad que sera usada como referencia para consultar la base de datos. 
	 * @descripcion Se realiza una  consulta  sobre los atributos de la entidad que no son nulos. Si todos los atributos son nulos se consultan todos los elementos de la tabla.
	 * @return Lista de entidades que tienen los atributos en comun con la entidad que se ingreso por parametro
	 * @throws Exception si se genera error en la consulta.
	 */
	public List<T> encontrarPorAtributos(T entidad) throws Exception {
		throw new ExcepcionNegocio("No implementado aun");
	}
	
	@SuppressWarnings("unchecked")
	public List<T> encontrarTodos() throws Exception {
		String clase = tipoClase.getName();
		String[] splitClass = clase.split("\\.");
		String jpql = "SELECT x FROM "+ splitClass[splitClass.length-1]+" x";
		Query consulta = entityManager.createQuery(jpql);
		List<T> entidades = (List<T>) consulta.getResultList();
		return entidades;
	}
	
}
