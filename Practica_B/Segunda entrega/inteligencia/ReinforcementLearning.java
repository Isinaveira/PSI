package inteligencia;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * This is a basic class with some learning tools: statistical learning,
 * learning automata (LA) and Q-Learning (QL)
 *
 * @author Juan C. Burguillo Rial
 * @version 2.0
 */
class LearningTools {
  final double dDecFactorLR = 0.99; // Value that will decrement the learning rate in each generation
  final double dEpsilon = 0.95; // Used to avoid selecting always the best action
  final double dMINLearnRate = 0.05; // We keep learning, after convergence, during 5% of times

  StateAction oPresentStateAction; // Contains the present state we are and the actions that are available
  ArrayList<StatePlayed> list_StatePlayeds = new ArrayList<>();
  HashMap<String, StateAction> hash_StateActions = new HashMap<>(); // A vector containing strings with the possible States and Actions
                                                  // available at each one
  StateAction oStateAction;
  StateAction oLastStateAction;
  int iAction, iNumActions;
  double dLearnRate;
  double dGamma;

  /**
   * 0------9
   * [[ActualState, ActionSelected],...,[]]
   * 
   * This method is used to implement Q-Learning:
   * 1. I start with the last action a, the previous state s and find the actual
   * state s'
   * 2. Select the new action with Qmax{a'}
   * 3. Adjust: Q(s,a) = Q(s,a) + dLearnRateLR [R + dGamma . Qmax{a'}(s',a') -
   * Q(s,a)]
   * 4. Select the new action by a epsilon-greedy methodology
   *
   * @param sState    contains the present state
   * @param iNActions contains the number of actions that can be applied in this
   *                  state
   * @param dReward   is the reward obtained after performing the last action.
   */
  public void updateQlearning(ArrayList<StateAction> list_jugadas) {

  }

  public void vGetNewActionQLearning(String sState, int iNActions, double dReward) {
    int iBest = -1, iNumBest = 1;
    double dQmax;
    int iNewAction;

    // Searching if we already have the state at the HashMap
    if (!hash_StateActions.containsKey(sState)) {
      oPresentStateAction = new StateAction(sState, iNActions);
      hash_StateActions.put(sState, oPresentStateAction);

    } else {
      oPresentStateAction = hash_StateActions.get(sState);
    }

    dQmax = 0;
    for (int i = 0; i < iNActions; i++) { // Determining the action to get Qmax{a'}
      if (oPresentStateAction.dValAction[i] > dQmax) {
        iBest = i;
        iNumBest = 1; // Reseting the number of best actions
        dQmax = oPresentStateAction.dValAction[i];
      } else if ((oPresentStateAction.dValAction[i] == dQmax) && (dQmax > 0)) { // If there is another one equal we must
                                                                                // select one of them randomly
        iNumBest++;
        if (Math.random() < 1.0 / (double) iNumBest) { // Choose randomly with reducing probabilities
          iBest = i;
          dQmax = oPresentStateAction.dValAction[i];
        }
      }
    }
    // Adjusting Q(s,a)   --------------------------------#########################################
    if (oLastStateAction != null)
      oLastStateAction.dValAction[iAction] += dLearnRate
          * (dReward + dGamma * dQmax - oLastStateAction.dValAction[iAction]);

    if ((iBest > -1) && (Math.random() > dEpsilon)) // Using the e-greedy policy to select the best action or any of the
                                                    // rest
      iNewAction = iBest;
    else
      do {
        iNewAction = (int) (Math.random() * (double) iNumActions);
      } while (iNewAction == iBest);

    //Adding the state and the action played to the list of actions played   
    StatePlayed sP = new  StatePlayed(oPresentStateAction.sState, iNewAction);
    list_StatePlayeds.add(sP);

    oLastStateAction = oPresentStateAction; // Updating values for the next time
    dLearnRate *= dDecFactorLR; // Reducing the learning rate
    if (dLearnRate < dMINLearnRate)
      dLearnRate = dMINLearnRate;
  }

} // from class LearningTools

/**
 * This is the basic class to store Q values (or probabilities) and actions for
 * a certain state
 *
 * @author Juan C. Burguillo Rial
 * @version 2.0
 */
class StateAction implements Serializable {
  String sState;
  double[] dValAction;

  StateAction(String sAuxState, int iNActions) {
    sState = sAuxState;
    dValAction = new double[iNActions];
  }

  public String sGetState() {
    return sState;
  }

  public double dGetQAction(int i) {
    return dValAction[i];
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

}


