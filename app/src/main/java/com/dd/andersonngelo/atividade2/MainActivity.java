package com.dd.andersonngelo.atividade2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alquimaraalves.teste.Adapter.Adapter;
import com.example.alquimaraalves.teste.Firebase.ConfiguracaoFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public Contato produtosExcluir;
    private ListView listView;
    private ArrayAdapter<Contato> adapter;
    private ArrayList contatoes;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerProdutos;
    private Button voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contatoes = new ArrayList<>();
        listView = findViewById(R.id.lista);
        adapter = new Adapter(this, contatoes);
        listView.setAdapter(adapter);
        listView.setSelector(android.R.color.holo_blue_dark
        );
        firebase = ConfiguracaoFirebase.getFirebase().child("Contatos");

        valueEventListenerProdutos = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contatoes.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Contato produtonovo = dados.getValue(Contato.class);
                    contatoes.add(produtonovo);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                produtosExcluir = (Contato) adapterView.getItemAtPosition(i);

            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.adicionar:
                adicionar();
                break;
            case R.id.editar:
                editar();
                break;
            case R.id.excluir:
                excluir();
                break;

        }
        return true;
    }

    private void adicionar() {
        Intent intent = new Intent(MainActivity.this, Cadastro.class);
        startActivity(intent);
        finish();

    }

    private void excluir() {
        firebase = ConfiguracaoFirebase.getFirebase().child("Contatos");
        firebase.child(produtosExcluir.getUid().toString()).removeValue();
        Toast.makeText(MainActivity.this, "Exclusão bem sucedida!", Toast.LENGTH_LONG).show();

    }

    private void editar() {
//        Intent intent = new Intent(MainActivity.this, editar.class);
        intent.putExtra("Nome", produtosExcluir.getNome());
        intent.putExtra("Telefone", produtosExcluir.getTelefone());
        intent.putExtra("Endereço", produtosExcluir.getEndereco());
        intent.putExtra("uid", produtosExcluir.getUid());
        startActivity(intent);
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerProdutos);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerProdutos);
    }


}
