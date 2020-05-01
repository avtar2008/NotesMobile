package com.example.myapplication;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.myapplication.entity.Note;
import com.example.myapplication.entity.UserDetails;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.text.SimpleDateFormat;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private final String NOTES_URL = "http://10.0.2.2:8080/notes/";

    @SneakyThrows
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle bundle = getIntent().getExtras();
        String user = bundle.getString("userName");
        final TextView usernameTextView = findViewById(R.id.userNameView);
        usernameTextView.setText("Welcome, " + user);



        String notes = new GetUrlContentTask().execute(NOTES_URL + user).get();
        List<Note> noteList = new ObjectMapper().readValue(notes, new TypeReference<List<Note>>(){});
        System.out.println("sadwdawdwa : " + noteList);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.homeLL);
        StringBuffer sb = new StringBuffer();
        int counter = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        for (Note note : noteList) {
            sb.append(counter + ", ");
            TextView noteView = new TextView(this);
            if(note.getText() != null){
                sb.append(note.getText() + ", ");
            }

            if(note.getLast_updated() != null){
                sb.append("Updated :" + sdf.format(note.getLast_updated()) + ", ");
            }

            noteView.setText(sb.toString());
//            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
//            noteView.setLayoutParams(params);


            GradientDrawable gd = new GradientDrawable();
            gd.setColor(0xFF00FF00); // Changes this drawbale to use a single color instead of a gradient
            gd.setCornerRadius(25);
            gd.setStroke(1, 0xFFFF0000);
            noteView.setBackground(gd);
            noteView.setPadding(30, 30, 30, 30);
            noteView.setTextSize(30);
            linearLayout.addView(noteView);
            sb.delete(0, sb.length());
            counter++;
        }
    }

    private class GetUrlContentTask extends AsyncTask<String, Integer, String> {

        protected String doInBackground(String... urls) {
            URL url = null;
            String content = "", line;
            try {
                System.out.println("sadwdawdw : " + urls[0]);
                url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                //connection.setRequestProperty("Content-Type","application/json");

                //UserDetails user = new UserDetails(urls[1], urls[2]);
                //OutputStream os = connection.getOutputStream();
                //os.write(new ObjectMapper().writeValueAsBytes(user));
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

    }
}


