package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonObject;

/*
 * CLASE SOLICITUD
 * Hereda de Persona
 * @see Persona
 */

public class Solicitud {

	private String IDayuda;
	private String descripcion;
	private String solucion;
	private Boolean medico = false;
	private String dniUsuario;

	/*
	 * Constructor, construye una nueva solicitud con argumentos
	 * 
	 * @param nuevoDni
	 * 
	 * @param nuevoNombre
	 * 
	 * @param nuevaPassword
	 * 
	 * @param nuevoCorreo
	 */
	/*public Solicitud(String nuevoDni, String nuevoNombre, String nuevaPassword, String nuevoCorreo) {
		super(nuevoDni, nuevoNombre, nuevaPassword, nuevoCorreo);
		// TODO Auto-generated constructor stub
	}*/
	
	public Solicitud(String nuevoIDayuda, String nuevoDni) {
		
		IDayuda = nuevoIDayuda;
		dniUsuario = nuevoDni;
		// TODO Auto-generated constructor stub
	}

	/*
	 * Constructor, construye una nueva solicitud con argumentos
	 * 
	 * @param nuevoDni
	 * 
	 * @param nuevoNombre
	 * 
	 * @param nuevaPassword
	 * 
	 * @param nuevoCorreo
	 * 
	 * @param descripcion_
	 * 
	 * @param solucion_
	 * 
	 * @param medico_
	 */
	/*public Solicitud(String nuevoDni, String nuevoNombre, String nuevaPassword, String nuevoCorreo, String descripcion_,
			String solucion_, Boolean medico_) {
		super(nuevoDni, nuevoNombre, nuevaPassword, nuevoCorreo);
		this.descripcion = descripcion_;
		this.solucion = solucion_;
		this.medico = medico_;
	}*/

	/*
	 * Constructor, construye una nueva solicitud con JSON
	 * 
	 * @param jSolicitudes datos JsonObject
	 */
	/*public Solicitud(JsonObject jSolicitudes) {
		super(jSolicitudes);
		this.descripcion = jSolicitudes.get("descripcion").getAsString();
		this.solucion = jSolicitudes.get("solucion").getAsString();
		this.medico = jSolicitudes.get("medico").getAsBoolean();

	}*/

	/*
	 * Obtener una solicitud en formato String
	 * 
	 * @return String
	 */
	/*public String toString() {
		return "dni: " + dni + "\nnombre: " + nombre + "\npassword: " + password + "\ncorreo: " + correo
				+ "\ndescripcion: " + descripcion + "\nsolucion: " + solucion + "\nmedico: " + medico;
	}*/

	/*
	 * Pasar y obtener una solicitud en formato JSON
	 * 
	 * @return JsonObject
	 */
	/*public JsonObject toJson() {
		JsonObject j = super.toJson();
		j.addProperty("descripcion", this.descripcion);
		j.addProperty("solucion", this.solucion);
		j.addProperty("medico", this.medico);

		return j;

	}*/

	/*
	 * Getters
	 */

	public Boolean getMedico() {
		return medico;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getSolucion() {
		return solucion;
	}

	public String getIDayuda() {
		return IDayuda;
	}
	
	public String getDniUsuario() {
		return dniUsuario;
	}
	
	/*
	 * Setters
	 */

	public void setDniUsuario(String nuevoDni) {
		dniUsuario = nuevoDni;
	}
	
	public void setMedico(Boolean medico) {
		this.medico = medico;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setSolucion(String solucion) {
		this.solucion = solucion;
	}

	public void enviarAyuda(String Dni, String Descripcion, Boolean Medico) {

	}
}