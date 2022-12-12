package classes;
import java.util.ArrayList;

public class Main{

    public static void main(String[] args) {
        
        ArrayList<String> Card_types = new ArrayList<String>();
        Card_types.add("EXPLODING_KITTEN");
        Card_types.add("DEFUSE");
        Card_types.add("SKIP");
        Card_types.add("ATTACK");
        Card_types.add("FAVOR");
        int id = 0;
        Partida p = new Partida( 2, 3, 3, Card_types ,id);
        p.juegaPartida();
        //p.resultados();
    }



}