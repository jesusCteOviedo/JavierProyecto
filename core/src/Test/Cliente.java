package Test;


import com.badlogic.gdx.utils.async.AsyncTask;
import com.mygdx.game.Datos;
import com.mygdx.game.Enemigo;
import com.mygdx.game.Escuadron;
import com.mygdx.game.Heroes;


import java.net.*;
import java.io.*;
import java.util.ArrayList;

import BasesDeDatos.EscuadronBBDD;
import BasesDeDatos.HeroBBDD;


public class Cliente implements AsyncTask {






    public void guardar(HeroBBDD hero, ArrayList<Escuadron> escuadron,int id,ArrayList<Escuadron> escu){
        try {

            Socket socket=new Socket("localhost",2500);
            OutputStream salida=socket.getOutputStream();
            ObjectOutputStream flujo_salida = new ObjectOutputStream( salida);
            flujo_salida.writeObject(hero);
            flujo_salida.writeInt(escuadron.size());
            for (int i=0;i<escuadron.size();i++) {
                flujo_salida.writeObject(new EscuadronBBDD(escuadron.get(i)));
            }
            flujo_salida.writeInt(id);//nivel
            flujo_salida.writeInt(escu.size());
            for (int i=0;i<escu.size();i++) {
                flujo_salida.writeObject(new EscuadronBBDD(escu.get(i)));
            }
            flujo_salida.flush();
            flujo_salida.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void guardar( Datos d, int id){
        try {

            Socket socket=new Socket("localhost",2500);
            OutputStream salida=socket.getOutputStream();
            ObjectOutputStream flujo_salida = new ObjectOutputStream( salida);
            flujo_salida.writeInt(2);
            flujo_salida.writeObject(d);
            flujo_salida.writeInt(id);//nivel
            flujo_salida.flush();
            flujo_salida.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }




    public Datos cargar(String id_Usuario){
        try {




            /*Socket socket=new Socket("localhost",2500);
            OutputStream salida=socket.getOutputStream();
            ObjectOutputStream flujo_salida = new ObjectOutputStream( salida);
            flujo_salida.writeInt(1);
            flujo_salida.flush();
            flujo_salida.close();*/



            /*ServerSocket direccion=new ServerSocket(2600);
            Socket socket2=direccion.accept();
            InputStream entradaDatos = socket2.getInputStream();
            ObjectInputStream entrada =new ObjectInputStream(entradaDatos);

            Heroes h=(Heroes) entrada.readObject();
            /*int level=entrada.readInt();
            String id_map=entrada.readUTF();
            ArrayList<Escuadron>es=new ArrayList<Escuadron>();
            es= (ArrayList<Escuadron>) entrada.readObject();
            // Datos d=new Datos(id_Usuario,h,es,id_map,null,level);*/
            return null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void c(){
        try {
            Socket socket = new Socket("localhost", 2500);
            OutputStream salida = socket.getOutputStream();
            ObjectOutputStream flujo_salida = new ObjectOutputStream(salida);
            flujo_salida.writeInt(1);
            flujo_salida.flush();
            flujo_salida.close();


            ServerSocket direccion=new ServerSocket(2600);
            Socket socket2=direccion.accept();
            InputStream entradaDatos = socket2.getInputStream();
            ObjectInputStream entrada =new ObjectInputStream(entradaDatos);

          /*  if(entrada.readUTF().equals("Acabo")){
                entrada.close();
                entradaDatos.close();
                socket2.close();
                File archivo = null;
                FileReader fr = null;
                BufferedReader br = null;

                try {

                    // Apertura del fichero y creacion de BufferedReader para poder
                    // hacer una lectura comoda (disponer del metodo readLine()).
                    archivo = new File ("/data/data/com.mygdx.game/files/Escuadron.txt");
                    fr = new FileReader (archivo);
                    br = new BufferedReader(fr);

                    // Lectura del fichero
                    String linea;
                    while((linea=br.readLine())!=null)
                        System.out.println(linea);
                }
                catch(Exception e){
                    e.printStackTrace();
                }finally{
                    // En el finally cerramos el fichero, para asegurarnos
                    // que se cierra tanto si todo va bien como si salta
                    // una excepcion.
                    try{
                        if( null != fr ){
                            fr.close();
                        }
                    }catch (Exception e2){
                        e2.printStackTrace();
                    }
                }
            }*/


        }catch (Exception e){

        }
      /*  File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File ("/data/data/com.mygdx.game/files/prueba_int.txt");
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while((linea=br.readLine())!=null)
                System.out.println(linea);
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta
            // una excepcion.
            try{
                if( null != fr ){
                    fr.close();
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }*/



    }


    public Datos prueba1(String id_usuario){

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        Heroes b=new Heroes();
        ArrayList<Enemigo>enemi=new ArrayList<Enemigo>();
        ArrayList<Escuadron>escuadron=new ArrayList<Escuadron>();
        Datos d;
        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).

            /*"Heroes{" +
                "posiconX=" + posiconX +
                ", posicionY=" + posicionY +
                ", vida=" + vida +
                ", defensa=" + defensa +
                ", ataque=" + ataque +
                '}';*/

            archivo = new File ("/data/data/com.mygdx.game/files/Hero.txt");
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            String[] parts = new String[10];
            while((linea=br.readLine())!=null) {
                parts= linea.split("\\,");
              //  System.out.println(linea);

            }
            //String nombre, int vida, int ataque, int defensa, String path,int posiconX,int posicionY,int poder
            b=new Heroes(parts[5],Integer.parseInt(parts[2]),Integer.parseInt(parts[4]),Integer.parseInt(parts[3])
                    ,parts[6],Integer.parseInt(parts[0]),Integer.parseInt(parts[1]),Integer.parseInt(parts[7]));

            br.close();
            fr.close();



            archivo = new File ("/data/data/com.mygdx.game/files/Enemigo.txt");
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);
            String linea2;
            String[] parts2 = new String[10];
             /*"Enemigo{" +
                    "nombre='" + nombre + '\'' +
                    ", path='" + path + '\'' +
                    ", vida=" + vida +
                    ", ataque=" + ataque +
                    ", defensa=" + defensa +
                    ", fila=" + fila +
                    ", columna=" + columna +
                    ", id='" + id + '\'' +
                    ", idGradico=" + idGradico +
                    '}';
            */
            while((linea2=br.readLine())!=null) {
                parts2= linea2.split("\\,");
                //System.out.println(parts2[1]);
               enemi.add(new Enemigo(parts2[0],Integer.parseInt(parts2[2]),Integer.parseInt(parts2[3]),Integer.parseInt(parts2[4])
                        ,parts2[1],Integer.parseInt(parts2[5]),Integer.parseInt(parts2[6]),parts2[7],Integer.parseInt(parts2[8])));


            }

            br.close();
            fr.close();



            archivo = new File ("/data/data/com.mygdx.game/files/Escuadron.txt");
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);
            String linea3;
            String[] parts3 = new String[10];
             /*
            ", posicionX=" + posicionX +
                ", posicionY=" + posicionY +
                ", fila=" + fila +
                ", columna=" + columna +
                ", id=" + id +
                ", id_escuadron='" + id_escuadron +
                ", path='" + path +
                '}';
             */
            while((linea3=br.readLine())!=null) {
                parts3= linea3.split("\\,");
                //float posicionX, float posicionY, String rutaTextura, int fila, int columna, String id_escuadron,int id
                Escuadron e=new Escuadron(Float.parseFloat(parts3[0]),Float.parseFloat(parts3[1]),parts3[6],Integer.parseInt(parts3[2])
                        ,Integer.parseInt(parts3[3]),parts3[5],Integer.parseInt(parts3[4]));
                for(int i=0;i<enemi.size();i++){
                    if(enemi.get(i).getId().contains(parts3[5])){
                        e.addEnemigo(enemi.get(i));
                    }
                }
                escuadron.add(e);
                //escuadron.add(new Escuadron(Float.parseFloat(parts3[0])))


            }

            /*for (int i=0;i<escuadron.size();i++){
                System.out.println(escuadron.get(i).toString());
            }*/

            br.close();
            fr.close();


            archivo = new File ("/data/data/com.mygdx.game/files/Mapa.txt");
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);
            String linea4;
            String[] parts4 = new String[10];
            int level = 0;
            String id_mapa = null;
            while((linea4=br.readLine())!=null) {
                parts4= linea4.split("\\,");
                level=Integer.parseInt(parts4[0]);
                id_mapa=parts4[1];


            }

            br.close();
            fr.close();

//String id_usario,Heroes heroe, ArrayList<Escuadron> escuadrones,String id_mapa,String id_mapaViejo,int level
            d=new Datos(id_usuario,b,escuadron,id_mapa,null,level);
            return d;
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta
            // una excepcion.
            try{
                if( null != fr ){
                    fr.close();
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }

        return null;
    }







    @Override
    public Object call() throws Exception {
        return null;
    }
}
