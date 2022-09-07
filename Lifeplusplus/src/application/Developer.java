package application;

import com.google.gson.JsonObject;

/*
 * CLASE DEVELOPER
 * Hereda de Persona
 * @see Persona
 */

public class Developer extends Persona {

	/*
	 * Constructor, construye un nuevo developer con argumentos
	 * 
	 * @param dni
	 * 
	 * @param nombre
	 * 
	 * @param password
	 * 
	 * @param correo
	 */
	public Developer(String dni, String nombre, String password, String correo) {
		super(dni, nombre, password, correo);

	}

	/*
	 * Constructor, construye un nuevo developer con JSON
	 * 
	 * @param jDeveloper datos JsonObject
	 */
	public Developer(JsonObject jDeveloper) {
		super(jDeveloper);

	}

	/*
	 * Pasar y obtener un developer en formato JSON
	 * 
	 * @return JsonObject
	 */
	public JsonObject toJson() {
		return super.toJson();

	}

}