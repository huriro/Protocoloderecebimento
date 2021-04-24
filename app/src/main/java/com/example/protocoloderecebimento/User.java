package com.example.protocoloderecebimento;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String username;
    public String email;
    public String Condominio;
    public String Funcao;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email,String Condominio,String Funcao) {
        this.username = username;
        this.email = email;
        this.Condominio=Condominio;
        this.Funcao=Funcao;
    }
}
