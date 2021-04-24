package com.example.protocoloderecebimento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class adastrarusuario extends AppCompatActivity {


    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Condominios");
    String condominio,funcao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adastrarusuario);

        mAuth = FirebaseAuth.getInstance();

        Button cadastrar = findViewById(R.id.btnCadastrar1);
        Button cancelar = findViewById(R.id.btnCancel1);
        final EditText nome = findViewById(R.id.edNome);
        final EditText email = findViewById(R.id.edEmail);
        final EditText Condominio = findViewById(R.id.edCondominio);
        final EditText Funcao=findViewById(R.id.edFuncao);
        final EditText senha = findViewById(R.id.edSenha1);
        final EditText confSenha = findViewById(R.id.edConfSenha);


        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Condominio.getText().toString()!=""){}else{Toast.makeText(adastrarusuario.this, "Condominio vazio", Toast.LENGTH_SHORT).show();return;}
                if(Funcao.getText().toString()!=""){}else{Toast.makeText(adastrarusuario.this, "Função vazio", Toast.LENGTH_SHORT).show();return;}
                if (email.getText().toString() != "") {
                    Log.d("Btn: ", "email");
                    if (senha.getText().toString().equals(confSenha.getText().toString())) {
                        if (senha.getText().length() > 5) {
                            Log.d("Btn: ", "senha");
                            condominio=Condominio.getText().toString();
                            funcao=Funcao.getText().toString();
                            criaUser(email.getText().toString(), senha.getText().toString(), nome.getText().toString(),Condominio.getText().toString(),Funcao.getText().toString());
                        } else {
                            Toast.makeText(adastrarusuario.this, "Senha com minimo de 6 caracteres", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(adastrarusuario.this, "Senhas não conferem.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(adastrarusuario.this, "E-Mail vazio", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nome.setText("");
                email.setText("");
                senha.setText("");
                confSenha.setText("");
                getCurrentFocus().clearFocus();
            }
        });
    }
    private void criaUser(final String email, String password, final String nome,final String Condominio,final String Funcao) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Login: ", "createUserWithEmail:success");
                            FirebaseUser userId = mAuth.getCurrentUser();
                            User user = new User(nome, email,Condominio,Funcao);
                            gravaNome(userId.getUid(), user);
                            entrada();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login: ", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(adastrarusuario.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void gravaNome(String userId, User user){
        myRef.child(condominio).child("Usuario").child(userId).setValue(user);
    }
    private void entrada() {
        Intent intent = new Intent(this, Inicio.class);
        startActivity(intent);
    }
}