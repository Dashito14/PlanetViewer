package simulator.model;

import java.util.List;

public interface SimulatorObserver {
	
	/**/
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc);

	/*
	 * todos los parámetros se devuelven al carácter base al hacer reset. Esta
	 * función se encargará de notificarlo a todas las interfaces
	 */
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc);

	/*
	 * en el caso de que aparezca un nuevo body en la lista, esta función
	 * actualizará el numero de bodies que aparecen en el panel modificando el
	 * parámetro correspondiente
	 */
	public void onBodyAdded(List<Body> bodies, Body b);

	/*
	 * al producirse un movimiento, este método será el encargado de cambiar dicha
	 * informacíon y actualizarla para que se visualice en pantalla
	 */
	public void onAdvance(List<Body> bodies, double time);

	/*
	 * si el delta-time se ve modificado está funcion deberá actualizar el parámetro
	 * correspondiente en el panel
	 */
	public void onDeltaTimeChanged(double dt);

	/*
	 * si las leyes se ven modificadas, este método será el encargado de actualizar
	 * el parámetro
	 */
	public void onGravityLawsChanged(String gLawsDesc);

}
