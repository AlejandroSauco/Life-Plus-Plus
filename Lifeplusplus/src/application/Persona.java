package application;

import com.google.gson.JsonObject;

/*
 * CLASE PERSONA
 * Clase con atributos comunes al resto de clases
 */

public class Persona {
	protected String dni;
	protected String nombre;
	protected String password;
	protected String correo;

	/*
	 * Constructor, construye una nueva persona con argumentos
	 * 
	 * @param nuevoDni
	 * 
	 * @param nuevoNombre
	 * 
	 * @param nuevaPassword
	 * 
	 * @param nuevoCorreo
	 */
	public Persona(String nuevoDni, String nuevoNombre, String nuevaPassword, String nuevoCorreo) { // Para el																						// Medico, Paciente)
		this.dni = nuevoDni;
		this.nombre = nuevoNombre;
		this.password = nuevaPassword;
		this.correo = nuevoCorreo;
	}
	
	public Persona(String nuevoDni) { // Para el																						// Medico, Paciente)
		this.dni = nuevoDni;
	}
	
	public Persona(String nuevoDni, String nuevoNombre) { // Para el																						// Medico, Paciente)
		this.dni = nuevoDni;
		this.nombre = nuevoNombre;
	}

	/*
	 * Constructor, construye una nueva persona con JSON
	 * 
	 * @param jPersona datos JsonObject
	 */
	public Persona(JsonObject jPersona) {
		this.dni = jPersona.get("dni").getAsString();
		this.nombre = jPersona.get("nombre").getAsString();
		this.password = jPersona.get("password").getAsString();
		this.correo = jPersona.get("correo").getAsString();
	}

	/*
	 * Pasar y obtener una persona en formato JSON
	 * 
	 * @return JsonObject
	 */
	public JsonObject toJson() {
		JsonObject j = new JsonObject();
		j.addProperty("dni", this.dni);
		j.addProperty("nombre", this.nombre);
		j.addProperty("password", this.password);
		j.addProperty("correo", this.correo);

		return j;

	}

	/*
	 * Getters
	 */

	public String getNombre() {
		return nombre;
	}

	public String getDni() {
		return dni;
	}

	public String getPassword() {
		return password;
	}

	public String getCorreo() {
		return correo;
	}

	/*
	 * Setters
	 */

	public void setDni(String nuevoDni) {
		dni = nuevoDni;
	}

	public void setPassword(String nuevaPassword) {
		password = nuevaPassword;
	}

	public void setCorreo(String nuevoCorreo) {
		correo = nuevoCorreo;
	}

	public void setNombre(String nuevoNombre) {
		nombre = nuevoNombre;
	}
}
