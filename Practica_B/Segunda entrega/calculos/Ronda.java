

public class Ronda {
    private int num_ronda;
    private int salvado;
    private int opoX;
    private int opoY;
    private int opoZ;
    private int opoT;

    public Ronda(int num_ronda, int salvado,  int opoX, int opoY,  int opoZ, int opoT){
        this.num_ronda = num_ronda;
        this.salvado = salvado;
        this.opoX = opoX;
        this.opoY = opoY;
        this.opoZ = opoZ;
        this.opoT = opoT;
    }
    
    public String rondaToString(){
        return Integer.toString(num_ronda)+"_"+Integer.toString(salvado)+"_"+Integer.toString(opoX)+"_"+Integer.toString(opoY)+"_"+Integer.toString(opoZ)+"_"+Integer.toString(opoT);
    }
}
