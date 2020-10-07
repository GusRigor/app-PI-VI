package com.example.piantxjava;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class HTTPService extends AsyncTask<Void, Void, Trashes> {

    private final String id;

    public HTTPService(String id){
        this.id = id;
    }

    @Override
    protected Trashes doInBackground(Void... voids){
        StringBuilder resposta = new StringBuilder();
        try {
            URL url = new URL("https://pix-bec.herokuapp.com/antx/trashes/piv0069");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(5000);
            connection.connect();

            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()){
                resposta.append(scanner.next());
            }
        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
            return new Gson().fromJson(resposta.toString(), Trashes.class);
    }
}
