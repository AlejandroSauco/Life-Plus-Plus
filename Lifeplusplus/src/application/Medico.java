package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonObject;

/*
 * CLASE MEDICO
 * Hereda de Persona 
 * @see Persona
 */

public class Medico extends Persona {

	private Date nacimiento;
	private String institucion;
	private String rol;
	private int identificacionInstitucion;

	/*
	 * Constructor, construye un nuevo medico con argumentos
	 * 
	 * @param dni
	 * 
	 * @param nombre
	 * 
	 * @param password
	 * 
	 * @param correo
	 */
	public Medico(String dni, String nombre, String password, String correo) {
		super(dni, nombre, password, correo);

	}

	/*
	 * Constructor, construye un nuevo medico con argumentos
	 * 
	 * @param dni
	 * 
	 * @param nombre
	 * 
	 * @param password
	 * 
	 * @param correo
	 * 
	 * @param nacim
	 * 
	 * @param institucion
	 * 
	 * @param rol
	 * 
	 * @param identificacionInstitucion
	 */
	public Medico(String dni, String nombre, String password, String correo, Date nacim, String institucion, String rol,
			int identificacionInstitucion) {
		super(dni, nombre, password, correo);

		this.nacimiento = nacim;
		this.institucion = institucion;
		this.rol = rol;
		this.identificacionInstitucion = identificacionInstitucion;
	}
	
	public Medico(String dni, String nombre) {
		super(dni, nombre);
	}

	/*
	 * Constructor, construye un nuevo medico con JSON
	 * 
	 * @param jMedicos datos JsonObject
	 */
	public Medico(JsonObject jMedicos) {
		super(jMedicos);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		try {
			this.nacimiento = sdf.parse(jMedicos.get("nacimiento").getAsString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.institucion = jMedicos.get("institucion").getAsString();
		this.rol = jMedicos.get("rol").getAsString();
		this.identificacionInstitucion = jMedicos.get("identificacionInstitucion").getAsInt();

	}

	/*
	 * Obtener un medico en formato String
	 * 
	 * @return String
	 */
	public String toString() {
		return "dni: " + dni + "\nnombre: " + nombre + "\npassword: " + password + "\ncorreo: " + correo
				+ "\ninstitucion: " + institucion + "\nnacimiento: " + nacimiento + "\nrol: " + rol
				+ "\nidentificacionInstitucion: " + identificacionInstitucion;
	}

	/*
	 * Pasar y obtener un medico en formato JSON
	 * 
	 * @return JsonObject
	 */
	public JsonObject toJson() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		JsonObject j = super.toJson();
		j.addProperty("nacimiento", sdf.format(this.nacimiento));
		j.addProperty("institucion", this.institucion);
		j.addProperty("rol", this.rol);
		j.addProperty("identificacionInstitucion", this.identificacionInstitucion);

		return j;

	}

	/*
	 * Getters
	 */

	public String getInstitucion() {
		return institucion;
	}

	public String getRol() {
		return rol;
	}

	public int getIdentificacionInstitucion() {
		return identificacionInstitucion;
	}

	public Date getNacimiento() {
		return nacimiento;
	}

	/*
	 * Setters
	 */

	public void setInstitucion(String nuevaInstitucion) {
		institucion = nuevaInstitucion;
	}

	public void setRol(String nuevoRol) {
		rol = nuevoRol;
	}

	public void setIdentificacionInstitucion(int nuevoIdentificacionInstitucion) {
		identificacionInstitucion = nuevoIdentificacionInstitucion;
	}

	public void setNacimiento(Date nacimiento) {
		this.nacimiento = nacimiento;
	}

}