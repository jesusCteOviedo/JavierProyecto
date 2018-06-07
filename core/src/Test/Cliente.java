package Test;


import com.badlogic.gdx.utils.async.AsyncTask;
import com.mygdx.game.Escuadron;


import java.net.*;
import java.io.*;
import java.util.ArrayList;

import BasesDeDatos.EscuadronBBDD;
import BasesDeDatos.HeroBBDD;


public class Cliente implements AsyncTask {






    public void guardar(HeroBBDD hero, ArrayList<Escuadron> escuadron,int id){
        try {

            Socket socket=new Socket("localhost",2500);
            OutputStream salida=socket.getOutputStream();
            ObjectOutputStream flujo_salida = new ObjectOutputStream( salida);
            flujo_salida.writeObject(hero);
            flujo_salida.writeInt(escuadron.size());
            for (int i=0;i<escuadron.size();i++) {
                flujo_salida.writeObject(new EscuadronBBDD(escuadron.get(i)));
            }
            flujo_salida.writeInt(id);
            flujo_salida.flush();
            flujo_salida.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void cargar(){
        try {

            Socket socket=new Socket("localhost",2500);
            OutputStream salida=socket.getOutputStream();
            ObjectOutputStream flujo_salida = new ObjectOutputStream( salida);
            flujo_salida.writeInt(2);
            flujo_salida.flush();
            flujo_salida.close();



            ServerSocket direccion=new ServerSocket(2500);
            Socket socket2=direccion.accept();
            InputStream entradaDatos = socket2.getInputStream();
            ObjectInputStream entrada =new ObjectInputStream(entradaDatos);



        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public Object call() throws Exception {
        return null;
    }
}
