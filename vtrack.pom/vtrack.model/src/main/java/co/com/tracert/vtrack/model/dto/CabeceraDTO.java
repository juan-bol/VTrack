package co.com.tracert.vtrack.model.dto;

import java.io.Serializable;

/**
 * Clase que facilita la gestión de las cabeceras de las tablas de los esquemas
 * 
 */
public class CabeceraDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3503398171345846402L;
	
	/**
	 * atributo que representa el nombre de la cabecera de la columna de la tabla de esquemas
	 */
	private String nombre;
	
	/**
	 * atributo que representa el entero de la posición de la cabecera en las columnas de la tabla del esquema
	 */
	private int posicion;
	
	
	
	public CabeceraDTO(String nombre, int posicion) {
		this.nombre = nombre;
		this.posicion = posicion;
	}
	
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getPosicion() {
		return posicion;
	}
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

}
