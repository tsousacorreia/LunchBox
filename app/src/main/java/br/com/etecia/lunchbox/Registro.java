package br.com.etecia.lunchbox;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registro extends AppCompatActivity {


    EditText txtPassword, txtEmail, txtPasswordConfirm;
    Button btnReg, btnVoltar;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView txtPasswordStr, txtPasswordHint;
    @Override
    public void onStart() {
        super.onStart();
        // checar se o usuario ja esta logado e abrir a tela de acordo
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.registro_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        txtEmail = findViewById(R.id.email);
        txtPassword = findViewById(R.id.password);
        btnReg = findViewById(R.id.btnReg);
        progressBar = findViewById(R.id.progressBar);
        btnVoltar = findViewById(R.id.btnVoltar);
        txtPasswordConfirm = findViewById(R.id.passwordConfirm);
        txtPasswordStr = findViewById(R.id.txtPasswordStr);


        txtPasswordStr.setVisibility(View.GONE);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password, passwordConfirm;
                email = String.valueOf(txtEmail.getText());
                password = String.valueOf(txtPassword.getText());
                passwordConfirm = String.valueOf((txtPasswordConfirm.getText()));

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Registro.this, "Insira um email", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Registro.this, "Insira uma senha", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (password.length() < 8) {
                    txtPasswordStr.setText("A senha deve conter pelo menos 8 caracteres!");
                    txtPasswordStr.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (!password.matches("^(?=.*[a-zA-Z]).+$")) {
                    txtPasswordStr.setText("A senha deve conter pelo menos uma letra!");
                    txtPasswordStr.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (!password.matches(".*\\d.*")) {
                    txtPasswordStr.setText("A senha deve conter pelo menos um número!");
                    txtPasswordStr.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (!password.equals(passwordConfirm)){
                    Toast.makeText(Registro.this, "Digite senhas iguais", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }



                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(Registro.this, "Conta criada com sucesso.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(Registro.this, "Criação de conta falhou.", Toast.LENGTH_SHORT).show();


                        }


                    }
                });

            }
        });
    }


}