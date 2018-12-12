package com.dd.andersonngelo.atividade2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.UUID;

public class Cadastro extends AppCompatActivity {
    private Button cadastrar, voltar;
    private EditText nome, telefone, endereco;
    private DatabaseReference firebase;
    private Contato produtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        nome = findViewById(R.id.nome);
        endereco = findViewById(R.id.endereco);
        telefone = findViewById(R.id.telefone);
        cadastrar = findViewById(R.id.cadastrar);
        voltar = findViewById(R.id.voltar);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                produtos = new Contato();
                produtos.setUid(UUID.randomUUID().toString());
                produtos.setNome(nome.getText().toString());
                produtos.setTelefone(telefone.getText().toString());
                produtos.setEndereco(endereco.getText().toString());

                salvarProdutos(produtos);
                nome.setText("");
                telefone.setText("");
                endereco.setText("");
                voltar();
            }
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltar();
            }
        });

    }
    private boolean salvarProdutos(Contato produtos){
        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("Contatos");
            firebase.child(produtos.getUid()).setValue(produtos);
            Toast.makeText(Cadastro.this, "Produto inserido com sucesso!", Toast.LENGTH_LONG).show();
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;

        }
    }
    private void voltar(){
        Intent intent = new Intent(Cadastro.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
