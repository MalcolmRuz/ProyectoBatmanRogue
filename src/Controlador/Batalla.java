package Controlador;
import Modelo.Heroe;
import Modelo.Villano;
//probando weas
/**
 *
 * @author artor
 */
public class Batalla {
    private Heroe heroe;
    private Villano villano;
    private boolean turnoHeroe;
    private boolean batallaTerminada;
    

    public Batalla(Heroe heroe, Villano villano, boolean turnoHeroe, boolean batallaTerminada) {
        this.heroe = heroe;
        this.villano = villano;
        this.turnoHeroe = turnoHeroe;
        this.batallaTerminada = batallaTerminada;
    }
    
    public void batalla(Heroe heroe, Villano villano){
       boolean turnoHeroe = true;
       boolean batallaTerminada = false;
       while (!batallaTerminada){
       if(turnoHeroe)
       {//int numeroHabilidad = obtenerHabilidadSeleccionada();                 <----------------------------------------------Esto hay que generarlo desde los botones de la interfaz grafica. por
           switch (numeroHabilidad) { //<--------------------------------------------------generarlo desde la interfaz grafica
               case 1:
                   heroe.atacarFisico(villano);
                   break;
               case 2 : 
                   heroe.ataqueEspecial(villano);
                   break;
               case 3 : 
                   heroe.enGuardia();
                   break;
               default:
                   continue;
           }
            if (villano.getHp() == 0){
                batallaTerminada = true;
                break;
            
            }
            turnoHeroe = false; // cambiar turno
            //---------------------Turno villano------------------------------------------------
           }else{ 
            double porcentajeVida = (double) villano.getHp() / villano.getHpMax();
            boolean accionvalida = false;
            if(porcentajeVida > 0.6){
                if (Math.random() > 0.5) {
                    accionvalida = villano.ataqueEspecial(heroe);
                    
                    if (!accionvalida){
                        villano.atacarFisico(heroe);}
                    else {villano.ataqueEspecial(heroe);
                    }
                }else{
                villano.atacarFisico(heroe);}
            
            }else{
            if(Math.random()<0.6){
            villano.enGuardia();}
            
            else if(Math.random() < 0.8){
            villano.atacarFisico(heroe);}
            else{
                accionvalida = villano.ataqueEspecial(heroe);
                if (!accionvalida){
                        villano.atacarFisico(heroe);}
                    else {villano.ataqueEspecial(heroe);
            }
            
            
            }

           if (heroe.getHp() <= 0) {
                heroe.setHp(0);
                batallaTerminada = true;
                break;}
           turnoHeroe = true;
       
       }
       
       
       
       }
     }
       
   }
}
    
    



