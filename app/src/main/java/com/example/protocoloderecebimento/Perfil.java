package com.example.protocoloderecebimento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Perfil extends AppCompatActivity {
    private FirebaseAuth usuario = FirebaseAuth.getInstance();
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    private DrawerLayout drawer;
    String usua;
    TextView perfilnome, perfilcondominio,perfilfuncao,perfilEmail;
    ImageView imagemusuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        usua=usuario.getUid();
        String idusuario=getIntent().getStringExtra("dados");
        perfilnome=findViewById(R.id.txtperfilnome);
        perfilcondominio=findViewById(R.id.txtperfilcondominio);
        perfilEmail=findViewById(R.id.txtperfilemail);
        perfilfuncao=findViewById(R.id.txtperfilfuncao);
        imagemusuario=findViewById(R.id.imageView);

        final DatabaseReference usuariobanco = reference.child("Condominios").child("Sun Place Hills")
                .child("Usuario");
        Query pesquisaAtiva = usuariobanco.orderByChild("Id").equalTo(usuario.getUid());
        pesquisaAtiva.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DatasetUsuario data =  snapshot.getValue(DatasetUsuario.class);
                perfilnome.setText(data.nome);
                perfilcondominio.setText(data.condominios);
                perfilEmail.setText(data.Email);
                perfilfuncao.setText(data.funcao);

                try {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

                    StorageReference imagearefe=storageReference.child(data.imagemusuario);



                    Glide.with(Perfil.this)
                            .using(new FirebaseImageLoader())
                            .load(imagearefe)
                            .into(imagemusuario);
                }catch (Exception e){
                    Toast.makeText(Perfil.this,"não foi possivel carregar a imagem, " +
                                    "pode ser que ela ainda não exista"
                            ,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}