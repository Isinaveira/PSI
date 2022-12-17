package estados;
import java.util.HashMap;
import java.util.Map;

public class States {
    
    private HashMap<String,Integer> used = new HashMap<String,Integer>();
    private HashMap<String,Integer> hand = new HashMap<String,Integer>();
    private HashMap<String,Float> actions = new HashMap<String, Float>();
    private String[] cards = {"Defuse", "Favor", "Attack", "Skip"};
    private String[] action_types = {"","Favor", "Attack", "Skip", "Draw"};
    private int deck; 



    public States(Integer[] used, Integer[] hand, int deck){
        this.deck  = deck;
        
        for(int i = 0; i < used.length ; i ++){
            this.used.put(this.cards[i], used[i]);
        }
        
        //se inicia a 1 ya que siempre tienes 1 opcion que es robar carta 
        float cuenta_acciones = 1;
        for(int i = 0; i < hand.length ; i ++){
            this.hand.put(this.cards[i], hand[i]);
            if(hand[i] != 0 && i != 0){
                cuenta_acciones ++;
            }
        }
        //coge lo que tienes en la mano , y calcula 1/(num_posibles_jugadas)
        float probabilidad_accion = 1/cuenta_acciones;
        this.actions.put("Draw", probabilidad_accion);
        for(int i = 1; i < hand.length ; i ++){
            if(hand[i] != 0){
                this.actions.put(action_types[i], probabilidad_accion);
            }
        }

    }

    public String makeKey(){
        String key = "";
        for(int i=0; i< this.hand.size(); i++){
            key += Integer.toString(this.hand.get(this.cards[i]));
        }

        key += Integer.toString(this.deck);
 
        for(int i=0; i< this.used.size(); i++){
            key += Integer.toString(this.used.get(this.cards[i]));
        }

        return key;
    }

    public static String stateString(String key, States state){
        String state_String = key+ ":\n \tHand:\n";

        for(Map.Entry<String, Integer> card : state.getHand().entrySet()){
            state_String += "\t\t" + card.getKey() + ":" + card.getValue() + "\n";
        }
        state_String += "\tDeck:\n"+"\t\t"+Integer.toString(state.deck)+"\n";
        state_String += "\tUsed:\n";
        for(Map.Entry<String, Integer> used_card : state.getUsed().entrySet()){
            state_String += "\t\t" + used_card.getKey() + ":" + used_card.getValue() + "\n";
        }
        state_String += "\tActions:\n";
        for(Map.Entry<String, Float> action : state.getActions().entrySet()){
            state_String += "\t\t" + action.getKey() + ":" + action.getValue() + "\n";
        }

        
        return state_String;
    }
    public HashMap<String,Integer> getUsed() {
        return this.used;
    }

    public void setUsed(HashMap<String,Integer> used) {
        this.used = used;
    }

    public HashMap<String,Integer> getHand() {
        return this.hand;
    }

    public void setHand(HashMap<String,Integer> hand) {
        this.hand = hand;
    }

    public HashMap<String,Float> getActions() {
        return this.actions;
    }

    public void setActions(HashMap<String,Float> actions) {
        this.actions = actions;
    }

    public String[] getCards() {
        return this.cards;
    }

    public void setCards(String[] cards) {
        this.cards = cards;
    }

    public String[] getAction_types() {
        return this.action_types;
    }

    public void setAction_types(String[] action_types) {
        this.action_types = action_types;
    }

    public int getDeck() {
        return this.deck;
    }

    public void setDeck(int deck) {
        this.deck = deck;
    }

}
