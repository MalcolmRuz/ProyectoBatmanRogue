
package Controlador;

import Modelo.Heroe;

/**
 *
 * @author artor
 */
public class ProgresionMapa {
    private int pisoActual;
    private final int PISO_MAX = 30;
    private Encuentro encuentro;
    private Batalla batalla;
    private Heroe heroe;
    

    public ProgresionMapa() {
    }

    public ProgresionMapa(int pisoActual) {
        this.pisoActual = pisoActual;
    }

   
    
    public int getPisoActual() {
        return pisoActual;
    }

    public void setPisoActual(int pisoActual) {
        this.pisoActual = pisoActual;
    }
    
    public void progresion(){
        //batalla.batalla(heroe,encuentro.generarVillano() );
        //aqui implementar la subida de pisos, los encuentros aleatorios.
        //if (piso <= PISO_MAX){
        
    //villanoActual = encuentro.generarVillano(){}
        //implementrar metodo batalla(villanoActual)
        //}else{victoria()}  <------------------------- implemenentar metodo para pantalla de victoria ya que se superó el piso Max
        
    }
    
}
