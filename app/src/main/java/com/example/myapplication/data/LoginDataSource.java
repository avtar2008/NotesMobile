package com.example.myapplication.data;

import android.os.AsyncTask;
import com.example.myapplication.data.model.LoggedInUser;
import com.example.myapplication.entity.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    String isValid = null;
    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
//            LoggedInUser fakeUser =
//                    new LoggedInUser(
//                            java.util.UUID.randomUUID().toString(),
//                            "Jane Doe");

//            @SuppressLint("AuthLeak") MongoClientURI mongoUri  = new MongoClientURI("mongodb+srv://adminuser:adminuser@cluster0-at6k0.mongodb.net/test?retryWrites=true&w=majority");
//            MongoClient mongoClient = new MongoClient(mongoUri);
//            DB db = mongoClient.getDB("notes");
//            Set<String> collectionNames = db.getCollectionNames();
//            System.out.println("collections : " + collectionNames.toString());

//            URL url = new URL("");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setDoOutput(true);
//            connection.setConnectTimeout(5000);
//            connection.setReadTimeout(5000);
//            connection.connect();
//            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String content = "", line;
//            while ((line = rd.readLine()) != null) {
//                content += line + "\n";
//            }
//            System.out.println("wadwdaws : " + content);

            isValid =  new GetUrlContentTask().execute("http://10.0.2.2:8080/notes/auth", username, password).get();

            if(isValid.trim().equalsIgnoreCase("yes")){
                LoggedInUser successUser = new LoggedInUser("username", "username");
                return new Result.Success<>(successUser);
            } else {
                return new Result.Error(new Exception("Failed authentication"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    @SneakyThrows
    public void displayMessage(String a){
        System.out.println(a);
    }

    private class GetUrlContentTask extends AsyncTask<String, Integer, String> {

        protected String doInBackground(String... urls) {
            URL url = null;
            String content = "", line;
            try {
                System.out.println("sadwdawdw : " + urls[0]);
                url = new URL(urls[0]);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("Content-Type","application/json");

            UserDetails user = new UserDetails(urls[1], urls[2]);
            OutputStream os = connection.getOutputStream();
            os.write(new ObjectMapper().writeValueAsBytes(user));
            connection.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((line = rd.readLine()) != null) {
                content += line + "\n";
            }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(String result) {
            // this is executed on the main thread after the process is over
            // update your UI here
            displayMessage(result);
        }
    }
}
