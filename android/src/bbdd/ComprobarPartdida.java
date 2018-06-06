package bbdd;

public class ComprobarPartdida {



    private boolean tienePartida;

    public ComprobarPartdida() {}

    public ComprobarPartdida(boolean tienePartida) {
     this.tienePartida=tienePartida;
    }

    public boolean isTienePartida() {
        return tienePartida;
    }

    public void setTienePartida(boolean tienePartida) {
        this.tienePartida = tienePartida;
    }

}
