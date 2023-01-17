package classes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import classes.Card.Estado;

public class Player{
    private boolean isHuman;
    private int id;
    private HashMap<Integer, Card> hand = new HashMap<Integer,Card>();
    private HashMap<String, ArrayList<Card>> card_types;
    /*
        Defuse: 
            c1
            c2; 
        Skip: 
            c3
            c4;
        .
        .
        .
    */ 
    
    public Player(int id, boolean onlyIA) {
        this.id = id;
        this.isHuman = false;
        if(this.id==0 && !onlyIA){
            this.isHuman = true;
        }
        

    }

    // GETTERS & SETTERS

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public HashMap<Integer,Card> getHand() {
        return this.hand;
    }

    public void setHand(HashMap<Integer, Card> hand) {
        this.hand = hand;
    }
    public boolean isHuman() {
        return isHuman;
    }

    public void setHuman(boolean isHuman) {
        this.isHuman = isHuman;
    }

    // FUNCTION

    // coge carta sabiendo cuál es la escogida
    public void cogeCarta(Partida p, int card_num){
        Card carta = p.getHashCards().get(card_num);
        ArrayList<Card> deck = p.getDeck();
        deck.remove(carta);
        p.setDeck(deck);
        this.getHand().put(card_num, carta);
        carta.setEstado(Estado.REPARTIDA);
    }


    public void countTypes(HashMap<Integer,Card> hand){
        HashMap<String, ArrayList<Card>> aux = new HashMap<String, ArrayList<Card>>();
        for(Map.Entry<Integer,Card> card: hand.entrySet()){
            if(aux.containsKey(card.getValue().getType())){
                aux.get(card.getValue().getType()).add(card.getValue());
            }else{
                ArrayList<Card> cards = new ArrayList<Card>();
                cards.add(card.getValue());
                aux.put(card.getValue().getType(), cards);

            }

        }
        this.card_types = aux;
    }


    // coge una carta aleatoria (la primera del mazo)
    public int cogeCarta(Partida p){
        ArrayList<Card> deck = p.getDeck();
        if(deck.isEmpty()){
            System.out.println(p.getDeck());
            Card carta = deck.get(0);
            
            if(carta.getType().equals("EXPLODING_KITTEN")){
                return carta.getId();
            }
            this.getHand().put(carta.getId(),carta);
            carta.setEstado(Estado.REPARTIDA);
            deck.remove(carta);
            p.setDeck(deck);
            return carta.getId();

        }else{
            return -1;
        }
    }
    
    public String manoString(){
        String mano = ""; 
        for (Map.Entry<Integer, Card> carta : hand.entrySet()) {
            mano += Integer.toString(carta.getKey())+": "+carta.getValue().getType() + "\n"; 
        }
        return mano;
    }
    public String manoString_sinId(){
        
        String mano = ""; 
        for (Map.Entry<Integer, Card> carta : hand.entrySet()) {
            mano += carta.getValue().getType() + ","; 
        }
        return mano;
        
    }

    public HashMap<String, ArrayList<Card>> getCard_types() {
        return card_types;
    }
   

    
}
