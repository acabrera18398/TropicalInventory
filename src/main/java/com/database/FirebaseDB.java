/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.database;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author angel
 */
public class FirebaseDB {

    /// Variable para trabajar con sigleton
    static private FirebaseDB firebaseDB = null;
    /// Variable que gaurda la conexión a firebaseinput
    public Firestore db;

    /// Constructor que inicializa la conexión
    private FirebaseDB(String path) {
        try {
            FileInputStream serviceAccount
                    = new FileInputStream(path);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            
            FirebaseApp.initializeApp(options);
                    
            db = FirestoreClient.getFirestore();
        } catch (IOException ex) {
            db = null;
        }
    }

    /// Singleton para no crear muchas instancias
    static public FirebaseDB getFirebaseDB(String path) {
        if (firebaseDB == null) {
            firebaseDB = new FirebaseDB(path);
        }

        return firebaseDB;

    }

    /// Método que consulta las credenciales en la base de datos
    public Object login(String correo, String password) {
        try {
            List<QueryDocumentSnapshot> resultado = db.collection("usuarios").whereEqualTo("correo", correo).whereEqualTo("password", password).get().get().getDocuments();

            if (resultado.isEmpty()) {
                return false;
            } else {
                return resultado.get(0);
            }
        } catch (Exception ex) {
            return false;
        }
    }
}
