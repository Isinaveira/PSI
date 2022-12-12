package classes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import classes.Card.Estado;

public class Player {

    private int id;
    
    private HashMap<Integer, Card> hand = new HashMap<Integer,Card>();

    public Player(int id) {

        this.id = id;
        
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
}
