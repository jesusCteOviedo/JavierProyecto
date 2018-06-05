package Test;

import java.util.ArrayList;

public class ContenedorMapas {

   /* private ArrayList<Integer>id;

    public ContenedorMapas(){
        id=new ArrayList<Integer>();
    }

    public void rellenar(){
        for(int i=0;i<4;i++){
            id.add(i);
        }
    }

    public String getPath(int identificador){
        return id.get(identificador);
    }*/

    private ArrayList<String>path;

    public ContenedorMapas(){
        path=new ArrayList<String>();
    }

    public void rellenar(){
        path.add("maps/level1.tmx");
        path.add("maps/level2.tmx");
        path.add("maps/level3.tmx");
        path.add("maps/level4.tmx");
    }

    public String getPath(String ruta){
        int identificador=path.indexOf(ruta);
        return path.get(identificador);
    }

    public String levelUp(String ruta){
        int identificador=path.indexOf(ruta);
        if(identificador==path.size()-1){
            return path.get(0);
        }else {
            return path.get(identificador + 1);
        }
    }

}
