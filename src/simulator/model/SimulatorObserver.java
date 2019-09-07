package simulator.model;

import java.util.List;

public interface SimulatorObserver {
	
	/**/
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc);

	/*
	 * todos los par�metros se devuelven al car�cter base al hacer reset. Esta
	 * funci�n se encargar� de notificarlo a todas las interfaces
	 */
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc);

	/*
	 * en el caso de que aparezca un nuevo body en la lista, esta funci�n
	 * actualizar� el numero de bodies que aparecen en el panel modificando el
	 * par�metro correspondiente
	 */
	public void onBodyAdded(List<Body> bodies, Body b);

	/*
	 * al producirse un movimiento, este m�todo ser� el encargado de cambiar dicha
	 * informac�on y actualizarla para que se visualice en pantalla
	 */
	public void onAdvance(List<Body> bodies, double time);

	/*
	 * si el delta-time se ve modificado est� funcion deber� actualizar el par�metro
	 * correspondiente en el panel
	 */
	public void onDeltaTimeChanged(double dt);

	/*
	 * si las leyes se ven modificadas, este m�todo ser� el encargado de actualizar
	 * el par�metro
	 */
	public void onGravityLawsChanged(String gLawsDesc);

}
