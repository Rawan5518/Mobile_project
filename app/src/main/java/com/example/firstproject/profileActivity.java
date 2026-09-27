package com.example.firstproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;

public class profileActivity extends AppCompatActivity {
    EditText user, mail, cntry;
    Button edit, logoutt;
    ImageView chooseImage;
    com.google.android.material.imageview.ShapeableImageView profilePict;
    DatabaseReference dbRef;
    FirebaseAuth mAuth;
    String userId;
    String currentImageUri = "default";
    boolean isEditing = false;

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    try {
                        getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } catch (SecurityException e) { e.printStackTrace(); }
                    currentImageUri = uri.toString();
                    profilePict.setImageURI(uri);
                    chooseImage.setVisibility(View.GONE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Profile");
        }
        user = findViewById(R.id.username);
        mail = findViewById(R.id.email);
        cntry = findViewById(R.id.country);
        edit = findViewById(R.id.edits);
        logoutt = findViewById(R.id.logout);
        profilePict = findViewById(R.id.profilepict);
        chooseImage = findViewById(R.id.chooseimage);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();
            mail.setText(mAuth.getCurrentUser().getEmail());
        }
        mail.setEnabled(false);
        user.setEnabled(false);
        cntry.setEnabled(false);
        dbRef = FirebaseDatabase.getInstance("https://myproject-3eece-default-rtdb.firebaseio.com/")
                .getReference("users").child(userId);
        loadUserData();
        chooseImage.setOnClickListener(v -> mGetContent.launch("image/*"));
        profilePict.setOnLongClickListener(v -> {
            currentImageUri = "default";
            profilePict.setImageResource(R.drawable.outline_account_circle_24);
            chooseImage.setVisibility(View.VISIBLE);
            saveUserData();
            return true;
        });
        edit.setOnClickListener(v -> {
            if (!isEditing) {
                isEditing = true;
                edit.setText("SAVE");
                user.setEnabled(true);
                cntry.setEnabled(true);
            } else {
                saveUserData();
            }
        });
        logoutt.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(profileActivity.this, MainActivity.class));
            finish();
        });
    }
    private void loadUserData() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    user.setText(snapshot.child("username").getValue(String.class));
                    cntry.setText(snapshot.child("country").getValue(String.class));

                    if (snapshot.hasChild("profileImage")) {
                        currentImageUri = snapshot.child("profileImage").getValue(String.class);
                        if (!currentImageUri.equals("default")) {
                            profilePict.setImageURI(Uri.parse(currentImageUri));
                            chooseImage.setVisibility(View.GONE);
                        } else {
                            chooseImage.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    private void saveUserData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", user.getText().toString());
        map.put("country", cntry.getText().toString());
        map.put("profileImage", currentImageUri);
        dbRef.updateChildren(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                isEditing = false;
                edit.setText("EDIT");
                user.setEnabled(false);
                cntry.setEnabled(false);
                Toast.makeText(this, "Profile Saved!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}