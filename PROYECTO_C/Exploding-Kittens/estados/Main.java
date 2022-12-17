package estados;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;



class Main{

    static int DEFUSE = 3;
    static int FAVOR = 3;
    static int SKIP = 3;
    static int ATTACK = 3;
    static int NUM_CARD_TYPES = 3;
    static int DECK_SIZE = 13;
    static int DECK = 7;
    public static HashMap<String,States> states = new HashMap<>();


    
    public static void main(String[] args) {

        for(int d = 0; d <= DEFUSE; d++){
            for(int f = 0 ; f <=FAVOR; f ++){
                for(int s = 0 ; s <= SKIP; s ++){
                    for(int a = 0 ; a <= ATTACK; a ++){
                        for(int deck = 0; deck <= DECK; deck ++){
                            for(int d_u = 0; d_u <= DEFUSE; d_u++){
                                for(int f_u = 0; f_u <= FAVOR; f_u++){
                                    for(int s_u = 0; s_u <= SKIP; s_u++){
                                        for(int a_u = 0; a_u <= ATTACK; a_u++){
                                                Integer[] hand = {d, f, s, a};
                                                Integer[] used = {d_u, f_u, s_u, a_u};                                        
                                                if(compruebaEstado(hand , used , deck)){
                                                    States estado = new States(used, hand, deck);
                                                    states.put(estado.makeKey(),estado);
                                                }                                    
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
       exportaEstados();
    }

    public static void exportaEstados(){
        BufferedWriter bw = null;
        String texto = "";
        try {
            File fichero = new File("estados.txt");
            bw = new BufferedWriter(new FileWriter(fichero)); 
            for(Map.Entry<String,States> state: states.entrySet()){
                texto = States.stateString(state.getKey(), state.getValue());
                bw.write(texto);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }

    public static boolean compruebaEstado(Integer[] hand, Integer[] used, int deck){
        boolean validity = true;
        int counter_cards = 0;
        for(int i = 0; i< NUM_CARD_TYPES ; i++){
            if( hand[i] + used[i] > 3){
                validity = false;
            }
            counter_cards += hand[i] + used[i];
        }
        if(counter_cards + deck > DECK_SIZE){
            validity = false;
        }
        
        return validity;
    }
}
