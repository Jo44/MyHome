package fr.my.home.tool;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;

/**
 * Classe OAuth permettant de gérer les authentification OAuth 2.0 auprès de Google
 * 
 * @author Jonathan
 * @version 1.0
 * @since 16/07/2021
 */
public class OAuth {

	// Attributes

	// Définis une instance globale de HTTP Transport
	public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	// Définis une instance globale de JSON Factory
	public static final JsonFactory JSON_FACTORY = new JacksonFactory();
	// Sous-répertoire du dossier User utilisé pour stocker les tokens OAuth
	private static final String CREDENTIALS_DIRECTORY = ".oauth-credentials/myhome";

	// Methods

	/**
	 * Authorise l'application à accéder aux données protégées de l'utilisateur
	 *
	 * @param scopes
	 * @param credentialDatastore
	 * @return Credential
	 */
	public static Credential authorize(List<String> scopes, String credentialDatastore) throws IOException {

		// Charge les client secrets
		Reader clientSecretReader = new InputStreamReader(OAuth.class.getResourceAsStream("/oauth/client_secrets.json"));
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader);

		// Création du DataStore de credentials
		FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(new File(System.getProperty("user.home") + "/" + CREDENTIALS_DIRECTORY));
		DataStore<StoredCredential> datastore = fileDataStoreFactory.getDataStore(credentialDatastore);

		// Création du GoogleAuthorizationCodeFlow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes)
				.setCredentialDataStore(datastore).build();

		// Construis le serveur local et associe le port 8090
		LocalServerReceiver localReceiver = new LocalServerReceiver.Builder().setPort(8090).build();

		// Authorisation
		return new AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user");
	}

}
