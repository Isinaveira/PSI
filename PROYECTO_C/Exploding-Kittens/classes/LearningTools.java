package classes;

//imports
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.ArrayList;
import java.io.Serializable;


class LearningTools  implements Serializable{

  final double DecFactorLR = 0.99; // Value that will decrement the learning rate in each generation
  double epsilon = 0.6; // Used to avoid selecting always the best action
  final double MINLearnRate = 0.05; // We keep learning, after convergence, during 5% of times

  StateAction PresentStateAction; // Contains the present state we are and the actions that are available
  ArrayList<StatePlayed> list_StatePlayeds = new ArrayList<>();
  HashMap<String, StateAction> hash_StateActions = new HashMap<>(); // A vector containing strings with the possible States and Actions
                                                  // available at each one
  StateAction StateAction;
  StateAction LastStateAction;
  int accion, numAcciones;
  double LearnRate;
  double Gamma;
  double Reward;
  /**
   * 0------9
   * [[ActualState, ActionSelected],...,[]]
   * 
   * This method is used to implement Q-Learning:
   * 1. I start with the last action a, the previous state s and find the actual
   * state s'
   * 2. Select the new action with Qmax{a'}
   * 3. Adjust: Q(s,a) = Q(s,a) + LearnRateLR [R + Gamma . Qmax{a'}(s',a') -
   * Q(s,a)]
   * 4. Select the new action by a epsilon-greedy methodology
   *
   * @param state    contains the present state
   * @param numActions contains the number of actions that can be applied in this
   *                  state
   * @param Reward   is the reward obtained after performing the last action.
   */
  public void updateQlearning(int Reward) {
    //Si ha ganado la partida entonces Reward será positiva y si la ha perdido Reward será negativa
    for(int i = 0 ; i < list_StatePlayeds.size(); i ++){
      //System.out.println(list_StatePlayeds.get(i).toString());
    }
    
    
    for(int i = 0 ; i< list_StatePlayeds.size(); i++){

        StatePlayed state_action = list_StatePlayeds.get(i); 
        String estado = state_action.getState();
        int action = state_action.getAction_played(); 
        double value = hash_StateActions.get(estado).dGetQAction(action);
        
        double dQmax = hash_StateActions.get(estado).getQmax();
        
          value += LearnRate * (Reward + Gamma * dQmax - value);
        hash_StateActions.get(estado).dValAction[action] = value;

    }
    LearnRate *= DecFactorLR; // Reducing the learning rate
    if (LearnRate < MINLearnRate)
    LearnRate = MINLearnRate;
    list_StatePlayeds.clear();
  }

 
  
  public int getNewActionQLearning(String state) {
    
    int iBest=-1, iNumBest=1;
    int iNewAction;
    double dQmax;
    Vector<Integer> indices_no_nulos = new Vector<Integer> (1,1); 
    // Searching if we already have the state at the HashMap
    if (!hash_StateActions.containsKey(state)) {
      PresentStateAction = new StateAction(state);
      hash_StateActions.put(state, PresentStateAction);

    } else {
      PresentStateAction = hash_StateActions.get(state);
    }
    //System.out.println("Probabilidades = ["+PresentStateAction.dValAction[0]+","+ PresentStateAction.dValAction[1]+","+ PresentStateAction.dValAction[2]+","+PresentStateAction.dValAction[3]+"]");
    dQmax = 0;
    for (int i=0; i<PresentStateAction.dValAction.length; i++) {     // Determining the action to get Qmax{a'}
      if (PresentStateAction.dValAction[i] > dQmax) {
        iBest = i;
        iNumBest = 1;       // Reseting the number of best actions
        dQmax = PresentStateAction.dValAction[i];
        indices_no_nulos.addElement(i);
      }
      else if ( (PresentStateAction.dValAction[i] == dQmax) && (dQmax > 0) ) { // If there is another one equal we must select one of them randomly
        iNumBest++;
        indices_no_nulos.addElement(i);
        if (Math.random() < 1.0 / (double) iNumBest) {    // Choose randomly with reducing probabilities
          iBest = i;
          dQmax = PresentStateAction.dValAction[i]; 
        }
      }else if( PresentStateAction.dValAction[i] < dQmax && PresentStateAction.dValAction[i] > 0 ){
        indices_no_nulos.addElement(i);
      }
    }
    ////System.out.println("HOLA"+"iBest: " + iBest + " iNumBest" + iNumBest);
    if ( (iBest > -1) && (Math.random() < epsilon) )    // Using the e-greedy policy to select the best action or any of the rest
      iNewAction = iBest;
    else{
      int indice_vector = (int) Math.random()*indices_no_nulos.size();
      iNewAction = indices_no_nulos.elementAt(indice_vector);
    } 
  
    //Adding the state and the action played to the list of actions played   
    StatePlayed sP = new  StatePlayed(PresentStateAction.state, iNewAction);
    list_StatePlayeds.add(sP);
      
    LastStateAction = PresentStateAction; // Updating values for the next time
    ////System.out.println("Accion jugada: " + iNewAction);
    return sP.getAction_played();
  }

  public Float[] getPosibleActions(String state){
    Float[] actions = new Float[4];



    return actions;
  } 

  public String getActualState(HashMap<Integer, Card> hand, Integer deck_size, HashMap<String, Integer> used_cards){
    int[] hand_values = new int[4];
    int[] used_values = new int[4];   
    String state = "";
    for(Map.Entry<Integer,Card> card : hand.entrySet()){
      switch(card.getValue().getType()){
        case "DEFUSE":
          hand_values[0]+=1;
          break;
        case "FAVOR":
          hand_values[1]+=1;
          break;
        case "SKIP":
          hand_values[2]+=1;
          break;
        case "ATTACK":
          hand_values[3]+=1;
          break;
      }
    }
    //TIPO: NUMERO_CARTAS
    for(Map.Entry<String,Integer> type : used_cards.entrySet()){
      switch(type.getKey()){
        case "DEFUSE":
          used_values[0] = type.getValue();
          break;
        case "FAVOR":
          used_values[1] = type.getValue();
          break;
        case "ATTACK":
          used_values[2] = type.getValue();
          break;
        case "SKIP": 
          used_values[3] = type.getValue();
          break;
      }
    }
    //transformamos los valores de la mano,usadas y el tamaño del deck a string y los añadimos al estado
    for(int i = 0; i< hand_values.length; i++){
      state +=Integer.toString((hand_values[i]));
    }
    state += "_";
    state += Integer.toString(deck_size);
    state += "_";
    for(int i = 0; i< used_values.length; i++){
      state +=Integer.toString((used_values[i]));
    }
    return state;
  }

}


class StateAction implements Serializable {
    String state;
    double[] dValAction;
  
    StateAction(String auxState) {
      state = auxState;
      String[] aux = auxState.split("_");
      double [] aux_valAction = new double[4];
      aux_valAction[0]=1; //robar
      String cards_string = aux[0];
      int contador_acciones=1;
      for(int i = 1; i < cards_string.length(); i ++){
        int valor = Character.getNumericValue(cards_string.charAt(i));
        if(valor != 0){
          aux_valAction[i]=1;
          contador_acciones ++;
        }else{
          aux_valAction[i]=0;
        }
      }
      for(int i = 0 ; i< aux_valAction.length ; i ++){
          aux_valAction[i] = aux_valAction[i]/contador_acciones;
      } 
      
      dValAction = aux_valAction;
      ////System.out.println("Probabilidades: " +dValAction[0] +"," +dValAction[1]+"," +dValAction[2] +"," +dValAction[3] );
    }
  
    public String sGetState() {
      return state;
    }
  
    public double[] getValAction(){
      return dValAction;
    }
  
    public double dGetQAction(int i) {
      return dValAction[i];
    }
  
    public double getQmax(){
      double dQmax = 0 ; 
      for(int i = 0 ; i< dValAction.length; i++){
          if(dQmax< dValAction[i]){
              dQmax = dValAction[i];
          }
      }
  
      return dQmax;
    }
    public String aString (){
      return state+": ["+dValAction[0]+","+dValAction[1]+","+dValAction[2]+","+dValAction[3]+"]";
    }
  }
  
  class StatePlayed {
    String state;
    int action_played;
  
    StatePlayed(String state, int action_played) {
      this.state = state;
      this.action_played = action_played;
    }
  
    public String getState() {
      return this.state;
    }
  
    public void setState(String state) {
      this.state = state;
    }
  
    public int getAction_played() {
      return this.action_played;
    }
  
    public void setAction_played(int action_played) {
      this.action_played = action_played;
    }
    
    public String toString(){
      return state +":"+ action_played;
    }
  }