package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonObject;

/*
 * CLASE PACIENTE
 * Hereda de Persona
 * @see Persona
 */

public class Paciente extends Persona {

	private Date nacimiento;
	private boolean sexo;

	/*
	 * Constructor, construye un nuevo paciente con argumentos
	 * 
	 * @param dni
	 * 
	 * @param nombre
	 * 
	 * @param password
	 * 
	 * @param correo
	 */
	public Paciente(String dni, String nombre, String password, String correo) {
		super(dni, nombre, password, correo);

	}

	/*
	 * Constructor, construye un nuevo paciente con argumentos
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
	 * @param sexo
	 */
	public Paciente(String dni, String nombre, String password, String correo, Date nacim, boolean sexo) {
		super(dni, nombre, password, correo);

		this.nacimiento = nacim;
		this.sexo = sexo;
	}
	
	public Paciente(String dni, String nombre, Date fecha) {
		super(dni, nombre);
		nacimiento = fecha;
	}
	
	public Paciente(String dni, String nombre) {
		super(dni, nombre);
	}

	/*
	 * Constructor, construye un nuevo paciente con JSON
	 * 
	 * @param jPacientes datos JsonObject
	 */
	public Paciente(JsonObject jPacientes) {
		super(jPacientes);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		try {
			this.nacimiento = sdf.parse(jPacientes.get("nacimiento").getAsString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.sexo = jPacientes.get("sexo").getAsBoolean();

	}

	/*
	 * Obtener un paciente en formato String
	 * 
	 * @return String
	 */
	public String toString() {
		return "dni: " + dni + "\nnombre: " + nombre + "\npassword: " + password + "\ncorreo: " + correo
				+ "\nnacimiento: " + nacimiento + "\nsexo: " + sexo;
	}

	/*
	 * Pasar y obtener un paciente en formato JSON
	 * 
	 * @return JsonObject
	 */
	public JsonObject toJson() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		JsonObject j = super.toJson();
		j.addProperty("nacimiento", sdf.format(this.nacimiento));
		j.addProperty("sexo", this.sexo);

		return j;

	}

	/*
	 * Getters
	 */

	public Date getNacimiento() {
		return nacimiento;
	}

	public boolean getSexo() {
		return sexo;
	}

	/*
	 * Setters
	 */

	public void setNacimiento(Date nuevoNacimiento) {
		nacimiento = nuevoNacimiento;
	}

	public void setSexo(boolean nuevoSexo) {
		sexo = nuevoSexo;
	}

}