package bbdd;

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
import com.mygdx.game.Datos;
import com.mygdx.game.Enemigo;

import java.io.InputStream;
import java.io.ObjectInputStream;
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
    private Datos d;
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


                escuadron.clear();
                escu.clear();

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
            escuadron_enemigo.put("ID_MAPA",d.getId_mapa());


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
