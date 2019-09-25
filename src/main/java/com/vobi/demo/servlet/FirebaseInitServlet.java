package com.vobi.demo.servlet;


import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentChange;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;



/**
 * Servlet implementation class FirebaseInitServlet
 */
@WebServlet("/FirebaseInitServlet")
public class FirebaseInitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final static Logger log=LoggerFactory.getLogger(FirebaseInitServlet.class);
	private final static String COLECCION_ITEMS_PRODUCTO = "products";
	
	private Firestore db=null;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FirebaseInitServlet() {
        super();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init();

		try {
			log.info("Inicializando FirebaseInitServlet...");

		
			String ruta = "/angular-firestore-ac544-firebase-adminsdk-mcfjr-5f42e65904.json";

			InputStream serviceAccount = FirebaseInitServlet.class.getResourceAsStream(ruta);

			if (serviceAccount == null) {
				throw new Exception("No se pudo encontrar la llave de firebase en  classpath:" + ruta);
			}

			// Use a service account
			GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
		    FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder().setCredentials(credentials).build();
		    Firestore db = firestoreOptions.getService();
		    this.db = db;
		    
			// Listener para clientes
			suscribirListenerItem(db, "clieId", COLECCION_ITEMS_PRODUCTO);
		} catch (Exception e) {
			log.error("Error inicializando FirebaseInitServlet: ", e);
		}
	}
	
	private void suscribirListenerItem(final Firestore db, final String keyName, final String nombreColeccion)throws Exception {
		db.collection(nombreColeccion).addSnapshotListener(new EventListener<QuerySnapshot>() {

			@Override
			public void onEvent(QuerySnapshot value, FirestoreException error) {
				List<DocumentChange> dcs = value.getDocumentChanges();
				for (DocumentChange dc : dcs) {

					QueryDocumentSnapshot queryDocumentSnapshot = dc.getDocument();
					Map<String, Object> data = queryDocumentSnapshot.getData();

					String tipoEvento = dc.getType().name();
					String datos = data.toString();

					log.info(tipoEvento + ": " + datos);

					try {
						// ADDED, MODIFIED, REMOVED
						if (tipoEvento.equals("ADDED")) {
							log.info("ADDED");
							log.info(datos);
						}else if (tipoEvento.equals("MODIFIED")) {
							log.info("MODIFIED");
							log.info(datos);
						}else if (tipoEvento.equals("REMOVED")) {
							log.info("REMOVED");
							log.info(datos);
						}
					}catch (Exception e) {
						log.error("Error", e);
					}
				
				}
			}
			
			
		});
		
	}
}
