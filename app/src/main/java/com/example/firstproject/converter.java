package com.example.firstproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.HashMap;

public class converter extends AppCompatActivity {
    Spinner spinner, spinner1;
    EditText amount;
    Button button;
    HashMap<String, Double> rates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.converter_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinner = findViewById(R.id.spnr);
        spinner1 = findViewById(R.id.spnr1);
        amount = findViewById(R.id.amount);
        button = findViewById(R.id.button);
        String[] currency = {"USD", "LBP", "EUR", "GBP", "IRR", "IQD", "TRY"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, currency);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner1.setAdapter(adapter);

        rates = new HashMap<>();
        rates.put("USD", 1.0);
        rates.put("EUR", 0.92);
        rates.put("GBP", 0.79);
        rates.put("LBP", 89.000);
        rates.put("IQD", 1.410);
        rates.put("IRR", 142.000);
        rates.put("TRY", 42.67);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String amnt = amount.getText().toString().trim();
                if (amnt.isEmpty()) {
                    Toast.makeText(converter.this, "Please enter amount", Toast.LENGTH_SHORT).show();
                    return;}
                double amountVal;
                try {
                    amountVal = Double.parseDouble(amnt);
                    } catch (NumberFormatException e) {
                    Toast.makeText(converter.this, "Invalid number", Toast.LENGTH_SHORT).show();
                    return;}
                String from = spinner.getSelectedItem().toString();
                String to = spinner1.getSelectedItem().toString();
                double result = amountVal / rates.get(from) * rates.get(to);
                TextView resultView = findViewById(R.id.result);
                resultView.setText(String.format("%.2f %s", result, to));
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.converter), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_converter, menu);
        return true; }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_profile) {
            Intent intent = new Intent(converter.this, profileActivity.class);
            intent.putExtra("username", "Authenticated User");
            startActivity(intent);
            return true;}
        if (id == R.id.menu_chat) {
            Intent intent = new Intent(converter.this, ChatPoolActivity.class);
            startActivity(intent);
            return true;}
        return super.onOptionsItemSelected(item);
    }
}