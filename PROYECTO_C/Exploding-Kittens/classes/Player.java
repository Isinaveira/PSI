package classes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import classes.Card.Estado;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class Player extends Agent {

    private int id;
    private State state;
    private int myID;
    private AID mainAgent;
    private HashMap<Integer, Card> hand = new HashMap<Integer,Card>();
    private  ACLMessage msg;

    protected void setup() {
        state = State.s0NoConfig;

        //Register in the yellow pages as a player
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Player");
        sd.setName("Game");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        addBehaviour(new Play());
        System.out.println("RandomAgent " + getAID().getName() + " is ready.");

    }

    private class Play extends CyclicBehaviour{
        Random random = new Random();
        @Override
        public void action() {
            //System.out.println(getAID().getName() + ":" + state.name());
            msg = blockingReceive();
            if (msg != null) {
                //System.out.println(getAID().getName() + " received " + msg.getContent() + " from " + msg.getSender().getName()); //DELETEME
                //-------- Agent logic
                switch (state) {
                    case s0NoConfig:
                        //If INFORM Id#_#_,_,_,_ PROCESS SETUP --> go to state 1
                        //Else ERROR
                        if (msg.getContent().startsWith("Id#") && msg.getPerformative() == ACLMessage.INFORM) {
                            boolean parametersUpdated = false;
                            try {
                                parametersUpdated = validateSetupMessage(msg);
                            } catch (NumberFormatException e) {
                                //System.out.println(getAID().getName() + ":" + state.name() + " - Bad message");
                            }
                            if (parametersUpdated) state = State.s1AwaitingGame;
                                System.out.println("He recibio ID");
                        } else {
                            System.out.println(getAID().getName() + ":" + state.name() + " - Unexpected message");
                        }
                        break;
                    case s1AwaitingGame:
                        //If INFORM NEWGAME#_,_ PROCESS NEWGAME --> go to state 2
                        //If INFORM Id#_#_,_,_,_ PROCESS SETUP --> stay at s1
                        //Else ERROR
                        //TODO I probably should check if the new game message comes from the main agent who sent the parameters
                        if (msg.getPerformative() == ACLMessage.INFORM) {
                            if (msg.getContent().startsWith("Id#")) { //Game settings updated
                                try {
                                    validateSetupMessage(msg);
                                } catch (NumberFormatException e) {
                                    //System.out.println(getAID().getName() + ":" + state.name() + " - Bad message");
                                }
                            } else if (msg.getContent().startsWith("NewGame#")) {
                                boolean gameStarted = false;
                                try {
                                    gameStarted = validateNewGame(msg.getContent());
                                } catch (NumberFormatException e) {
                                    //System.out.println(getAID().getName() + ":" + state.name() + " - Bad message");
                                }
                                if (gameStarted) state = State.s2Round;
                            }
                        } else {
                            System.out.println(getAID().getName() + ":" + state.name() + " - Unexpected message");
                        }
                        break;
                    case s2Round:
                        //If REQUEST POSITION --> INFORM POSITION --> go to state 3
                        //If INFORM CHANGED stay at state 2
                        //If INFORM ENDGAME go to state 1
                        //Else error
                        if (msg.getPerformative() == ACLMessage.REQUEST && msg.getContent().startsWith("Action")) {
                            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                            msg.addReceiver(mainAgent);
                            //Cantidad que apuesta el jugador en cada ronda
                            int importe = random.nextInt(5);
                            E_partida = E_partida-importe;
                            msg.setContent("Action#" + importe);
                            //System.out.println(getAID().getName() + " sent " + msg.getContent());
                            send(msg);
                            state = State.s3AwaitingResult;
                        } else if (msg.getPerformative() == ACLMessage.INFORM && msg.getContent().startsWith("Changed#")) {
                            // Process changed message, in this case nothing
                        } else if (msg.getPerformative() == ACLMessage.INFORM && msg.getContent().startsWith("GameOver")) {
                            E_partida = E;
                            state = State.s1AwaitingGame;
                        } else {
                            //System.out.println(getAID().getName() + ":" + state.name() + " - Unexpected message:" + msg.getContent());
                        }
                        break;
                    case s3AwaitingResult:
                        //If INFORM RESULTS --> go to state 2
                        //Else error
                        if (msg.getPerformative() == ACLMessage.INFORM && msg.getContent().startsWith("Results#")) {
                            //Process results
                            state = State.s2Round;
                        } else {
                            //System.out.println(getAID().getName() + ":" + state.name() + " - Unexpected message");
                        }
                        break;
                }
            }
        }
    }
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
    private enum State {
        s0NoConfig, s1AwaitingGame, s2Round, s3AwaitingResult
    }

    private boolean validateSetupMessage(ACLMessage msg) throws NumberFormatException {
        int tN, tE, tR, tNumGames, tMyId;
        float tP;
        String msgContent = msg.getContent();

        String[] contentSplit = msgContent.split("#");
        if (contentSplit.length != 3) return false;
        if (!contentSplit[0].equals("Id")) return false;
        tMyId = Integer.parseInt(contentSplit[1]);

        String[] parametersSplit = contentSplit[2].split(",");
        if (parametersSplit.length != 5) return false;
        tN = Integer.parseInt(parametersSplit[0]);
        tE = Integer.parseInt(parametersSplit[1]);
        tR = Integer.parseInt(parametersSplit[2]);
        tP = Float.parseFloat(parametersSplit[3]);
        tNumGames = Integer.parseInt(parametersSplit[4]);

        //At this point everything should be fine, updating class variables
        mainAgent = msg.getSender();
        
        return true;
    }

    /**
     * Processes the contents of the New Game message
     * @param msgContent Content of the message
     * @return true if the message is valid
     */
    public boolean validateNewGame(String msgContent) {
        boolean valid = false;
        String[] contentSplit = msgContent.split("#");
        if (contentSplit.length != 2) return false;
        if (!contentSplit[0].equals("NewGame")) return false;
        String[] idSplit = contentSplit[1].split(",");
        if (idSplit.length != 5) return false;
        int[] ids = new int[idSplit.length];
        for(int i=0; i<idSplit.length; i++){
            ids[i]=Integer.parseInt(idSplit[i]);
            if(ids[i] == myId) valid = true;
        }
        return valid;
    }

}
