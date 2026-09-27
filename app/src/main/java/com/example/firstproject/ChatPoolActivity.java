package com.example.firstproject;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class ChatPoolActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText messageEdit;
    private Button sendBtn;
    private DatabaseReference dbRef, userRef;
    private List<Message> messageList;
    private ChatAdapter adapter;
    private String myUsername = "User";
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_pool);

        Toolbar toolbar = findViewById(R.id.chatToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chat Pool");
        }

        recyclerView = findViewById(R.id.chatRecyclerView);
        messageEdit = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.sendBtn);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance("https://myproject-3eece-default-rtdb.firebaseio.com/").getReference("messages");
        userRef = FirebaseDatabase.getInstance("https://myproject-3eece-default-rtdb.firebaseio.com/").getReference("users").child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot s) {
                if (s.exists()) myUsername = s.child("username").getValue(String.class);
            }
            @Override public void onCancelled(@NonNull DatabaseError e) {}
        });

        messageList = new ArrayList<>();
        adapter = new ChatAdapter(messageList);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        recyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                if (messageList.size() > 0) {
                    recyclerView.postDelayed(() ->
                            recyclerView.scrollToPosition(messageList.size() - 1), 100);
                }
            }
        });

        sendBtn.setOnClickListener(v -> {
            String txt = messageEdit.getText().toString().trim();
            if (!txt.isEmpty()) {
                String msgId = dbRef.push().getKey();
                Message msg = new Message(msgId, uid, myUsername, txt, System.currentTimeMillis());
                if (msgId != null) dbRef.child(msgId).setValue(msg);
                messageEdit.setText("");
            }
        });

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot d : snapshot.getChildren()) {
                    Message m = d.getValue(Message.class);
                    if (m != null) messageList.add(m);
                }
                adapter.notifyDataSetChanged();

                if (messageList.size() > 0) {
                    recyclerView.scrollToPosition(messageList.size() - 1);
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError e) {}
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Clear Chat");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 1) { // CLEAR ALL
            dbRef.removeValue();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}