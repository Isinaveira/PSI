package classes;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import classes.Card.Estado;


public class Partida {
    private HashMap<Integer, Player> hashPlayers = new HashMap<Integer, Player>();
    private HashMap<Integer, Card> hashCards = new HashMap<Integer, Card>(); 
    private HashMap<Integer, Turno> turnos = new HashMap<Integer, Turno>();   
    private ArrayList<Card> deck = new ArrayList<Card>();
    private ArrayList<Card> used_cards = new ArrayList<Card>();
    private int winner;
    private int num_hand_cards;
    
    // num_cards: es el numero de cartas por tipo (no incluye exploding kittens y
    // defuse)
    // num_hand_cards: el numero de cartas a repartir en el primer turno
    // card_types: tipos de cartas usadas en la partida
    public Partida(int num_players, int num_cards, int num_hand_cards, HashMap<String, String> card_types) {
        int num_exploding = num_players - 1;
        int num_defuse = num_players + 1;
        this.num_hand_cards = num_hand_cards;

        // Genera las cartas
        for (Map.Entry<String, String> type : card_types.entrySet()) {
            if (type.getKey().equalsIgnoreCase("exploding kitten")) {
                for (int i = 0; i < num_exploding; i++) {
                    Card c = new Card("exploding kitten", type.getValue());
                    hashCards.put(c.getId(), c);
                }
            } else if (type.getKey().equalsIgnoreCase("defuse")) {
                for (int i = 0; i < num_defuse; i++) {
                    Card c = new Card(type.getKey(), type.getValue());
                    hashCards.put(c.getId(), c);
                    
                }
            } else {
                for (int i = 0; i < num_cards; i++) {
                    Card c = new Card(type.getKey(), type.getValue());
                    hashCards.put(c.getId(), c);
                    
                
                }
            }
        }
        
        //Metemos las cartas en el deck (mazo)
        for(int card_num = 0 ; card_num < hashCards.size(); card_num ++){
            deck.add(hashCards.get(card_num));
        }
        for (int i = 0; i < num_players; i++) {
            Player j = new Player(i);
            hashPlayers.put(i, j);
        }
        JuegaPartida();
    }

    // FUNCTIONS
    public void JuegaPartida() {
        reparteCartas();
        int accion_anterior = 0;
        int accion_turno = 0;
        int turn_number = 0; // numero del turno en el que estamos 
        boolean exploding = false;
        while (!exploding) { // mientras no se muera se juega una partida
            Turno t = new Turno();
            turnos.put(turn_number, t);
            
            //Bucle para que ambos jugadores jueguen.
            for(Map.Entry<Integer,Player> player : hashPlayers.entrySet()){
                turno(player.getValue(), turn_number);
            }
            turn_number ++;
        }
    }

    // devuelve la acción resultante de cada turno (atacar, pasar, jugar, no jugar o favor)
    public int turno(Player p, int turn_number){
        String opcion;
        Boolean seguir = false;
        System.out.println("Turno del jugador: " + p.getName());
        System.out.println("Escoge una carta o escribe 'R' para robar una carta ");
        System.out.println("Cartas en mano: " + p.manoString());
        Scanner leer=new Scanner(System.in);
        while(!seguir){
            opcion = leer.next();
            // comprobar que tenga la carta que dice tener en la mano o que va a robar
            
            if(Utilities.isNumeric(opcion)){ // se juega una carta
                if(p.getHand().containsKey(Integer.parseInt(opcion))){
                    Card c = p.getHand().get(Integer.parseInt(opcion));
                    String accion = c.getType();
                    switch(accion){
                        case "ATTACK":
                        break;
                        case "SKIP":
                        break;
                        case "FAVOR":

                        break;

                    }
                }

            }else if(opcion.equalsIgnoreCase("R")){ // se coge una carta
                p.cogeCarta(this);
                
            }else{
                System.out.println("ESCOGE UNA OPCIÓN VÁLIDA");
            }
            
        }
        leer.close();
        return -1;
    }
    
    
    // repartir cartas aleatorias 
    public void reparteCartas() {
        int num_cards = this.hashCards.size();
        reparteDefuse();
        for (int i = 0; i < this.hashPlayers.size(); i++) {
            
            for (int j = 0; j < this.num_hand_cards - 1; j++) {
                int card_number = (int) Math.random() * num_cards + 1;
                // se cogen las cartas que no estén ocupadas ya
                if(hashCards.get(card_number).getEstado() == Card.Estado.LIBRE  && hashCards.get(card_number).getType() != "exploding kitten"){
                    hashPlayers.get(i).getHand().put(card_number, this.gethashCards().get(card_number));
                }
                // coge la mano del jugador i y le añade la carta aleatoria card_number de las
                // cartas de la partida
            } 
            
        }
    }
    
    // al comienzo de la partida se reparte una carta "Defuse" a cada jugador por defecto
    public void reparteDefuse(){
        for( int num_player = 0 ; num_player < hashPlayers.size(); num_player ++){
            for(int card_num=0 ; card_num < hashCards.size(); card_num++){
                Card carta = hashCards.get(card_num);
                if(hashPlayers.get(num_player).getHand().isEmpty()){ 
                    if(carta.getType().equalsIgnoreCase("Defuse") && carta.getEstado() == Estado.LIBRE){
                        hashPlayers.get(num_player).cogeCarta(this, card_num);
                    }
                }
            }        
        }
    }
    
    // GETTERS & SETTERS
    public HashMap<Integer, Player> gethashPlayers() {
        return this.hashPlayers;
    }

    public void sethashPlayers(HashMap<Integer, Player> hashPlayers) {
        this.hashPlayers = hashPlayers;
    }

    public HashMap<Integer, Card> gethashCards() {
        return this.hashCards;
    }

    public void sethashCards(HashMap<Integer, Card> hashCards) {
        this.hashCards = hashCards;
    }

    public int getWinner() {
        return this.winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public HashMap<Integer,Player> getHashPlayers() {
        return this.hashPlayers;
    }

    public void setHashPlayers(HashMap<Integer,Player> hashPlayers) {
        this.hashPlayers = hashPlayers;
    }

    public HashMap<Integer,Card> getHashCards() {
        return this.hashCards;
    }

    public void setHashCards(HashMap<Integer,Card> hashCards) {
        this.hashCards = hashCards;
    }

    public ArrayList<Card> getDeck() {
        return this.deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public ArrayList<Card> getUsed_cards() {
        return this.used_cards;
    }

    public void setUsed_cards(ArrayList<Card> used_cards) {
        this.used_cards = used_cards;
    }

    public int getNum_hand_cards() {
        return this.num_hand_cards;
    }

    public void setNum_hand_cards(int num_hand_cards) {
        this.num_hand_cards = num_hand_cards;
    }

}
