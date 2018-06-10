package bbdd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mygdx.game.Datos;
import com.mygdx.game.Enemigo;
import com.mygdx.game.Escuadron;
import com.mygdx.game.Heroes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import BasesDeDatos.EscuadronBBDD;
import BasesDeDatos.HeroBBDD;

import static com.firebase.ui.auth.ui.phone.SubmitConfirmationCodeFragment.TAG;

public class SegundoplanoCopia extends AsyncTask <Void, Integer, Boolean>{





    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Map<String, Object> heros;
    private Map<String, Object> partida_usuario;
    private Map<String, Object> escuadron_enemigo;
    private Map<String, Object> enemigo;
    private Map<String, Object> mapa;
    private ArrayList<EscuadronBBDD> escuadron,escu;
    private ArrayList<Enemigo> enemigo_juego;
    private ArrayList<Escuadron> escuadron_cargar;
    private Escuadron escuadon;
    private Datos d;
    private int idPartida;
    private Activity acti;


    private int idMapa;
    private int maximo;
    private int numero;
    private Timestamp timestamp;
    private Heroes hero;
    private ObjectOutputStream flujo_salida;
    private OutputStream salida;
    private Socket socket;
    private OutputStreamWriter h,m,es,e;
    private int resultado;

    public SegundoplanoCopia(Activity act){
        this.acti=act;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        escuadron=new ArrayList<EscuadronBBDD>();
        escu=new ArrayList<EscuadronBBDD>();
        try {
            while(true) {

                mAuth= FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();
                db=FirebaseFirestore.getInstance();
                timestamp = new Timestamp(System.currentTimeMillis());


                ServerSocket direccion=new ServerSocket(2500);
                Socket socket=direccion.accept();
                InputStream entradaDatos = socket.getInputStream();
                ObjectInputStream entrada =new ObjectInputStream(entradaDatos);


                escuadron.clear();
                escu.clear();
                int estado=entrada.readInt();
                switch (estado) {
                    case 1:
                        entrada.close();
                        entradaDatos.close();
                        socket.close();
                        direccion.close();

                        h= new OutputStreamWriter(
                                acti.openFileOutput("Hero.txt", Context.MODE_PRIVATE));
                        m= new OutputStreamWriter(
                                acti.openFileOutput("Mapa.txt", Context.MODE_PRIVATE));
                        es= new OutputStreamWriter(
                                acti.openFileOutput("Escuadron.txt", Context.MODE_PRIVATE));
                        e= new OutputStreamWriter(
                                acti.openFileOutput("Enemigo.txt", Context.MODE_PRIVATE));

                        cargarPartida();
                        // cerrar();
                        //enviar();
                        break;
                    case 2:
                        d=(Datos)entrada.readObject();

                        int id=entrada.readInt();
                        idMapa=id;
                        entrada.close();
                        entradaDatos.close();
                        socket.close();
                        direccion.close();


                        db.collection("Partida_Actual")
                                .whereEqualTo("ID_PARTIDA", user.getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @SuppressLint("LongLogTag")
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        //si salio bien
                                        if (task.isSuccessful()) {
                                            //si el cursor no devulve nada es que no tiene nada guardado
                                            if(task.getResult().isEmpty()){
                                                guardarPartida_Actual();
                                                Escuadron_Enemigo();

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

                                        //si salio bien
                                        if (task.isSuccessful()) {
                                            //si el cursor no devulve nada es que no tiene nada guardado
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                if(d.getId_mapa().equals(document.getData().get("ID_MAPA").toString())){
                                                    Actualiza_Hero_Escuadron();
                                                }else{
                                                    Actualizar_Mapa_Escuadrones(document.getId());
                                                }
                                            }
                                        }
                                    }
                                });

                        break;

                }

                // EeliminarPruebA();
                //EliminarEscuadron_Enemigos();*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return true;
    }


    public void Actualizar_Mapa_Escuadrones(String idDocumento){

        db.collection("Mapa").document(idDocumento)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });


        mapa = new HashMap<>();
        mapa.put("ID_PARTIDA", user.getUid());
        mapa.put("NIVEL", idMapa);
        mapa.put("ID_MAPA", d.getId_mapa());

        db.collection("Mapa")
                .add(mapa)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });




        db.collection("Escuadron")
                .whereEqualTo("ID_MAPA", d.getId_mapaViejo())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                db.collection("Enemigos")
                                        .whereEqualTo("ID_ESCUADRON", document.getData().get("ID_ESCUADRON"))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @SuppressLint("LongLogTag")
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                                        db.collection("Enemigos").document(document.getId())
                                                                .delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.w(TAG, "Error deleting document", e);
                                                                    }
                                                                });
                                                    }
                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });
                                db.collection("Escuadron").document(document.getId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        escuadron_enemigo=new HashMap<>();
        for(int i = 0; i< d.getEscruadrones().size(); i++) {

            escuadron_enemigo.put("ID_ESCUADRON",d.getEscruadrones().get(i).getId_escuadron());
            escuadron_enemigo.put("PosX", d.getEscruadrones().get(i).getPosicionX());
            escuadron_enemigo.put("PosY", d.getEscruadrones().get(i).getPosicionY());
            escuadron_enemigo.put("FILA", d.getEscruadrones().get(i).getFila());
            escuadron_enemigo.put("COLUMNA", d.getEscruadrones().get(i).getColumna());
            escuadron_enemigo.put("ID_MAPA",d.getId_mapa());
            escuadron_enemigo.put("ID",d.getEscruadrones().get(i).getId());
            escuadron_enemigo.put("PATH",d.getEscruadrones().get(i).getPath());


            db.collection("Escuadron")
                    .add(escuadron_enemigo)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }

        enemigo=new HashMap<>();

        for(int i = 0; i< this.d.getEscruadrones().size(); i++){
            for(int j = 0; j< this.d.getEscruadrones().get(i).getEnemigos().size(); j++) {
                enemigo.put("ID_ENEMIGO", this.d.getEscruadrones().get(i).getEnemigo(j).getId());
                enemigo.put("Ataque_E", this.d.getEscruadrones().get(i).getEnemigo(j).getAtaque());
                enemigo.put("Defensa_E", this.d.getEscruadrones().get(i).getEnemigo(j).getDefensa());
                enemigo.put("Path", this.d.getEscruadrones().get(i).getEnemigo(j).getPath());
                enemigo.put("Vida_E", this.d.getEscruadrones().get(i).getEnemigo(j).getVida());
                enemigo.put("Nombre_E", this.d.getEscruadrones().get(i).getEnemigo(j).getNombre());
                enemigo.put("FILA", this.d.getEscruadrones().get(i).getEnemigo(j).getFila());
                enemigo.put("COLUMNA", this.d.getEscruadrones().get(i).getEnemigo(j).getColumna());
                enemigo.put("ID_GRAFICO", this.d.getEscruadrones().get(i).getEnemigo(j).getIdGradico());
                enemigo.put("ID_ESCUADRON", this.d.getEscruadrones().get(i).getId_escuadron());


                db.collection("Enemigos")
                        .add(enemigo)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        }


    }

    public void Actualiza_Hero_Escuadron(){

        db.collection("Heroes")
                .whereEqualTo("ID_PARTIDA", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DocumentReference her = db.collection("Heroes").document(document.getId());
                                her.update("ATAQUE", d.getHeroe().getAtaque());
                                her.update("DEFENSA", d.getHeroe().getDefensa());
                                her.update("POSICIONY", d.getHeroe().getY());
                                her.update("POSICIONX", d.getHeroe().getX());
                                her.update("VIDA", d.getHeroe().getVida());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        if(d.getBorrar().size()>0) {

            for(int i=0;i<d.getBorrar().size();i++) {


                db.collection("Enemigos")
                        .whereEqualTo("ID_ESCUADRON", d.getBorrar().get(i).getId_escuadron())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        db.collection("Enemigos").document(document.getId())
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error deleting document", e);
                                                    }
                                                });
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }

            for(int i=0;i<d.getBorrar().size();i++) {
                db.collection("Escuadron")
                        .whereEqualTo("ID_ESCUADRON", d.getBorrar().get(i).getId_escuadron())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        db.collection("Escuadron").document(document.getId())
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error deleting document", e);
                                                    }
                                                });
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }


        }



    }


    public void guardarPartida_Actual(){

        partida_usuario = new HashMap<>();
        partida_usuario.put("ID_PARTIDA", user.getUid());
        partida_usuario.put("FECHA",timestamp.toString());
        partida_usuario.put("ID_USUARIO", user.getUid());


        //creamos el documento que guarda en la base de datos
        db.collection("Partida_Actual")
                .add(partida_usuario)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        mapa = new HashMap<>();
        mapa.put("ID_PARTIDA", user.getUid());
        mapa.put("NIVEL", idMapa);
        mapa.put("ID_MAPA", d.getId_mapa());

        db.collection("Mapa")
                .add(mapa)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


        heros = new HashMap<>();
        heros.put("ID_PARTIDA", user.getUid());
        heros.put("ID_PERSONAJE", 1);
        heros.put("NOMBRE","Caballero");
        heros.put("ATAQUE", d.getHeroe().getAtaque());
        heros.put("DEFENSA", d.getHeroe().getDefensa());
        heros.put("POSICIONY",d.getHeroe().getPosicionY());
        heros.put("POSICIONX",d.getHeroe().getPosiconX());
        heros.put("VIDA", d.getHeroe().getVida());


        db.collection("Heroes")
                .add(heros)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        escuadron_enemigo=new HashMap<>();
        for(int i = 0; i< d.getEscruadrones().size(); i++) {

            escuadron_enemigo.put("ID_ESCUADRON",d.getEscruadrones().get(i).getId_escuadron());
            escuadron_enemigo.put("PosX", d.getEscruadrones().get(i).getPosicionX());
            escuadron_enemigo.put("PosY", d.getEscruadrones().get(i).getPosicionY());
            escuadron_enemigo.put("ID_MAPA",d.getId_mapa());
            escuadron_enemigo.put("ID",d.getEscruadrones().get(i).getId());
            escuadron_enemigo.put("FILA",d.getEscruadrones().get(i).getFila());
            escuadron_enemigo.put("COLUMNA",d.getEscruadrones().get(i).getColumna());
            escuadron_enemigo.put("PATH",d.getEscruadrones().get(i).getPath());



            db.collection("Escuadron")
                    .add(escuadron_enemigo)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }

    }

    public void Escuadron_Enemigo(){


        enemigo=new HashMap<>();

        for(int i = 0; i< this.d.getEscruadrones().size(); i++){
            for(int j = 0; j< this.d.getEscruadrones().get(i).getEnemigos().size(); j++) {
                enemigo.put("ID_ENEMIGO", this.d.getEscruadrones().get(i).getEnemigo(j).getId());
                enemigo.put("Ataque_E", this.d.getEscruadrones().get(i).getEnemigo(j).getAtaque());
                enemigo.put("Defensa_E", this.d.getEscruadrones().get(i).getEnemigo(j).getDefensa());
                enemigo.put("Path", this.d.getEscruadrones().get(i).getEnemigo(j).getPath());
                enemigo.put("Vida_E", this.d.getEscruadrones().get(i).getEnemigo(j).getVida());
                enemigo.put("Nombre_E", this.d.getEscruadrones().get(i).getEnemigo(j).getNombre());
                enemigo.put("ID_GRAFICO",this.d.getEscruadrones().get(i).getEnemigo(j).getIdGradico());
                enemigo.put("FILA",this.d.getEscruadrones().get(i).getEnemigo(j).getFila());
                enemigo.put("COLUMNA",this.d.getEscruadrones().get(i).getEnemigo(j).getColumna());
                enemigo.put("ID_ESCUADRON", this.d.getEscruadrones().get(i).getId_escuadron());



                db.collection("Enemigos")
                        .add(enemigo)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        }
    }


    public void cargarPartida( ){
        try {

            enemigo_juego=new ArrayList<>();
            escuadron_cargar=new ArrayList<>();


/*
/data/data/com.mygdx.game/files/prueba_int.txt
 */



            //Esto funciona



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


                                    hero = new Heroes();
                                    hero.setVida(Integer.parseInt(document.getData().get("VIDA").toString()));
                                    hero.setAtaque(Integer.parseInt(document.getData().get("ATAQUE").toString()));
                                    hero.setDefensa(Integer.parseInt(document.getData().get("DEFENSA").toString()));
                                    hero.setPosiconX(Integer.parseInt(document.getData().get("POSICIONX").toString()));
                                    hero.setPosicionY(Integer.parseInt(document.getData().get("POSICIONY").toString()));
                                    try {
                                        h.write(String.valueOf(hero.toString()+'\n'));
                                        h.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    /*try {
                                        fout.write(String.valueOf(hero.toString()));
                                        fout.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }*/

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
                                        m.write(document.getData().get("NIVEL").toString()+'\n');
                                        m.write(document.getData().get("ID_MAPA").toString());
                                        m.close();
                                        // flujo_salida.writeInt((Integer.parseInt(document.getData().get("NIVEL").toString())));
                                        // flujo_salida.writeUTF(document.getData().get("ID_MAPA").toString());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });



           /* db.collection("Mapa")
                    .whereEqualTo("ID_PARTIDA", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    db.collection("Escuadron")
                                            .whereEqualTo("ID_MAPA", document.getData().get("ID_MAPA"))
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @SuppressLint("LongLogTag")
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                                            //float posicionX, float posicionY, String rutaTextura, int fila, int columna, String id_escuadron,int id
                                                            escuadon=new Escuadron(Float.parseFloat(document.getData().get("PosX").toString()),Float.parseFloat(document.getData().get("PosY").toString()),document.getData().get("PATH").toString()
                                                                    ,Integer.parseInt(document.getData().get("FILA").toString()),Integer.parseInt(document.getData().get("COLUMNA").toString()),document.getData().get("ID_ESCUADRON").toString()
                                                                    ,Integer.parseInt(document.getData().get("ID").toString()));

                                                            try {
                                                                es.write(escuadon.toString());
                                                                // es.close();
                                                            } catch (IOException e1) {
                                                                e1.printStackTrace();
                                                            }
                                                            db.collection("Enemigos")
                                                                    .whereEqualTo("ID_ESCUADRON", document.getData().get("ID_ESCUADRON"))
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                                                                    // String nombre, int vida, int ataque, int defensa, String path,int fila,int columna,String id,int idGrafico
                                                                                    Enemigo ene=new Enemigo(document.getData().get("Nombre_E").toString(),Integer.parseInt(document.getData().get("Vida_E").toString())
                                                                                            ,Integer.parseInt(document.getData().get("Ataque_E").toString()),Integer.parseInt(document.getData().get("Defensa_E").toString()),document.getData().get("Path").toString()
                                                                                            ,Integer.parseInt(document.getData().get("FILA").toString()),Integer.parseInt(document.getData().get("COLUMNA").toString()),document.getData().get("ID_ENEMIGO").toString()
                                                                                            ,Integer.parseInt( document.getData().get("ID_GRAFICO").toString()));
                                                                                    escuadon.addEnemigo(new Enemigo(document.getData().get("Nombre_E").toString(),Integer.parseInt(document.getData().get("Vida_E").toString())
                                                                                            ,Integer.parseInt(document.getData().get("Ataque_E").toString()),Integer.parseInt(document.getData().get("Defensa_E").toString()),document.getData().get("Path").toString()
                                                                                            ,Integer.parseInt(document.getData().get("FILA").toString()),Integer.parseInt(document.getData().get("COLUMNA").toString()),document.getData().get("ID_ENEMIGO").toString()
                                                                                            ,Integer.parseInt( document.getData().get("ID_GRAFICO").toString())));
                                                                                    escuadon.addEnemigo(ene);
                                                                                    try {
                                                                                        e.write(ene.toString());
                                                                                        // e.close();
                                                                                    } catch (IOException e1) {
                                                                                        e1.printStackTrace();
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    });
                                                            escuadron_cargar.add(escuadon);

                                                        }

                                                    }
                                                }
                                            });
                                }


                            }

                        }
                    });*/



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
                                                            //float posicionX, float posicionY, String rutaTextura, int fila, int columna, String id_escuadron,int id
                                                            escuadon=new Escuadron(Float.parseFloat(document.getData().get("PosX").toString()),Float.parseFloat(document.getData().get("PosY").toString()),document.getData().get("PATH").toString()
                                                                    ,Integer.parseInt(document.getData().get("FILA").toString()),Integer.parseInt(document.getData().get("COLUMNA").toString()),document.getData().get("ID_ESCUADRON").toString()
                                                                    ,Integer.parseInt(document.getData().get("ID").toString()));


                                                            escuadron_cargar.add(escuadon);
                                                            // es.write(escuadon.toString());

                                                            // es.close();

                                                        }
                                                        try {
                                                            for (int i=0;i<escuadron_cargar.size();i++){
                                                                //System.out.println("Entro********");
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

                                                            //float posicionX, float posicionY, String rutaTextura, int fila, int columna, String id_escuadron,int id
                                                            db.collection("Enemigos")
                                                                    .whereEqualTo("ID_ESCUADRON", document.getData().get("ID_ESCUADRON"))
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                                                                    // String nombre, int vida, int ataque, int defensa, String path,int fila,int columna,String id,int idGrafico
                                                                                    Enemigo ene=new Enemigo(document.getData().get("Nombre_E").toString(),Integer.parseInt(document.getData().get("Vida_E").toString())
                                                                                            ,Integer.parseInt(document.getData().get("Ataque_E").toString()),Integer.parseInt(document.getData().get("Defensa_E").toString()),document.getData().get("Path").toString()
                                                                                            ,Integer.parseInt(document.getData().get("FILA").toString()),Integer.parseInt(document.getData().get("COLUMNA").toString()),document.getData().get("ID_ENEMIGO").toString()
                                                                                            ,Integer.parseInt( document.getData().get("ID_GRAFICO").toString()));
                                                                                    enemigo_juego.add(ene);
                                                                                }
                                                                                /*try {
                                                                                    for (int i=0;i<enemigo_juego.size();i++){

                                                                                        e.write(enemigo_juego.get(i).toString()+'\n');

                                                                                    }
                                                                                    //enemigo_juego.clear();
                                                                                    // e.close();
                                                                                } catch (IOException e1) {
                                                                                    e1.printStackTrace();
                                                                                }*/
                                                                            }
                                                                            try {
                                                                                for (int i=0;i<enemigo_juego.size();i++){
                                                                                    System.out.println(i+"*************");
                                                                                    e.write(enemigo_juego.get(i).toString()+'\n');

                                                                                }
                                                                                enemigo_juego.clear();
                                                                                resultado--;
                                                                                if(resultado==0){
                                                                                    e.close();
                                                                                    //enviar();
                                                                                }
                                                                                //enemigo_juego.clear();
                                                                                //e.close();
                                                                            } catch (IOException e1) {
                                                                                e1.printStackTrace();
                                                                            }
                                                                        }
                                                                    });

                                                        }
                                                    }

                                                }
                                            });

                                    /*try {
                                        e.close();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }*/
                                }


                            }

                        }

                    });



            /*db.collection("Heroes")
                    .whereEqualTo("ID_PARTIDA", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    try {
                                        hero=new Heroes();
                                        hero.setVida(Integer.parseInt(document.getData().get("VIDA").toString()));
                                        hero.setAtaque(Integer.parseInt(document.getData().get("ATAQUE").toString()));
                                        hero.setDefensa(Integer.parseInt(document.getData().get("DEFENSA").toString()));
                                        hero.setPosiconX(Integer.parseInt(document.getData().get("POSICIONX").toString()));
                                        hero.setPosicionY(Integer.parseInt(document.getData().get("POSICIONY").toString()));
                                        flujo_salida.writeObject(hero);
                                        flujo_salida.flush();
                                        flujo_salida.close();
                                        socket.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }

                            }

                        }

                    });

*/
           /* db.collection("Mapa")
                    .whereEqualTo("ID_PARTIDA", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    try {
                                        flujo_salida.writeInt((Integer.parseInt(document.getData().get("NIVEL").toString())));
                                        flujo_salida.writeUTF(document.getData().get("ID_MAPA").toString());
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
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    db.collection("Escuadron")
                                            .whereEqualTo("ID_MAPA", document.getData().get("ID_MAPA"))
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @SuppressLint("LongLogTag")
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                                            //float posicionX, float posicionY, String rutaTextura, int fila, int columna, String id_escuadron,int id
                                                            escuadon=new Escuadron(Float.parseFloat(document.getData().get("PosX").toString()),Float.parseFloat(document.getData().get("PosY").toString()),document.getData().get("PATH").toString()
                                                                    ,Integer.parseInt(document.getData().get("FILA").toString()),Integer.parseInt(document.getData().get("COLUMNA").toString()),document.getData().get("ID_ESCUADRON").toString()
                                                                    ,Integer.parseInt(document.getData().get("ID").toString()));


                                                            db.collection("Enemigos")
                                                                    .whereEqualTo("ID_ESCUADRON", document.getData().get("ID_ESCUADRON"))
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                                                                    // String nombre, int vida, int ataque, int defensa, String path,int fila,int columna,String id,int idGrafico
                                                                                    escuadon.addEnemigo(new Enemigo(document.getData().get("Nombre_E").toString(),Integer.parseInt(document.getData().get("Vida_E").toString())
                                                                                            ,Integer.parseInt(document.getData().get("Ataque_E").toString()),Integer.parseInt(document.getData().get("Defensa_E").toString()),document.getData().get("Path").toString()
                                                                                            ,Integer.parseInt(document.getData().get("FILA").toString()),Integer.parseInt(document.getData().get("COLUMNA").toString()),document.getData().get("ID_ENEMIGO").toString()
                                                                                            ,Integer.parseInt( document.getData().get("ID_GRAFICO").toString())));
                                                                                }
                                                                            }
                                                                        }
                                                                    });
                                                            escuadron_cargar.add(escuadon);

                                                        }
                                                        try {
                                                            flujo_salida.writeObject(escuadron_cargar);
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    });


*/
            // d=new Datos(hero)
            //flujo_salida.flush();
            //flujo_salida.close();
            //socket.close();

            // e.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }

    }

    public void enviar(){
        try {
            socket=new Socket("localhost",2600);
            salida=socket.getOutputStream();
            flujo_salida = new ObjectOutputStream( salida);
            flujo_salida.writeUTF("Acabo");
            flujo_salida.flush();
            flujo_salida.close();

            socket.close();



        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
