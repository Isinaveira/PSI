
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class MainAgent extends Agent {

    private GUI gui;
    private ArrayList<AID> playerAgents = new ArrayList<AID>();
    private GameParametersStruct parameters = new GameParametersStruct();
    private HashMap<String,PlayerInformation> hash_pAgents = new HashMap<String, PlayerInformation>();

    @Override
    protected void setup() {
        gui = new GUI(this);
        System.setOut(new PrintStream(gui.getLoggingOutputStream()));

        updatePlayers();
        gui.logLine("Agent " + getAID().getName() + " is ready.");
    }

    public int updatePlayers() {
        gui.logLine("Updating player list");
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Player");
        template.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(this, template);
            if (result.length > 0) {
                gui.logLine("Found " + result.length + " players");
            }
            //playerAgents = new AID[result.length];
            
            for (int i = 0; i < result.length; i++) {
                 
                 playerAgents.add(result[i].getName());
            }
        } catch (FIPAException fe) {
            gui.logLine(fe.getMessage());
        }
        //Provisional
        String[] playerNames = new String[playerAgents.size()];
        int i=0;
        for (AID aid_player : playerAgents) {
            playerNames[i] = aid_player.getName();
            i++;
        }
        gui.setPlayersUI(playerNames);
        return 0;
    }
    
    public int newGame() {
        addBehaviour(new GameManager());
        return 0;
    }

    /**
     * In this behavior this agent manages the course of a match during all the
     * rounds.
     */
    private class GameManager extends SimpleBehaviour {
        @Override
        public void action() {
            //Assign the IDs
            ArrayList<PlayerInformation> players = new ArrayList<>();
            int lastId = 0;
            //Crea los jugadores
            for (AID a : playerAgents) {
                players.add(new PlayerInformation(a, lastId++));
            }
            for(int i = 0; i<playerAgents.size(); i++) {
                String agent_name = playerAgents.get(i).getName();
                for(PlayerInformation player : players){
                    if(player.aid.getName() == agent_name){
                        hash_pAgents.put(agent_name, player);
                    }
                }
            }
            //Initialize (inform ID)
            //Crea un mensaje de tipo inform con el id que generó y con los parametros de la partida.
            for (int i = 0 ; i < players.size(); i++) {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setContent("Id#" + players.get(i).id + "#" + parameters.N + "," + parameters.E + "," + parameters.R + "," + parameters.P + "," + parameters.NumGames);
                msg.addReceiver(players.get(i).aid);
                send(msg);
            }
            play_maker(players);
            pinta_Resultados();
        }

        private void playGame(ArrayList<PlayerInformation> players, int num_partida){
            Partida p = new Partida(players);
            //int[] plays = new int[players.size()];
            
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent(PlayerInformation.getNewGameString(players));
            for(PlayerInformation player : players){
                msg.addReceiver(player.aid);
            }
            send(msg);
            
            
            for(int i=0;  i<parameters.R; i++){
                String plays = "Results#";
                //Jugadas para cada jugador
                for(PlayerInformation player : players){
                    msg = new ACLMessage(ACLMessage.REQUEST);
                    msg.setContent("Position");
                    msg.addReceiver(player.aid);
                    send(msg);
                    //gui.logLine("Main waiting for movement");
                    ACLMessage move = blockingReceive();
                    //gui.logLine("Main Received" + move.getContent() + "from" + move.getSender().getName());
                    int inversion = Integer.parseInt(move.getContent().split("#")[1]);
                    player.endowment = player.endowment-inversion;
                    plays= plays + inversion + ",";
                }
                ACLMessage results = new ACLMessage(ACLMessage.INFORM);
                results.setContent(Game_plays(plays));
                for(PlayerInformation player: players){
                    results.addReceiver(player.aid);
                }
                send(results);
            }
            ACLMessage gameOver = new ACLMessage(ACLMessage.INFORM);
            gameOver.setContent("GameOver");
            for(PlayerInformation player: players){
                gameOver.addReceiver(player.aid);
            }
            send(gameOver);
            p.calcula_resultados();
            for(PlayerInformation player: players){
                player.restart_player_values();
            }
            //System.out.println("#####################~~~~~~~~~~~~############");
            
        }

        public void play_maker(ArrayList<PlayerInformation> players){
            ArrayList<PlayerInformation> jugadores_Partida = new ArrayList<>();
            for(int g = 0; g < parameters.Generaciones; g ++){
                if(g==0){
                    //Añade los N primeros a la partida ya que todos llevan 0 partidas.
                    for(int j = 0; j < parameters.N; j ++){
                        jugadores_Partida.add(players.get(j));
                    }
                    
                }else{
                    //Cojo los N que menos partidas han jugado
                    Collections.sort(players);
                    for(int j = 0; j < parameters.N; j ++){
                        jugadores_Partida.add(players.get(j));
                    }
                }
                //Juega las partidas
                for(int i=0; i< parameters.NumGames; i++){
                    playGame(jugadores_Partida, i);
                }
                //Elimino de el Array de todos los jugadores al peor de esta generacion
                if(players.size() > parameters.N){
                    int index = 0;
                    for(int j=0; j<jugadores_Partida.size(); j++){
                        if(jugadores_Partida.get(j).puntuacion < jugadores_Partida.get(index).puntuacion) index = j;
                    }
                    PlayerInformation loser = jugadores_Partida.get(index);
                    for(PlayerInformation player : players){
                        if(loser.aid.getName().equals(player.aid.getName())) players.remove(player);
                    }
                    //System.out.println("HA PERDIDO : " +loser.aid.getName());

                }else{
                    parameters.NumGames = 1000;
                }
                jugadores_Partida.clear();
            }
        }
        

        @Override
        public boolean done() {
            return true;
        }
    }

    public void pinta_Resultados(){
        String[] column_names = {"Jugador", "Puntuación","NºPartidas", "Avg.Puntuación"};
        for(String s: column_names){
            gui.model.addColumn(s);
        }
        //String[][] data = new String[hash_pAgents.size()][4];
        for(int i = 0; i < playerAgents.size(); i ++){
            String agent_name = playerAgents.get(i).getName();
            PlayerInformation p_info = hash_pAgents.get(agent_name);
            gui.model.addRow(new Object[]{agent_name, Integer.toString(p_info.puntuacion),Integer.toString(p_info.num_partidas),Float.toString(p_info.avg_puntuacion)});
        }
    }

    public class PlayerInformation implements Comparable<PlayerInformation>{

        AID aid;
        int id;
        int endowment; //Presupuesto
        int inversion; //Inversion en la partida
        int num_partidas;
        int puntuacion;
        float avg_puntuacion;

        public PlayerInformation(AID a, int i) {
            aid = a;
            id = i;
            endowment = parameters.E;
            this.inversion = 0;
            this.num_partidas = 0;
            this.puntuacion = 0;
            this.avg_puntuacion = 0;
        }

        @Override
        public boolean equals(Object o) {
            return aid.equals(o);
        }
        

        public static String getNewGameString(ArrayList<PlayerInformation> players){
            String result ="NewGame#";
            for(PlayerInformation player: players){
                result = result + player.id +",";
            }
            return result.substring(0, result.length()-1);   
            
        }

        public void restart_player_values(){
            this.endowment = parameters.E;
            this.inversion = 0;
        }

        public void calcula_avg_puntuacion(){
            this.avg_puntuacion = this.puntuacion / this.num_partidas; 
        }

        @Override
        public int compareTo(PlayerInformation p) {
            return (this.num_partidas - p.num_partidas);
        }

    }

    public class GameParametersStruct {
        
        int N; //Number of players in each play.
        int E; //Endowment provided to each player
        int R; //Average number of rounds in each game
        float P; //Probability of desaster
        int NumGames; // Number of games
        int Generaciones; //Number of Generations

        public GameParametersStruct() {
            N = 5;
            E = 40;
            R = 10;
            P = (float) 0.50;
            NumGames = 10;
            Generaciones = 25;
        }
        
        public void setN(int N){
            this.N = N;
        }
        public void setE(int E){
            this.E = E;
        }
        public void setR(int R){
            this.R = R;
        }
        public void setP(float P){
            this.P = P;
        }
        public void setNumGames(int NumGames){
            this.NumGames = NumGames;
        }
    }
    public GameParametersStruct getParameters(){
        return this.parameters;
    }
    public String Game_plays(String plays){
        return plays.substring(0,plays.length()-1);
    }
    public class Partida{
        //List de jugadores
        ArrayList<PlayerInformation> players;
        int valor_aportado;
        int num_partida; //Number of Game;
        HashMap<Integer,ArrayList<Integer>> hash_partida; //id_jugador , list_apuestas
        
        public Partida(ArrayList<PlayerInformation> players){
            this.players = players;
            this.hash_partida = new HashMap<Integer,ArrayList<Integer>>();
            this.valor_aportado = 0;
            for(PlayerInformation player : players){
                ArrayList<Integer> inversiones = new ArrayList<Integer>();
                hash_partida.put(player.id, inversiones);
            }
        }
        public void calcula_resultados(){
            for(PlayerInformation player: this.players){
                this.valor_aportado += calc_valor_aportado_jugador(hash_partida.get(player.id));
                if(disaster()){
                    System.out.println("DESASTRE!!!!!!!!!!!!!");
                    player.num_partidas ++;
                }else{
                    //System.out.println(player.aid.getName()+"Puntuacion: " + player.endowment);
                    player.puntuacion += player.endowment;
                    player.num_partidas ++;
                }
                player.calcula_avg_puntuacion();
            }

        }
        public boolean disaster(){
            if(this.num_partida == parameters.NumGames-1){ //si es la ultima partida
                if(this.valor_aportado < (this.players.size()*parameters.E)/2){ //Han aportado menos que (N*E)/2
                    int valor = (int) (Math.random()*10+1);
                    if(valor < parameters.P*10){
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
        public int calc_valor_aportado_jugador(ArrayList<Integer> inversiones){
            int valor=0;
            for(int inversion: inversiones){
                valor = valor + inversion;
            }
            return valor;
        }
    } 
}
