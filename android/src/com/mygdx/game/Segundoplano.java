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
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Map<String, Object> heros;
    private Map<String, Object> partida_usuario;
    private Map<String, Object> escuadron_enemigo;
    private Map<String, Object> enemigo;
    private Map<String, Object> mapa;
    private ArrayList<EscuadronBBDD> escuadron,escu;
    private ArrayList<Enemigo> enemigo_juego;
    private int idPartida;


    private int idMapa;
    private int maximo;
    private int numero;
    private Timestamp timestamp;
    private HeroBBDD hero;



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

                hero=(HeroBBDD)entrada.readObject();
                int longitud=entrada.readInt();

                escuadron.clear();
                escu.clear();
                while(longitud>0) {
                    // EscuadronBBDD escuadron = (EscuadronBBDD) entrada.readObject();
                    escuadron.add((EscuadronBBDD) entrada.readObject());//borrar
                    longitud--;
                }


                int id=entrada.readInt();
                longitud=entrada.readInt();
                while(longitud>0) {
                    // EscuadronBBDD escuadron = (EscuadronBBDD) entrada.readObject();
                    escu.add((EscuadronBBDD) entrada.readObject());//con los escuadrones del mapa o del nuevo mapa
                    longitud--;
                }
                idMapa=id;
                entrada.close();
                entradaDatos.close();
                socket.close();
                direccion.close();



/*
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

                                    }else{
                                        Actualizar_Partida_Mapa_Hero();
                                        // EeliminarPruebA();
                                        // EeliminarPruebB();

                                        // EliminarEscuadron_Enemigos();
                                        //Escuadron();
                                        // Escuadron_Enemigo();

                                        Actualizar_Partida_Mapa_Hero();
                                        EliminarEscuadron();

                                    }
                                }
                            }
                        });*/

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
                                        if(idMapa==Integer.parseInt(document.getData().get("NIVEL").toString())){
                                            Actualiza_Hero_Escuadron();
                                        }else{
                                            Actualizar_Mapa_Escuadrones();
                                        }
                                    }
                                }
                            }
                        });


                // EeliminarPruebA();
                //EliminarEscuadron_Enemigos();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return true;
    }


    public void Actualizar_Mapa_Escuadrones(){
        mapa = new HashMap<>();
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
                                //mapa.put("ID_PARTIDA", document.getData().get("ID_PARTIDA"));
                                //mapa.put("ID_MAPA", idMapa);
                                DocumentReference mapaModificado= db.collection("Mapa").document(document.getId());
                                mapaModificado.update("NIVEL", idMapa);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("Escuadron")
                .whereEqualTo("ID_PARTIDA", user.getUid())
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
        for(int i = 0; i< escu.size(); i++) {

            escuadron_enemigo.put("ID_ESCUADRON", escu.get(i).getId());
            escuadron_enemigo.put("PosX", escu.get(i).getPosicionX());
            escuadron_enemigo.put("PosY", escu.get(i).getPosicionY());
            escuadron_enemigo.put("ID_PARTIDA",user.getUid());


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

        for(int i = 0; i< this.escu.size(); i++){
            for(int j = 0; j< this.escu.get(i).getEnemigos().size(); j++) {
                enemigo.put("ID_ENEMIGO", this.escu.get(i).getEnemigo(j).getId());
                enemigo.put("Ataque_E", this.escu.get(i).getEnemigo(j).getAtaque());
                enemigo.put("Defensa_E", this.escu.get(i).getEnemigo(j).getDefensa());
                enemigo.put("Path", this.escu.get(i).getEnemigo(j).getPath());
                enemigo.put("Vida_E", this.escu.get(i).getEnemigo(j).getVida());
                enemigo.put("Nombre_E", this.escu.get(i).getEnemigo(j).getNombre());
                enemigo.put("ID_ESCUADRON", this.escu.get(i).getId());


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
        heros = new HashMap<>();
        db.collection("Heroes")
                .whereEqualTo("ID_PARTIDA", user.getUid())
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

        for(int i=0;i<escuadron.size();i++) {
            db.collection("Escuadron")
                    .whereEqualTo("ID_ESCUADRON", escuadron.get(i).getId())
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
        for(int i=0;i<escuadron.size();i++) {
            db.collection("Enemigos")
                    .whereEqualTo("ID_ESCUADRON", escuadron.get(i).getId())
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
    }


    public void guardarPartida_Actual(){

        partida_usuario = new HashMap<>();
        partida_usuario.put("ID_PARTIDA", user.getUid());
        partida_usuario.put("FECHA",timestamp.toString());
        //partida_usuario.put("ID_USUARIO", user.getUid());


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
        for(int i = 0; i< escu.size(); i++) {

            escuadron_enemigo.put("ID_ESCUADRON", escu.get(i).getId());
            escuadron_enemigo.put("PosX", escu.get(i).getPosicionX());
            escuadron_enemigo.put("PosY", escu.get(i).getPosicionY());
            escuadron_enemigo.put("ID_PARTIDA",user.getUid());


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

        for(int i = 0; i< this.escu.size(); i++){
            for(int j = 0; j< this.escu.get(i).getEnemigos().size(); j++) {
                enemigo.put("ID_ENEMIGO", this.escu.get(i).getEnemigo(j).getId());
                enemigo.put("Ataque_E", this.escu.get(i).getEnemigo(j).getAtaque());
                enemigo.put("Defensa_E", this.escu.get(i).getEnemigo(j).getDefensa());
                enemigo.put("Path", this.escu.get(i).getEnemigo(j).getPath());
                enemigo.put("Vida_E", this.escu.get(i).getEnemigo(j).getVida());
                enemigo.put("Nombre_E", this.escu.get(i).getEnemigo(j).getNombre());
                enemigo.put("ID_ESCUADRON", this.escu.get(i).getId());


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

    public void Escuadron() {

        escuadron_enemigo = new HashMap<>();
        for (int i = 0; i < escuadron.size(); i++) {

            escuadron_enemigo.put("ID_ESCUADRON", escuadron.get(i).getId());
            escuadron_enemigo.put("PosX", escuadron.get(i).getPosicionX());
            escuadron_enemigo.put("PosY", escuadron.get(i).getPosicionY());
            escuadron_enemigo.put("ID_PARTIDA", user.getUid());


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


    public void EeliminarPruebA(){
        db.collection("Escuadron")
                .whereEqualTo("ID_ESCUADRON", user.getUid())
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

                                db.collection("Enemigos")
                                        .whereEqualTo("ID_ESCUADRON",document.getData().get("ID_ESCUADRON"))
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

                            enemigo=new HashMap<>();

                            for(int i = 0; i< escuadron.size(); i++){
                                for(int j = 0; j<escuadron.get(i).getEnemigos().size(); j++) {
                                    enemigo.put("ID_ENEMIGO", escuadron.get(i).getEnemigo(j).getId());
                                    enemigo.put("Ataque_E", escuadron.get(i).getEnemigo(j).getAtaque());
                                    enemigo.put("Defensa_E", escuadron.get(i).getEnemigo(j).getDefensa());
                                    enemigo.put("Path", escuadron.get(i).getEnemigo(j).getPath());
                                    enemigo.put("Vida_E", escuadron.get(i).getEnemigo(j).getVida());
                                    enemigo.put("Nombre_E", escuadron.get(i).getEnemigo(j).getNombre());
                                    enemigo.put("ID_ESCUADRON", escuadron.get(i).getId());


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



/*
                            escuadron_enemigo = new HashMap<>();
                            for (int i = 0; i < escuadron.size(); i++) {

                                escuadron_enemigo.put("ID_ESCUADRON", escuadron.get(i).getId());
                                escuadron_enemigo.put("PosX", escuadron.get(i).getPosicionX());
                                escuadron_enemigo.put("PosY", escuadron.get(i).getPosicionY());
                                escuadron_enemigo.put("ID_PARTIDA", user.getUid());


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
                            }*/

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });







    }


    public void EeliminarPruebB(){
        enemigo=new HashMap<>();

        for(int i = 0; i< escuadron.size(); i++){
            for(int j = 0; j<escuadron.get(i).getEnemigos().size(); j++) {
                enemigo.put("ID_ENEMIGO", escuadron.get(i).getEnemigo(j).getId());
                enemigo.put("Ataque_E", escuadron.get(i).getEnemigo(j).getAtaque());
                enemigo.put("Defensa_E", escuadron.get(i).getEnemigo(j).getDefensa());
                enemigo.put("Path", escuadron.get(i).getEnemigo(j).getPath());
                enemigo.put("Vida_E", escuadron.get(i).getEnemigo(j).getVida());
                enemigo.put("Nombre_E", escuadron.get(i).getEnemigo(j).getNombre());
                enemigo.put("ID_ESCUADRON", escuadron.get(i).getId());


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

/*
        escuadron_enemigo = new HashMap<>();
        for (int i = 0; i < escuadron.size(); i++) {

            escuadron_enemigo.put("ID_ESCUADRON", escuadron.get(i).getId());
            escuadron_enemigo.put("PosX", escuadron.get(i).getPosicionX());
            escuadron_enemigo.put("PosY", escuadron.get(i).getPosicionY());
            escuadron_enemigo.put("ID_PARTIDA", user.getUid());


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
        }*/
    }

    public void EliminarEscuadron_Enemigos() {

        db.collection("Escuadron")
                .whereEqualTo("ID_PARTIDA", user.getUid())
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


                            enemigo=new HashMap<>();

                            for(int i = 0; i< escuadron.size(); i++){
                                for(int j = 0; j< escuadron.get(i).getEnemigos().size(); j++) {
                                    enemigo.put("ID_ENEMIGO", escuadron.get(i).getEnemigo(j).getId());
                                    enemigo.put("Ataque_E", escuadron.get(i).getEnemigo(j).getAtaque());
                                    enemigo.put("Defensa_E", escuadron.get(i).getEnemigo(j).getDefensa());
                                    enemigo.put("Path", escuadron.get(i).getEnemigo(j).getPath());
                                    enemigo.put("Vida_E", escuadron.get(i).getEnemigo(j).getVida());
                                    enemigo.put("Nombre_E", escuadron.get(i).getEnemigo(j).getNombre());
                                    enemigo.put("ID_ESCUADRON", escuadron.get(i).getId());
                                }
                            }
                            escuadron_enemigo = new HashMap<>();
                            for (int i = 0; i < escuadron.size(); i++) {

                                escuadron_enemigo.put("ID_ESCUADRON", escuadron.get(i).getId());
                                escuadron_enemigo.put("PosX", escuadron.get(i).getPosicionX());
                                escuadron_enemigo.put("PosY", escuadron.get(i).getPosicionY());
                                escuadron_enemigo.put("ID_PARTIDA", user.getUid());


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

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        /*db.collection("Escuadron")
                .whereEqualTo("ID_PARTIDA", user.getUid())
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
                                                        //Log.d(TAG, document.getId() + " => " + document.getData());
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
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        enemigo=new HashMap<>();

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
        }*/


    }

    //esto funciona
   /* public void Actualizar_Partida_Mapa_Hero(){

        mapa = new HashMap<>();
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
                                //mapa.put("ID_PARTIDA", document.getData().get("ID_PARTIDA"));
                                //mapa.put("ID_MAPA", idMapa);
                                DocumentReference mapaModificado= db.collection("Mapa").document(document.getId());
                                mapaModificado.update("NIVEL", idMapa);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        heros = new HashMap<>();
        db.collection("Heroes")
                .whereEqualTo("ID_PARTIDA", user.getUid())
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
    }*/


    public void Actualizar_Partida_Mapa_Hero(){

        mapa = new HashMap<>();
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
                                //mapa.put("ID_PARTIDA", document.getData().get("ID_PARTIDA"));
                                //mapa.put("ID_MAPA", idMapa);
                                DocumentReference mapaModificado= db.collection("Mapa").document(document.getId());
                                mapaModificado.update("NIVEL", idMapa);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        heros = new HashMap<>();
        db.collection("Heroes")
                .whereEqualTo("ID_PARTIDA", user.getUid())
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

        for(int i=0;i<escuadron.size();i++) {
            db.collection("Escuadron")
                    .whereEqualTo("ID_ESCUADRON", escuadron.get(i).getId())
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
        for(int i=0;i<escuadron.size();i++) {
                db.collection("Enemigos")
                        .whereEqualTo("ID_ESCUADRON", escuadron.get(i).getId())
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


    }


/*    public void EliminarEnemigos(){
        db.collection("Enemigos")
                .whereEqualTo("ID_ESCUADRON", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }*/

/*
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
*/

/*
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

                                idPartida=Integer.valueOf(document.getData().get("ID_PARTIDA").toString());
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
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
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
    */

    /*
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
    */
    public void cargarPartida( ){

       /* try {
            Socket socket = new Socket("localhost", 2500);
            OutputStream salida = socket.getOutputStream();
            ObjectOutputStream flujo_salida = new ObjectOutputStream(salida);
        }catch (Exception e){
            e.printStackTrace();
        }
*/

        db.collection("Partida_Actual")
                .whereEqualTo("ID_USUARIO", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                db.collection("Mapa")
                                        .whereEqualTo("ID_PARTIDA", document.getData().get("ID_PARTIDA"))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @SuppressLint("LongLogTag")
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        //document.getData().get("ID_MAPA").toString();
                                                        //enviar atraves del socket
                                                    }

                                                }
                                            }
                                        });
                            }

                        }
                    }
                });

    }

}
