import java.awt.*; //interfaces graficas
import java.awt.event.*; //escuchar eventos
import javax.swing.*; //mas interfaces graficas


/**
 * Graphical User Interface (GUI) example
 *
 * @author  Juan C. Burguillo Rial
 * @version 1.0
 */



/**
  * This class creates a graphical user interface with menus, dialogs and labels.
  *
  * @author  Juan C. Burguillo Rial
  * @version 1.0
  */
class GUI extends JFrame implements ActionListener, Runnable
{
  private boolean bProcessExit = false;
  private Thread oProcess; // Object to manage the thread
  private MyDialog oDl;

/**
  * This is the GUI constructor.
  *
  */
GUI() {
  super (" GUI"); // Calling the constructor from the parent class
  
  setBackground (Color.black);
  setForeground (Color.white);

  MenuBar mb = new MenuBar();                             // Barra de menus
  //Edit
  Menu menu = new Menu("Edit");                           // un menu de la barra
  //items edit menu
  MenuItem menu_item = new MenuItem ("Reset players");                     // una opcion del menu
  menu_item.addActionListener (this);                            // Escucha el evento de click 
  menu.add(menu_item);                                          // Añadiendo la opcion al menu
  menu_item = new MenuItem ("Remove players", new MenuShortcut('O'));     // Shortcuts are hot keys for executing actions  
  menu_item.addActionListener (this);
  menu.add(menu_item);
  mb.add (menu);                                         // Including this menu in the MenuBar
  
  //Run
  menu = new Menu("Run");
  //items run menu
  menu_item = new MenuItem ("New");
  menu_item.addActionListener (this);
  menu.add(menu_item);

  menu_item = new MenuItem ("Stop");
  menu_item.addActionListener (this);
  menu.add(menu_item);

  menu_item = new MenuItem ("Continue");
  menu_item.addActionListener (this);
  menu.add(menu_item);
  
  menu_item = new MenuItem ("Number of games");
  menu_item.addActionListener (this);
  menu.add(menu_item);

  menu_item = new MenuItem ("Change Param");
  menu_item.addActionListener (this);
  menu.add(menu_item);

  mb.add (menu);
  
  //Window
  menu = new Menu("Window");

  //items window menu
  menu_item = new MenuItem("Verbose on/off");
  menu_item.addActionListener(this);
  menu.add(menu_item);

  mb.add(menu);

  //Help 
  menu = new Menu("Help");
  //items help menu
  menu_item = new MenuItem("About");
  menu.add(menu_item);
  mb.add(menu);
  mb.setHelpMenu (menu);                                 // Helps menus appear in some OSs in the right side
  

  setMenuBar(mb);
  
  setLayout(new GridLayout(5,1));
  add (new Label ("Number of players", Label.LEFT));
  add (new Label ("Parameters", Label.LEFT));
  add (new Label ("Number of games", Label.LEFT));
  add (new Label ("Names and statistics", Label.LEFT));
  add (new Label ("Game info", Label.LEFT));
  setSize (new Dimension(500,400));     // Window size
  setLocation (new Point (100, 100));   // Window position in the screen
  setVisible (true);                    // Let's make the GUI appear in the screen  
}



/**
  * This method recibes and process events related with this class.
  *
  * @param evt In this parameter we receive the event that has been generated.
  */
public void actionPerformed (ActionEvent evt) {
  if ("New".equals (evt.getActionCommand()))
    System.out.println ("\nYou have selected 'New' ! \n");
  else if ("Start".equals (evt.getActionCommand()))
    vStartThread ();
  else if ("Exit".equals (evt.getActionCommand())) {
    dispose();
    System.exit(0);
  }
  else if ("GUI".equals (evt.getActionCommand()))
    System.out.println ("\nYou have selected GUI Help ! \n ");
  else if ("About".equals (evt.getActionCommand()))
    oDl = new MyDialog (this, "About", false);
}



/**
  * This method starts a thread
  */
private void vStartThread () {
  if (oProcess == null) {
    oProcess = new Thread (this);
    oProcess.start();
    bProcessExit = false;
  }
}


/**
  * This method stops a thread
  */
private void vStopThread () {
  if (oProcess != null)
    bProcessExit = true;
}


/**
  * This method contains the code to be executed in parallel.
  */
public void run() {
  int i=0;
  while (true) {
   
    try {
      i++;
      System.out.println("Working iteration: " + i);
      Thread.sleep(1000);
    }
    catch (InterruptedException oIE) {}
    
    if (bProcessExit) return;
  }
}



public static void main (String args[]) {
  GUI oGUI= new GUI();
}

} // from the class GUI








/**
  * This class produces dialog windows with a text field and two buttons: one to accept and another to cancel.
  *
  * @author  Juan C. Burguillo Rial
  * @version 1.0
*/
class MyDialog extends JDialog implements ActionListener
{
private JTextField oJTF;

/**
  * This is the MyDialog class constructor
  *
  * @param oParent Reference to the object that has created this MyDialog object
  * @param sDialogName Name of this dialog window
  * @param bBool Indicates if this is a modal window (true) or not.
  */
MyDialog (Frame oParent, String sDialogName, boolean bBool) {
  super (oParent, sDialogName, bBool);
  
  setBackground (Color.white); // Colors
  setForeground (Color.blue);
  
  setLayout (new GridLayout(2,1));
  
  oJTF = new JTextField ("1234", 30);
  add (oJTF);
  
  JPanel oJPanel = new JPanel();
  oJPanel.setLayout (new GridLayout(1,2));
  JButton oJBut = new JButton ("OK");
  oJBut.addActionListener (this);
  oJPanel.add (oJBut);
  oJBut  = new JButton ("Cancel");
  oJBut.addActionListener (this);
  oJPanel.add (oJBut);
  add (oJPanel);
  
  setSize (new Dimension(300,150));
  setLocation (new Point (150, 150));
  setVisible (true);
}



/**
  * This method recibes and process events related with this class.
  *
  * @param evt In this parameter we receive the event that has been generated.
  */
public void actionPerformed (ActionEvent evt) {
  if ("OK".equals (evt.getActionCommand())) {
    String sText = oJTF.getText();                     // Getting the present text from the TextField
    int iVal = Integer.parseInt (sText);               // Converting such text to several formats
    float fVal = Float.parseFloat (sText);
    double dVal = Double.parseDouble (sText);
    dispose();                                         // Closing the dialog window
  }
  
  else if ("Cancel".equals (evt.getActionCommand()))
    dispose();
}


} // from MyDialog class
