package eu.ase.ro.licenta;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    private RadioGroup rgSelection;
    private RadioButton rbCurier, rbClient;
    private EditText etEmail, etPassword;
    private Button btn_login, btn_register;
    private String checked_rb;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializareVariabile();

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser != null){
                    verificareRadioButton();
                    if(checked_rb == "Curier") {
                        Intent intent_curier = new Intent(getApplicationContext(), CurierMapActivity.class);
                        startActivity(intent_curier);
                    } else {
                        Intent intent_client = new Intent(getApplicationContext(), ClientMainActivity.class);
                        startActivity(intent_client);
                    }
                    finish();
                }
            }
        };

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseRegister();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseLogin();
            }
        });
    }

    private void firebaseLogin() {
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        if(TextUtils.isEmpty(email)) {
            etEmail.setError("Email trebuie completat");
            return;
        }
        if(TextUtils.isEmpty(password)) {
            etPassword.setError("Parola trebuie completata");
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Eroare login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void firebaseRegister() {
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        if(TextUtils.isEmpty(email)) {
            etEmail.setError("Email trebuie completat");
            return;
        }
        if(TextUtils.isEmpty(password)) {
            etPassword.setError("Parola trebuie completata");
            return;
        }

        verificareRadioButton();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Eroare inregistrare", Toast.LENGTH_SHORT).show();
                } else {
                    String userId = firebaseAuth.getCurrentUser().getUid();
                    DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Utilizator").child(checked_rb).child("user_id");
                    currentUserDb.setValue(userId);
                    Toast.makeText(MainActivity.this, "User creat", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    private void initializareVariabile() {
        rbCurier = findViewById(R.id.rb_user_courier);
        rbClient = findViewById(R.id.rb_user_client);
        rgSelection = findViewById(R.id.rg_user);
        etEmail = findViewById(R.id.edit_text_email);
        etPassword = findViewById(R.id.edit_text_password);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        verificareRadioButton();
    }

    private void verificareRadioButton() {
        if(rgSelection.getCheckedRadioButtonId() == -1) {
            Toast.makeText(MainActivity.this, "Alegeti tipul de utilizator", Toast.LENGTH_SHORT).show();
        }
        else {
            if (rbCurier.isChecked()) {
                checked_rb = "Curier";
            } else {
                checked_rb = "Client";
            }
        }
    }

}