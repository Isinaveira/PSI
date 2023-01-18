package classes;
import java.util.ArrayList;


public class Main{

    public static void main(String[] args) {
        LearningTools LT1 = new LearningTools();
        LearningTools LT2 = new LearningTools();
        ArrayList<String> Card_types = new ArrayList<String>();
        Card_types.add("EXPLODING_KITTEN");
        Card_types.add("DEFUSE");
        Card_types.add("SKIP");
        Card_types.add("ATTACK");
        Card_types.add("FAVOR");
        int contador_0 = 0;
        int contador_1 = 0;
        for(int i = 0; i<100000; i++){
            System.out.println(i);
            Partida p = new Partida( 2, 3, 3, Card_types , true);
            if(i!=0){
                p.getHashPlayers().get(0).setLT(LT1);
                p.getHashPlayers().get(1).setLT(LT2);
            }
            p.juegaPartida();
            int ganador = p.getWinner();
            int otherPlayer = ganador == 0 ? 1 : 0;
            if(ganador == 0 ){
                contador_0 ++;
            }else{
                contador_1 ++;
            }
            if(!p.getHashPlayers().get(ganador).isHuman()){
                p.getHashPlayers().get(ganador).LT.updateQlearning(20);
            }else{
                p.getHashPlayers().get(otherPlayer).LT.updateQlearning(-20);
            }
        }
        System.out.println("Partidas ganadas por el jugador 0: "+contador_0);
        System.out.println(LT1.hash_StateActions.size());
        System.out.println(LT2.hash_StateActions.size());
        System.out.println("Partidas ganadas por el jugador 1: "+ contador_1);

        //p.resultados();
    }



}