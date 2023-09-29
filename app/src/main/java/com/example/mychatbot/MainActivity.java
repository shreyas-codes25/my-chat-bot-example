package com.example.mychatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView textView;
    ImageButton sendButton;
    EditText enter;
    List<Message> msgList;
    MessageAdapter msgadap;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recycle_layout);
        textView=findViewById(R.id.welcome_text);
        sendButton=findViewById(R.id.send_btn);
        enter=findViewById(R.id.text_box);
        msgList = new ArrayList<>();

        //msgadapter
        msgadap=new MessageAdapter(msgList);
        recyclerView.setAdapter(msgadap);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);


        sendButton.setOnClickListener((v)->{
            String question = enter.getText().toString().trim();
            //Toast.makeText(this,question,Toast.LENGTH_LONG).show();
            addToChat(question,Message.SEND_BY_ME);
            enter.setText("");
            callAPI(question);
            textView.setVisibility(View.GONE);
        });

    }
    void addToChat(String msg,String sendBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                msgList.add(new Message(msg,sendBy));
                msgadap.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(msgadap.getItemCount());
            }
        });

    }
    void addResponse(String response){
        addToChat(response,Message.SEND_BY_BOT);
    }
    void callAPI(String question){
        JSONObject jsonbody = new JSONObject();
        try{
            jsonbody.put("model","text-davinci-003");
            jsonbody.put("promt",question);
            jsonbody.put("max-tokens",4000);
            jsonbody.put("tempreture",0);
        }catch(Exception e){e.printStackTrace();}
        RequestBody body = RequestBody.create(jsonbody.toString(),JSON);
        //SSLUtils.trustAllCertificates();

        Request req = new Request.Builder()
                .url("https://api.opeanai.com/v1/completions")
                .header("Authorization","Bearer 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")
                .post(body)
                .build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("failed to load the response because "+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){

                    JSONObject jsonobject = null;
                    try {
                        jsonobject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonobject.getJSONArray("choices");
                        String res = jsonArray.getJSONObject(0).getString("text");
                        addResponse(res.trim());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
                else{
                    addResponse("failed to load the response because "+response.body().toString());
                }
            }
        });
    }
}