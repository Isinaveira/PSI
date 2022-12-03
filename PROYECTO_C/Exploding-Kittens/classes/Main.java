package classes;
import java.util.HashMap;

public class Main{

    public static void main(String[] args) {
        
        HashMap<String, String> imagesPath = new HashMap<String, String>();
        imagesPath.put("EXPLODING KITTEN", "../images/explodin_kitten.jpg");
        imagesPath.put("DEFUSE", "../images/defuse.jpg");
        imagesPath.put("SKIP", "../images/skip.jpg");
        imagesPath.put("ATTACK", "../images/attack.jpg");
        imagesPath.put("FAVOR", "../images/favor.jpg");

        Partida p = new Partida( 2, 3, 3, imagesPath);

    }



}