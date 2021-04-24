package com.example.protocoloderecebimento;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
//import com.itextpdf.text.pdf.AcroFields;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class Inicio extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    //TextView nomeperfil;
    private FirebaseAuth usuario = FirebaseAuth.getInstance();
    private AppBarConfiguration mAppBarConfiguration;
    private AlertDialog alerta;
    private DrawerLayout drawer;
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        String idusuario1;
        if (usuario.getCurrentUser() != null) {

        }


        View headerView = navigationView.getHeaderView(0);
        final TextView nomeperfil = (TextView) headerView.findViewById(R.id.textView);
               final TextView nomecondo =(TextView) headerView.findViewById(R.id.txtcondominio);
        final ImageView imagemusuario =(ImageView) headerView.findViewById(R.id.imageView);

        final DatabaseReference usuariobanco = reference.child("Condominios").child("Sun Place Hills")
                .child("Usuario");
        Query pesquisaAtiva = usuariobanco.orderByChild("Id").equalTo(usuario.getUid());
        pesquisaAtiva.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DatasetUsuario datasetUsuario = snapshot.getValue(DatasetUsuario.class);
                nomeperfil.setText(datasetUsuario.nome);
                nomecondo.setText(datasetUsuario.condominios);
                //usuar=datasetUsuario.nome;
               // nomeCondominio.setText("Condomínio: "+datasetUsuario.condominios);
                //funcaousuario.setText("Função: "+datasetUsuario.funcao);
                try {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

                    StorageReference imagearefe=storageReference.child(datasetUsuario.imagemusuario);



                    Glide.with(Inicio.this)
                            .using(new FirebaseImageLoader())
                            .load(imagearefe)
                            .into(imagemusuario);
                }catch (Exception e){
                    Toast.makeText(Inicio.this,"não foi possivel carregar a imagem, " +
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sair:
                deslogarUsuario();
                startActivity(new Intent(getApplicationContext(),Telalogin.class));
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);

    }



    void deslogarUsuario(){
        try {usuario.signOut();

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.nav_logout: {
                alerta("Logout","Deseja deslogar do app?","logout");

                break;
            }
            case  R.id.menu_sair:{
                alerta("Sair","Deseja sair do app?","sair");
                break;
            }
            case  R.id.nav_home:{
                break;
            }
            case R.id.nav_perfil:{
                startActivity(new Intent(getApplicationContext(),Perfil.class));

                break;

            }
            case R.id.nav_Cadastrar:{
                startActivity(new Intent(getApplicationContext(),adastrarusuario.class));
                break;
            }


        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    public void alerta(String logout , String setMessage, final String parameto){
        AlertDialog.Builder builder = new AlertDialog.Builder(Inicio.this);
        //define o titulo
        builder.setTitle(logout);
        //define a mensagem
        builder.setMessage(setMessage );
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

         if(parameto.contains("logout")){
                deslogarUsuario();

                startActivity(new Intent(getApplicationContext(),Telalogin.class));
                finish();
         }else {
            System.exit(0);
         }

                // backToMain();
                //Toast.makeText(CorrespondeciasAtivas.this, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                startActivity(new Intent(getApplicationContext(),Inicio.class));
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }@Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}