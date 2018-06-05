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
            /*InetAddress hostReceptor = InetAddress.getByName("127.0.0.1");
            DatagramSocket socket = new DatagramSocket();
            ByteArrayOutputStream bs= new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream (bs);
            os.writeObject(datos);  // this es de tipo DatoUdp
            os.close();
            byte[] bytes =  bs.toByteArray(); // devuelve byte[]

            DatagramPacket paquete = new DatagramPacket(bytes, bytes.length, hostReceptor,6789);
            socket.send(paquete);
*/

            Socket socket=new Socket("localhost",2500);
            OutputStream salida=socket.getOutputStream();
            ObjectOutputStream flujo_salida = new ObjectOutputStream( salida);
            flujo_salida.writeObject(hero);
            flujo_salida.writeInt(escuadron.size());
            for (int i=0;i<escuadron.size();i++) {
                flujo_salida.writeObject(new EscuadronBBDD(escuadron.get(i)));
            }
            flujo_salida.writeInt(id);
            //flujo_salida.writeObject(escuadron);
            flujo_salida.flush();
            flujo_salida.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public Object call() throws Exception {
        return null;
    }
}
