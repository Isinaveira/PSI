package classes;


import java.util.HashMap;

public class Jugada {

    private int id_jugador;
    private String jugada; // selección para una jugada 
    private String mano;
    private int  cartas_restantes;
    private HashMap<String, Integer> cartas_usadas = new HashMap<>();

    /* 
    public enum ACCIONES {
        ATTACK, SKIP, FAVOR, GET_CARD, NO_PLAY  // Hay 4 acciones:
        //    - ATTACK y SKIP: implican no coger carta,
        //    - FAVOR: una coger al oponente y segui el turno
        //    - GET_CARD: Coger una carta 
        //    
    };
    */
    
    public Jugada(int id_jugador, String jugada,  int cartas_restantes, HashMap<String,Integer> cartas_usadas) {
        this.id_jugador = id_jugador;
        this.jugada = jugada;
        this.cartas_restantes = cartas_restantes;
        this.cartas_usadas = cartas_usadas;
        
    }
    public Jugada(int id_jugador, String jugada){
        this.id_jugador = id_jugador;
        this.jugada = jugada;
    }

    public int getId_jugador() {
        return this.id_jugador;
    }

    public void setId_jugador(int id_jugador) {
        this.id_jugador = id_jugador;
    }

    public String getJugada() {
        return this.jugada;
    }

    public void setJugada(String jugada) {
        this.jugada = jugada;
    }

    public String getMano() {
        return this.mano;
    }

    public void setMano(String mano) {
        this.mano = mano;
    }

    public int getCartas_restantes() {
        return this.cartas_restantes;
    }

    public void setCartas_restantes(int cartas_restantes) {
        this.cartas_restantes = cartas_restantes;
    }

    public HashMap<String,Integer> getCartas_usadas() {
        return this.cartas_usadas;
    }

    public void setCartas_usadas(HashMap<String,Integer> cartas_usadas) {
        this.cartas_usadas = cartas_usadas;
    }

    public Jugada id_jugador(int id_jugador) {
        setId_jugador(id_jugador);
        return this;
    }

    public Jugada jugada(String jugada) {
        setJugada(jugada);
        return this;
    }

    public Jugada mano(String mano) {
        setMano(mano);
        return this;
    }

    public Jugada cartas_restantes(int cartas_restantes) {
        setCartas_restantes(cartas_restantes);
        return this;
    }

    public Jugada cartas_usadas(HashMap<String,Integer> cartas_usadas) {
        setCartas_usadas(cartas_usadas);
        return this;
    }

    @Override
    public String toString() {
        return "{\n" +
            " id_jugador='" + getId_jugador() + "'\n" +
            ", jugada='" + getJugada() + "'\n" +
            ", mano='" + getMano() + "'\n" +
            ", cartas_restantes='" + getCartas_restantes() + "'\n" +
            ", cartas_usadas='" + getCartas_usadas() + "'\n" +
            "}\n";
    }

    
    
}
