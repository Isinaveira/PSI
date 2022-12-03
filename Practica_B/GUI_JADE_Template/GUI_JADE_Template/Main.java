import java.lang.Math;
import java.util.*;

public class Main {

    public static void main(String args[]) {
        HashMap<Integer, ArrayList<int []>> Hash_Partidas = new HashMap<Integer, ArrayList< int[]>>();        
        int R = 5;
        int N = 3;
        int E = 40;
        int Num_games = 4;
        
        for(int i=0; i< N; i++){
            ArrayList<int []> List_partidas = new ArrayList<int []>();
            for(int j=0;j<Num_games; j++){
                E=40;
                int apuestas_rondas[] = new int[R+1];
                for(int k=0; k<R; k++){
                    int apuesta = (int) Math.random()*4;
                    apuestas_rondas[k] = apuesta;
                }
                List_partidas.add(apuestas_rondas);
            }
            Hash_Partidas.put(i,List_partidas);
        }
        for(int i=0; i<Hash_Partidas.size(); i++){
            System.out.println("Jugador"+ i + Hash_Partidas.get(i).toString());
        }
    }
}