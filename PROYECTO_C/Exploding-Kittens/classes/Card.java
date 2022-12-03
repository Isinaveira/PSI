package classes;

public class Card {
    private String type; // shuffle, skip, favor, attack, exploding kitten o defuse
    private String img; // ruta a la imagen
    private Estado estado;
    private int id;
    static public int id_index=0;

    public enum Estado {
        REPARTIDA, USADA, LIBRE // REPARTIDA: la tiene un jugador en la mano
                                // USADA: está en el mazo de usadas
                                // LIBRE: está en el mazo para coger
    };

    public Card(String type, String path) {
        this.id = id_index;
        id_index ++;
        this.type = type;
        this.img = path;
        this.estado = Estado.LIBRE;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Estado getEstado() {
        return this.estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

}
