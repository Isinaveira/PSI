package classes;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import classes.Card.Estado;


public class Partida {
    private HashMap<Integer, Player> hashPlayers = new HashMap<Integer, Player>();
    private HashMap<Integer, Card> hashCards = new HashMap<Integer, Card>(); 
    private HashMap<Integer, ArrayList<Jugada> > turnos = new HashMap<Integer, ArrayList<Jugada> >();   
    private ArrayList<Card> deck = new ArrayList<Card>();
    private HashMap<String, Integer> used_cards = new HashMap<String, Integer>();
    private int winner;
    private int num_hand_cards;
    private int number_of_turns;
    private int id;
    private Card exploding;
    // num_cards: es el numero de cartas por tipo (no incluye exploding kittens y
    // defuse)
    // num_hand_cards: el numero de cartas a repartir en el primer turno
    // card_types: tipos de cartas usadas en la partida
    public Partida(int num_players, int num_cards, int num_hand_cards, ArrayList<String> card_types, int id) {
        int num_exploding = num_players - 1;
        int num_defuse = num_players + 1;
        this.num_hand_cards = num_hand_cards;
        this.number_of_turns = 1; 
        this.id = id;

        // Genera las cartas
        for (String type : card_types) {
            if (type.equalsIgnoreCase("EXPLODING_KITTEN")) {
                for (int i = 0; i < num_exploding; i++) {
                 this.exploding = new Card("EXPLODING_KITTEN");
                    
                }
            } else if (type.equalsIgnoreCase("DEFUSE")) {
                for (int i = 0; i < num_defuse; i++) {
                    Card c = new Card(type);
                    this.hashCards.put(c.getId(), c);
                    
                }
            } else {
                for (int i = 0; i < num_cards; i++) {
                    Card c = new Card(type);
                    this.hashCards.put(c.getId(), c);
                    
                
                }
            }
        }
        //INICIALIZAMOS EL HASH DE CARTAS USADAS A 0
        this.used_cards.put("DEFUSE", 0);
        this.used_cards.put("SKIP", 0);
        this.used_cards.put("ATTACK", 0);
        this.used_cards.put("FAVOR", 0);
        
        //Metemos las cartas en el deck (mazo)
        System.out.println("Tamaño hashCards: "+hashCards.size());
        for(Map.Entry<Integer,Card> card: this.hashCards.entrySet()){
            this.deck.add(card.getValue());
        }
        for (int i = 0; i < num_players; i++) {
            Player j = new Player(i);
            hashPlayers.put(i, j);
        }

        
        
    }

    // FUNCTIONS
    public void juegaPartida() {
        reparteCartas();
        int turn_number = 1; // numero del turno en el que estamos 
        boolean exploding = false;
        int num_turns_jugador = 1; // numero de turnos que tiene que jugar un jugador
        int player_id = 0;
        ArrayList<Jugada> jugadas  = new ArrayList<>();
        Boolean attack = false; 
        this.deck.add(this.exploding);
        baraja_bomba();
         
        while (!exploding) { // mientras no se muera se juega una partida
            Player player = hashPlayers.get(player_id);
            num_turns_jugador = this.getNumber_of_turns();
            while( num_turns_jugador > 0){
                System.out.println("--------------------------------------------------------------");
                System.out.println("Turno general: "+turn_number);
                System.out.println("Numero de turnos del jugador: "+num_turns_jugador);
                jugadas = turno(player);
                System.out.println("EL JUGADOR "+player_id+" HA JUGADO: "+jugadas.get(jugadas.size()-1).getJugada());
                turnos.put(turn_number,jugadas);
                Jugada ultima = jugadas.get(jugadas.size()-1);
                if (ultima.getJugada().equalsIgnoreCase("MUERTE")){
                    exploding = true;
                    this.setWinner(winner = (player_id == 0)? 1 : 0);
                }else if( ultima.getJugada().equalsIgnoreCase("ATTACK")){
                    attack = true;
                    num_turns_jugador = 0;
                   // this.setNumber_of_turns(this.getNumber_of_turns() + 1);
                }
                turn_number ++;
                num_turns_jugador --;
            }
            if(num_turns_jugador <= 0 && !attack){
                player_id = (player_id == 0)? 1 : 0;
                this.setNumber_of_turns(1);
            }else if(num_turns_jugador <= 0 && attack){
                player_id = (player_id == 0)? 1 : 0;
                this.setNumber_of_turns(this.getNumber_of_turns() + 1);
                attack = false;
            }
        }

    }

    // devuelve la acción resultante de cada turno (atacar, pasar, jugar, no jugar o favor)
    public ArrayList<Jugada> turno(Player p){
        
        ArrayList<Jugada> jugadas = new ArrayList<>();
        String opcion="";  
        Boolean end_turn = false;
        System.out.println("-------------------------------------------------------");
        System.out.println("Turno del jugador: " + p.getId());
        System.out.println("Cartas en el deck: " + this.getDeck().size());
        System.out.println("Probabilidad de bomba " + 1/this.getDeck().size());
        Scanner leer=new Scanner(System.in);
        while(!end_turn){
            System.out.println("Escoge una carta o escribe 'R' para robar una carta ");
            System.out.println("Cartas en mano: \n" + p.manoString());
            System.out.println("R) Robar");
            opcion = leer.nextLine();
            // comprobar que tenga la carta que dice tener en la mano o que va a robar    
            Jugada j;
            if(Utilities.isNumeric(opcion)){ // se juega una carta
                if(p.getHand().containsKey(Integer.parseInt(opcion))){
                    Card c = p.getHand().get(Integer.parseInt(opcion));
                    String accion = c.getType();
                    
                    switch(accion){
                        case "ATTACK":
                            j = new Jugada(p.getId(), accion, this.getDeck().size(), this.getUsed_cards());
                            jugadas.add(j);
                            this.usar_carta(p, c);
                            end_turn = true;
                            break;

        
                        case "SKIP":
                            j = new Jugada(p.getId(), accion, this.getDeck().size(), this.getUsed_cards());
                            jugadas.add(j);
                            this.usar_carta(p, c);
                            end_turn = true;
                            break;
        
                        case "FAVOR":
                            j = new Jugada(p.getId(), accion, this.getDeck().size(), this.getUsed_cards());
                            jugadas.add(j);
                            this.usar_carta(p, c);
                            giveMeFavor(p.getId());
                            break;
                        default:
                            System.out.println("NO PUEDES USAR EL DEFUSE HASTA QUE NO EXPLOTES");
                    }
                }
            }else if(opcion.equalsIgnoreCase("R")){ // se coge una carta
                j = new Jugada(p.getId(), "GET_CARD", this.getDeck().size(), this.getUsed_cards());
                jugadas.add(j);
                Card c = this.deck.get(0);
                if(!c.getType().equalsIgnoreCase("EXPLODING_KITTEN")){
                    this.deck.remove(0);
                    this.deck.removeAll(Collections.singleton(null));
                    p.getHand().put(c.getId(),c);
                }else{
                    //HA SALIDO UNA BOMBA
                        System.out.println("HA EXPLOTADO!!!!!!!!!!!!!");
                        Card defuse = null;
                        Boolean used_defuse = false;
                        Boolean muerte = true;
                        HashMap<Integer,Card> hand = p.getHand();
    
                        for(Map.Entry<Integer,Card> card : hand.entrySet()){ 
                            //busco un Defuse
                            if(card.getValue().getType().equalsIgnoreCase("DEFUSE") && !used_defuse){ 
                                defuse = card.getValue();
                                this.baraja_bomba();
                                muerte = false;
                                Jugada d = new Jugada(p.getId(), "DEFUSE", this.getDeck().size(), this.getUsed_cards());
                                jugadas.add(d);
                                used_defuse = true;
                            }
                        }
                        if(used_defuse){
                            this.usar_carta(p, defuse);
                        }
                        if(muerte){
                            Jugada m = new Jugada(p.getId(), "MUERTE");
                            jugadas.add(m);
                        }
                    
                }
                end_turn = true;
                    
            }else{
                System.out.println("ESCOGE UNA OPCION VALIDA");
            }
            
        }
        

        return jugadas;
    }
    
    public String giveMeFavor(int id){
        ArrayList<Card> oponent_hand = new ArrayList<>();
        int id_oponent  = (id == 0)? 1 : 0;
        Player oponent = hashPlayers.get(id_oponent);
        if(oponent.getHand().size()!=0){
            for(Map.Entry<Integer,Card> c : oponent.getHand().entrySet()){
                oponent_hand.add(c.getValue());
            }
            Collections.shuffle(oponent_hand);
            Card carta_robada = oponent_hand.get(0);
            oponent.getHand().remove(carta_robada.getId());
            hashPlayers.get(id).getHand().put(carta_robada.getId(), carta_robada);
            return "";
        }
        return "El oponente no tiene cartas!";
    }
    
    // repartir cartas aleatorias 
    public void reparteCartas() {
        int num_cards = this.hashCards.size();
        System.out.println(num_cards);
        reparteDefuse();
        for (int i = 0; i < this.hashPlayers.size(); i++) {
            System.out.println("Numero de cartas a coger: "+num_hand_cards);
            System.out.println("Jugador: "+this.hashPlayers.get(i).getId());
            
            while (this.hashPlayers.get(i).getHand().size() != num_hand_cards) {
                int card_number = (int) (Math.random() * num_cards + 2);
                System.out.println(card_number);
                // se cogen las cartas que no estén ocupadas ya
                
                if(this.hashCards.get(card_number).getEstado() == Card.Estado.LIBRE  && !this.hashCards.get(card_number).getType().equalsIgnoreCase("EXPLODING_KITTEN")){
                    System.out.println("Carta repartida: "+this.gethashCards().get(card_number).getType());
                    this.hashPlayers.get(i).getHand().put(card_number, this.gethashCards().get(card_number));
                    this.hashCards.get(card_number).setEstado(Estado.REPARTIDA);
                    this.getDeck().remove(hashCards.get(card_number));
                    
                }
                // coge la mano del jugador i y le añade la carta aleatoria card_number de las
                // cartas de la partida
            } 
            
        }
        
        
    }
    public void usar_carta(Player p ,Card c){
        // eliminar la carta de la mano del jugador y añadirla a el mazo de cartas usadas (used_cards)
        p.getHand().remove(c.getId()); 
        int valor  = this.getUsed_cards().get(c.getType());
        this.getUsed_cards().put(c.getType(), valor ++);
        c.setEstado(Estado.USADA);
         
    }

    // Mostrar los resultados una vez finalice la partida
    public void resultados(){
        FileWriter file = null;
        PrintWriter pw = null;
        String path = "../resultados/resultados" + Integer.toString(this.getId()) + ".txt";
        try{
            file = new FileWriter(path);
            pw = new PrintWriter(file);
            pw.println("############# WINNER #############");
            pw.println("PLAYER: "+ Integer.toString(this.getWinner()));
            pw.println("############# LIST_OF_TURNS #############");
            for (Map.Entry<Integer, ArrayList<Jugada>> turno : this.getTurnos().entrySet()){
                pw.println("-------- TURN_"+ turno.getKey() + " --------");
                for(Jugada jugada : turno.getValue()){
                    pw.println(jugada.toString());
                }
            } 

        }catch(Exception e){

        }finally{
        
        } 
    }
    // tras usar el defuse se añade la bomba al mazo y se baraja para que esté en una posición aleatoria
    public void baraja_bomba(){
        Collections.shuffle(this.getDeck());
    }
    // al comienzo de la partida se reparte una carta "Defuse" a cada jugador por defecto
    public void reparteDefuse(){
        for( int num_player = 0 ; num_player < hashPlayers.size(); num_player ++){
            for(int card_num=2 ; card_num < hashCards.size()+1; card_num++){
                Card carta = hashCards.get(card_num);
                if(hashPlayers.get(num_player).getHand().isEmpty()){ 
                    if(carta.getType().equals("DEFUSE") && carta.getEstado() == Estado.LIBRE){
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

    public HashMap<String, Integer> getUsed_cards() {
        return this.used_cards;
    }

    public void setUsed_cards(HashMap<String, Integer> used_cards) {
        this.used_cards = used_cards;
    }

    public int getNum_hand_cards() {
        return this.num_hand_cards;
    }

    public void setNum_hand_cards(int num_hand_cards) {
        this.num_hand_cards = num_hand_cards;
    }
    

    public HashMap<Integer,ArrayList<Jugada>> getTurnos() {
        return this.turnos;
    }

    public void setTurnos(HashMap<Integer,ArrayList<Jugada>> turnos) {
        this.turnos = turnos;
    }

    public int getNumber_of_turns() {
        return this.number_of_turns;
    }

    public void setNumber_of_turns(int number_of_turns) {
        this.number_of_turns = number_of_turns;
    }
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
    

}
