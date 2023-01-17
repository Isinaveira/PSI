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
        Partida p = new Partida( 2, 3, 3, Card_types ,id, true);
        p.juegaPartida();
        int ganador = p.getWinner();
        if(!p.getHashPlayers().get(ganador).isHuman()){
            p.LT.updateQlearning(20);
        }else{
            p.LT.updateQlearning(-10);
        }
        //p.resultados();
    }



}