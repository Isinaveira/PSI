package classes;

import java.util.HashMap;

public class Turno {
    public enum ACCIONES {
        ATTACK, SKIP, FAVOR, GET_CARD, NO_PLAY  // Hay 4 acciones:
        //    - ATTACK y SKIP: implican no coger carta,
        //    - FAVOR: una coger al oponente y segui el turno
        //    - GET_CARD: Coger una carta 
        //    - NO_PLAY: cuando se ha ejecutado un ATTACK en el turno previo
        //               y el oponente tiene un turno extra
    };
    private HashMap<Integer, ACCIONES> acciones = new HashMap<Integer, ACCIONES>();
    
    public Turno(){
    }

    public HashMap<Integer,ACCIONES> getAcciones() {
        return this.acciones;
    }

    public void setAcciones(HashMap<Integer,ACCIONES> acciones) {
        this.acciones = acciones;
    }
    
}
