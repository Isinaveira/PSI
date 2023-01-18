package classes;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class Utilities{
    
    public static boolean isNumeric(String str) { 
        try {  
          Double.parseDouble(str);  
          return true;
        } catch(NumberFormatException e){  
          return false;  
        }  
    }

    public void exportData(HashMap<String,StateAction> hash_StateActions , String file_name) throws IOException{
      try {
        FileOutputStream fos = new FileOutputStream(file_name);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(hash_StateActions);

        oos.close();
        fos.close();
        
      } catch (IOException e) {
        e.printStackTrace();
      }

    }

    public HashMap<String, StateAction> importData(String nombre) throws IOException{
      HashMap<String, StateAction> hash_States = null;
      try{
        
        FileInputStream fis = new FileInputStream(nombre);
        ObjectInputStream ois = new ObjectInputStream(fis);
        
        hash_States = (HashMap<String,StateAction>) ois.readObject();


        ois.close();
        fis.close();

      }catch(Exception e){
        e.printStackTrace();
      }
      
      return hash_States;


    }
}
