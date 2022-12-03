package classes;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import classes.Card.Estado;

public class Player {

    private int id;
    private String name;
    private HashMap<Integer, Card> hand = new HashMap<Integer,Card>();

    public Player(int id) {
        String name = "";
        System.out.println("Introduce un nombre de usuario: ");
        Scanner sc = new Scanner(System.in);
        name = sc.nextLine();
        sc.close();

        this.id = id;
        this.name = name;
    }

    // GETTERS & SETTERS

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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
        p.getDeck().remove(carta);
        this.getHand().put(card_num, carta);
        carta.setEstado(Estado.REPARTIDA);
    }
    
    // coge una carta aleatoria (la primera del mazo)
    public void cogeCarta(Partida p){
        Card carta = p.getDeck().get(0);
        p.getDeck().remove(carta);
        this.getHand().put(carta.getId(),carta);
        carta.setEstado(Estado.REPARTIDA);
    }
    
    public String manoString(){
        String mano = ""; 
        for (Map.Entry<Integer, Card> carta : hand.entrySet()) {
            mano += Integer.toString(carta.getKey())+": "+carta.getValue().getType() + "\n"; 
        }
        return mano;
    }
}
