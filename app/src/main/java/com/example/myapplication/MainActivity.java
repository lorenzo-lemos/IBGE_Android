package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.myapplication.util.APISingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText eAno;
    private EditText eEstado;
    private List<String> names;

    Integer[] lista = new Integer[13];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textName);
        eAno = findViewById(R.id.eAno);
        eEstado = findViewById(R.id.eEstado);
        names = new ArrayList<>();
        eAno.setText("2019");
        eEstado.setText("4106902");
    }

    public void btnCarregarEvent(View v){
        carregarDados();
    }

    private void generateRequest(String url, Integer mes){
        String endpoint = url;
        final Integer mesFinal = mes;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, endpoint, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i = 0; i < response.length(); i++){
                        lista[mesFinal] = response.getJSONObject(i).getInt("valor");
                    }
                    showData(lista);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        APISingleton.getInstance(this).addToRequestQueue(request);
    }

    private void showData(Integer[] teste) {
        String names_str = "";
        for(int i = 1 ; i < teste.length; i++) {
            names_str += String.valueOf(i) + " - " + teste[i] + "\n";
        }
        textView.setText(names_str);
    }

    private void carregarDados(){

        lista = new Integer[13];
        for(int i = 1 ; i <= 12; i++) {
            String mes = i <= 9 ? "0" + i: String.valueOf(i);
            String endpoint = "http://www.transparencia.gov.br/api-de-dados/bolsa-familia-por-municipio?mesAno=" + eAno.getText() + mes + "&codigoIbge=" + eEstado.getText() + "&pagina=1";

            generateRequest(endpoint, i);
        }
        showData(lista);
    }
}
