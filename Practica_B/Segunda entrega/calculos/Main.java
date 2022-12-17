package calculos;

import java.util.HashMap;

class Main {

    final static int PUNTOS = 40;
    final static int RONDAS = 10;
    final static int APUESTA = 4;
    final static int JUGADORES = 5;

    static HashMap<String, Ronda> hash_rondas = new HashMap<>();
    // num_ronda_puntos_salvado_opoX_opoY_opoZ_opoT
    
    public static void main() {
        int salvado = 1; 
        for(int num_ronda=1 ; num_ronda<=RONDAS; num_ronda ++){
            
            if(num_ronda>=5){
                for(int s = 0; s<= salvado; s ++){
                    for(int x=0; x<=APUESTA; x++){
                        for(int y=0; y<=APUESTA;  y++){
                            for(int z=0; z<=APUESTA; z++){
                                for(int t=0; t<=APUESTA; t++){
                                    Ronda r = new Ronda(num_ronda,s,x,y,z,t);
                                    hash_rondas.put(r.toString(),r);
                                }
                            }
                        }
                    }

                }
            }else{
                for(int x=0; x<=APUESTA; x++){
                    for(int y=0; y<=APUESTA;  y++){
                        for(int z=0; z<=APUESTA; z++){
                            for(int t=0; t<=APUESTA; t++){
                                Ronda r = new Ronda(num_ronda,0,x,y,z,t);
                                hash_rondas.put(r.toString(),r);
                            }
                        }
                    }
                }
            }
        }
        System.out.println(hash_rondas.size());
    }
}
