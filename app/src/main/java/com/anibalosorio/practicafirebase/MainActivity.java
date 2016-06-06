package com.anibalosorio.practicafirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText id, nombre, cantidad, valor, contenido;
    Button agregar, actualizar, eliminar, inventario, venta, ganancias, buscar, limpiar;
    Integer contDatos=0;
    private static final String FIREBASE_URL="https://practicafirebaseani.firebaseio.com";
    private Firebase firebasedatos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id=(EditText)findViewById(R.id.eId);
        nombre=(EditText)findViewById(R.id.eNombre);
        cantidad=(EditText)findViewById(R.id.eCantidad);
        valor=(EditText)findViewById(R.id.eValor);
        contenido=(EditText)findViewById(R.id.eContenido);

        agregar=(Button)findViewById(R.id.bAgregar);
        actualizar=(Button)findViewById(R.id.bActualizar);
        eliminar=(Button)findViewById(R.id.bEliminar);
        inventario=(Button)findViewById(R.id.bInventario);
        ganancias=(Button)findViewById(R.id.bGanancias);
        buscar=(Button)findViewById(R.id.bBuscar);
        limpiar=(Button)findViewById(R.id.bLimpiar);

        Firebase.setAndroidContext(this);
        firebasedatos = new Firebase(FIREBASE_URL);

        agregar.setOnClickListener(this);
        actualizar.setOnClickListener(this);
        eliminar.setOnClickListener(this);
        inventario.setOnClickListener(this);
        ganancias.setOnClickListener(this);
        buscar.setOnClickListener(this);
        limpiar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLimpiar:
                id.getText().clear();
                nombre.getText().clear();
                cantidad.getText().clear();
                valor.getText().clear();
                contenido.getText().clear();
                break;

            case R.id.bAgregar:
                Toast.makeText(MainActivity.this, "Agregado al Inventario", Toast.LENGTH_SHORT).show();
                String name=nombre.getText().toString();
                String cant=cantidad.getText().toString();
                String val=valor.getText().toString();
                Firebase firebd=firebasedatos.child("datos"+contDatos);
                Datos datos=new Datos(String.valueOf(contDatos),name,cant,val);
                firebd.setValue(datos);
                id.getText().clear();
                nombre.getText().clear();
                cantidad.getText().clear();
                valor.getText().clear();
                contDatos++;
                break;

            case R.id.bInventario:
                firebasedatos.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        contenido.setText(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                break;

            case R.id.bBuscar:
                String codigo=id.getText().toString();
                final String idS = "datos"+codigo;
                firebasedatos.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(idS).exists()){
                            contenido.setText(dataSnapshot.child(idS).getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                break;

            case R.id.bActualizar:
                Toast.makeText(MainActivity.this, "Inventario Actualizado", Toast.LENGTH_SHORT).show();
                codigo=id.getText().toString();
                name=nombre.getText().toString();
                cant=cantidad.getText().toString();
                val=valor.getText().toString();
                firebd = firebasedatos.child("datos"+codigo);
                Map<String, Object>nuevoNombre=new HashMap<>();
                nuevoNombre.put("nombre",name);
                firebd.updateChildren(nuevoNombre);
                Map<String, Object>nuevoCantidad=new HashMap<>();
                nuevoCantidad.put("cantidad",cant);
                firebd.updateChildren(nuevoCantidad);
                Map<String, Object>nuevoValor=new HashMap<>();
                nuevoNombre.put("valor",val);
                firebd.updateChildren(nuevoValor);
                id.getText().clear();
                nombre.getText().clear();
                cantidad.getText().clear();
                valor.getText().clear();
                break;
            
            case R.id.bEliminar:
                Toast.makeText(MainActivity.this, "Eliminado del Inventario", Toast.LENGTH_SHORT).show();
                codigo=id.getText().toString();
                firebd=firebasedatos.child("datos"+codigo);
                firebd.removeValue();
                break;


        }

    }
}