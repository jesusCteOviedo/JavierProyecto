package com.mygdx.game;

import android.annotation.SuppressLint;
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

import java.net.*;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import BasesDeDatos.EscuadronBBDD;
import BasesDeDatos.HeroBBDD;

import static com.firebase.ui.auth.ui.phone.SubmitConfirmationCodeFragment.TAG;

public class Segundoplano extends AsyncTask <Void, Integer, Boolean>{





    private FirebaseFirestore db;
    private FirebaseFirestore da;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Map<String, Object> heros;
    private Map<String, Object> partida_usuario;
    private Map<String, Object> escuadron_enemigo;
    private Map<String, Object> enemigo;
    private Map<String, Object> mapa;
    private ArrayList<EscuadronBBDD> escuadron;
    private ArrayList<Enemigo> enemigo_juego;
    private int idPartida;

    private String idEnemigo,IdEscuadron,IdHero;
    private int idMapa;
    private int maximo;
    private int numero;
    private Timestamp timestamp;
    private boolean encontrado;
    private HeroBBDD hero;



    @Override
    protected Boolean doInBackground(Void... voids) {
        escuadron=new ArrayList<EscuadronBBDD>();
        try {
            while(true) {

                mAuth= FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();
                db=FirebaseFirestore.getInstance();
                da=FirebaseFirestore.getInstance();
                timestamp = new Timestamp(System.currentTimeMillis());


                ServerSocket direccion=new ServerSocket(2500);
                Socket socket=direccion.accept();
                InputStream entradaDatos = socket.getInputStream();
                ObjectInputStream entrada =new ObjectInputStream(entradaDatos);

                hero=(HeroBBDD)entrada.readObject();
                int longitud=entrada.readInt();

                escuadron.clear();
                while(longitud>0) {
                    // EscuadronBBDD escuadron = (EscuadronBBDD) entrada.readObject();
                    escuadron.add((EscuadronBBDD) entrada.readObject());
                    longitud--;
                }
                int id=entrada.readInt();
                idMapa=id;
                entrada.close();
                entradaDatos.close();
                socket.close();
                direccion.close();




                db.collection("Partida_Actual")
                        .whereEqualTo("ID_USUARIO", user.getUid())
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
                                      //  Escuadron_Mapa();
                                    }else{
                                        Actualizar_Partida_Mapa_Hero();
                                        EliminarEscuadron();
                                    }
                                }
                            }
                        });


            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return true;
    }




    public void guardarPartida_Actual(){


        partida_usuario = new HashMap<>();


        db.collection("Partida_Actual")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d(TAG, document.getId() + " => " + document.getData().get("ID_PARTIDA"));
                                maximo= Integer.valueOf(document.getData().get("ID_PARTIDA").toString());
                                if(maximo>numero){
                                    numero=maximo;
                                }
                            }
                            numero++;

                            partida_usuario.put("ID_PARTIDA", numero);
                            partida_usuario.put("FECHA",timestamp.toString());
                            partida_usuario.put("ID_USUARIO", user.getUid());


                            //creamos el documento que guarda en la base de datos
                            db.collection("Partida_Actual")
                                    .add(partida_usuario)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });

                            mapa = new HashMap<>();
                            mapa.put("ID_PARTIDA", numero);
                            mapa.put("NIVEL", idMapa);

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
                            heros.put("ID_PARTIDA", numero);
                            heros.put("ID_PERSONAJE", 1);
                            heros.put("NOMBRE","Caballero");
                            heros.put("ATAQUE", hero.getAtaque());
                            heros.put("DEFENSA", hero.getDefensa());
                            heros.put("POSICIONY",hero.getPosicionY());
                            heros.put("POSICIONX",hero.getPosiconX());
                            heros.put("VIDA", hero.getVida());


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
                            for(int i = 0; i< escuadron.size(); i++) {

                                escuadron_enemigo.put("ID_ESCUADRON", escuadron.get(i).getId());
                                escuadron_enemigo.put("PosX", escuadron.get(i).getPosicionX());
                                escuadron_enemigo.put("PosY", escuadron.get(i).getPosicionY());
                                escuadron_enemigo.put("ID_PARTIDA",numero);


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



                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }




    public void EliminarEscuadron() {
        db.collection("Partida_Actual")
                .whereEqualTo("ID_USUARIO", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());


                                db.collection("Escuadron")
                                        .whereEqualTo("ID_PARTIDA", document.getData().get("ID_PARTIDA"))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



    }


    public void Escuadron_Mapa(){

        db.collection("Partida_Actual")
                .whereEqualTo("ID_USUARIO", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                idPartida=Integer.valueOf(document.getData().get("ID_PARTIDA").toString());


                            }


                            escuadron_enemigo=new HashMap<>();
                            for(int i = 0; i< escuadron.size(); i++) {

                                escuadron_enemigo.put("ID_ESCUADRON", escuadron.get(i).getId());
                                escuadron_enemigo.put("PosX", escuadron.get(i).getPosicionX());
                                escuadron_enemigo.put("PosY", escuadron.get(i).getPosicionY());
                                escuadron_enemigo.put("ID_PARTIDA",idPartida);


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



                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



/*
        escuadron_enemigo=new HashMap<>();
        for(int i = 0; i< this.escuadron.size(); i++) {

            escuadron_enemigo.put("ID_ESCUADRON", this.escuadron.get(i).getId());
            escuadron_enemigo.put("PosX", this.escuadron.get(i).getPosicionX());
            escuadron_enemigo.put("PosY", this.escuadron.get(i).getPosicionY());
            escuadron_enemigo.put("ID_PARTIDA",idMapa);


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
        */
    }


    public void Escuadron_Enemigo(){


        enemigo=new HashMap<>();
        //String nombre, int vida, int ataque, int defensa, String path,int fila,int columna,int id
        for(int i = 0; i< this.escuadron.size(); i++){
            for(int j = 0; j< this.escuadron.get(i).getEnemigos().size(); j++) {
                enemigo.put("ID_ENEMIGO", this.escuadron.get(i).getEnemigo(j).getId());
                enemigo.put("Ataque_E", this.escuadron.get(i).getEnemigo(j).getAtaque());
                enemigo.put("Defensa_E", this.escuadron.get(i).getEnemigo(j).getDefensa());
                enemigo.put("Path", this.escuadron.get(i).getEnemigo(j).getPath());
                enemigo.put("Vida_E", this.escuadron.get(i).getEnemigo(j).getVida());
                enemigo.put("Nombre_E", this.escuadron.get(i).getEnemigo(j).getNombre());
                enemigo.put("ID_ESCUADRON", this.escuadron.get(i).getId());


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

    public void Actualizar_Partida_Mapa_Hero(){

        mapa = new HashMap<>();
        heros=new HashMap<>();


        db.collection("Partida_Actual")
                .whereEqualTo("ID_USUARIO", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                db.collection("Mapa")
                                        .whereEqualTo("ID_PARTIDA", document.getData().get("ID_PARTIDA"))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @SuppressLint("LongLogTag")
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                                        mapa.put("ID_PARTIDA", document.getData().get("ID_PARTIDA"));
                                                        mapa.put("ID_MAPA", idMapa);
                                                        db.collection("Mapa").document(document.getId()).set(mapa);

                                                        /*heros.put("ID_PARTIDA", document.getData().get("ID_PARTIDA"));
                                                        heros.put("ID_PERSONAJE", 1);
                                                        heros.put("NOMBRE","Caballero");
                                                        heros.put("ATAQUE", hero.getAtaque());
                                                        heros.put("DEFENSA", hero.getDefensa());
                                                        heros.put("POSICIONY",hero.getPosicionY());
                                                        heros.put("POSICIONX",hero.getPosiconX());
                                                        heros.put("VIDA", hero.getVida());

                                                        db.collection("Heroes").document(document.getId()).set(heros);*/
                                                    }
                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });


                                db.collection("Heroes")
                                        .whereEqualTo("ID_PARTIDA", document.getData().get("ID_PARTIDA"))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @SuppressLint("LongLogTag")
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                        heros.put("ID_PARTIDA", document.getData().get("ID_PARTIDA"));
                                                        heros.put("ID_PERSONAJE", 1);
                                                        heros.put("NOMBRE","Caballero");
                                                        heros.put("ATAQUE", hero.getAtaque());
                                                        heros.put("DEFENSA", hero.getDefensa());
                                                        heros.put("POSICIONY",hero.getPosicionY());
                                                        heros.put("POSICIONX",hero.getPosiconX());
                                                        heros.put("VIDA", hero.getVida());

                                                        db.collection("Heroes").document(document.getId()).set(heros);
                                                    }
                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void cargarPartida(String []arg){

    }

}
