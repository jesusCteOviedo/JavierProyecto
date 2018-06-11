package bbdd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mygdx.game.Enemigo;
import com.mygdx.game.Escuadron;
import com.mygdx.game.Heroes;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


import static com.firebase.ui.auth.ui.phone.SubmitConfirmationCodeFragment.TAG;

public class CargarPartida extends Activity{


    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ArrayList<Enemigo> enemigo_juego;
    private ArrayList<Escuadron> escuadron_cargar;
    private Escuadron escuadon;
    private Heroes hero;
    private OutputStreamWriter h,m,es,e;
    private int resultado;





/*
    public void cargarPartida(Activity acti ){
        try {

            enemigo_juego=new ArrayList<>();
            escuadron_cargar=new ArrayList<>();
            mAuth= FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            db=FirebaseFirestore.getInstance();

            h= new OutputStreamWriter(
                    acti.openFileOutput("Hero.txt", Context.MODE_PRIVATE));
            m= new OutputStreamWriter(
                    acti.openFileOutput("Mapa.txt", Context.MODE_PRIVATE));
            es= new OutputStreamWriter(
                    acti.openFileOutput("Escuadron.txt", Context.MODE_PRIVATE));
            e= new OutputStreamWriter(
                    acti.openFileOutput("Enemigo.txt", Context.MODE_PRIVATE));

            db.collection("Heroes")
                    .whereEqualTo("ID_PARTIDA", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {


                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Log.d(TAG, document.getId() + " => " + document.getData());


                                    hero = new Heroes(document.getData().get("NOMBRE").toString()
                                            ,Integer.parseInt(document.getData().get("VIDA").toString())
                                            ,Integer.parseInt(document.getData().get("ATAQUE").toString())
                                            ,Integer.parseInt(document.getData().get("DEFENSA").toString())
                                            ,document.getData().get("PATH").toString()
                                            ,Integer.parseInt(document.getData().get("POSICIONX").toString())
                                            ,Integer.parseInt(document.getData().get("POSICIONY").toString())
                                            ,Integer.parseInt(document.getData().get("PODER").toString())
                                            );

                                    try {
                                        h.write(String.valueOf(hero.toString()+'\n'));
                                        h.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }


                        }

                    });



            db.collection("Mapa")
                    .whereEqualTo("ID_PARTIDA", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                    try {
                                        m.write(document.getData().get("NIVEL").toString()+',');
                                        m.write(document.getData().get("ID_MAPA").toString());
                                        m.close();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

            db.collection("Mapa")
                    .whereEqualTo("ID_PARTIDA", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Log.d(TAG, document.getId() + " => " + document.getData());
                                    db.collection("Escuadron")
                                            .whereEqualTo("ID_MAPA", document.getData().get("ID_MAPA"))
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @SuppressLint("LongLogTag")
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            //Log.d(TAG, document.getId() + " => " + document.getData());

                                                            escuadon=new Escuadron(Float.parseFloat(document.getData().get("PosX").toString()),Float.parseFloat(document.getData().get("PosY").toString()),document.getData().get("PATH").toString()
                                                                    ,Integer.parseInt(document.getData().get("FILA").toString()),Integer.parseInt(document.getData().get("COLUMNA").toString()),document.getData().get("ID_ESCUADRON").toString()
                                                                    ,Integer.parseInt(document.getData().get("ID").toString()));
                                                            escuadron_cargar.add(escuadon);

                                                        }
                                                        try {
                                                            for (int i=0;i<escuadron_cargar.size();i++){

                                                                es.write(escuadron_cargar.get(i).toString()+'\n');
                                                            }
                                                            es.close();
                                                        }catch (Exception e1) {
                                                            e1.printStackTrace();
                                                        }

                                                    }
                                                }
                                            });
                                }

                            }


                        }
                    });



            db.collection("Mapa")
                    .whereEqualTo("ID_PARTIDA", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Log.d(TAG, document.getId() + " => " + document.getData());

                                    db.collection("Escuadron")
                                            .whereEqualTo("ID_MAPA", document.getData().get("ID_MAPA"))
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @SuppressLint("LongLogTag")
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        resultado=task.getResult().size();
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            Log.d(TAG, document.getId() + " => " + document.getData());


                                                            db.collection("Enemigos")
                                                                    .whereEqualTo("ID_ESCUADRON", document.getData().get("ID_ESCUADRON"))
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                    Log.d(TAG, document.getId() + " => " + document.getData());

                                                                                    Enemigo ene=new Enemigo(document.getData().get("Nombre_E").toString(),Integer.parseInt(document.getData().get("Vida_E").toString())
                                                                                            ,Integer.parseInt(document.getData().get("Ataque_E").toString()),Integer.parseInt(document.getData().get("Defensa_E").toString()),document.getData().get("Path").toString()
                                                                                            ,Integer.parseInt(document.getData().get("FILA").toString()),Integer.parseInt(document.getData().get("COLUMNA").toString()),document.getData().get("ID_ENEMIGO").toString()
                                                                                            ,Integer.parseInt( document.getData().get("ID_GRAFICO").toString()));
                                                                                    enemigo_juego.add(ene);
                                                                                }

                                                                            }
                                                                            try {
                                                                                for (int i=0;i<enemigo_juego.size();i++){

                                                                                    e.write(enemigo_juego.get(i).toString()+'\n');

                                                                                }
                                                                                enemigo_juego.clear();
                                                                                resultado--;
                                                                                if(resultado==0){
                                                                                    e.close();

                                                                                }

                                                                            } catch (IOException e1) {
                                                                                e1.printStackTrace();
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    }

                                                }
                                            });

                                }
                            }

                        }

                    });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }

    }

*/

}
