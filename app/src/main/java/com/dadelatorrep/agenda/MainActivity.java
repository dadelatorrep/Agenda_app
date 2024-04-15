package com.dadelatorrep.agenda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    EditText txtNombre, txtTelefono, txtEmail, txtNumeroCargar;
    ArrayList<Contacto> listaContactos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtNombre = findViewById(R.id.txtNombre);
        txtTelefono =findViewById(R.id.txtNumero);
        txtEmail =findViewById(R.id.txtEmail);

        // Inicializar la lista de contactos
        listaContactos = new ArrayList<>();
        txtNumeroCargar =findViewById(R.id.txtNumeroCargar);
    }

    public void cmdGuardar_onClick(View v)
    {
        String nombre = txtNombre.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String email = txtEmail.getText().toString();

        // Crear un nuevo contacto
        Contacto nuevoContacto = new Contacto(nombre, telefono, email);

        // Agregar el nuevo contacto a la lista de contactos
        listaContactos.add(nuevoContacto);

        // Guardar la lista de contactos en SharedPreferences
        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> contactosSet = new HashSet<>();
        for (Contacto contacto : listaContactos) {
            // Convertir cada contacto a una cadena y agregarlo al conjunto
            contactosSet.add(contacto.toString());
        }
        editor.putStringSet("contactos", contactosSet);
        editor.apply();

        Toast.makeText(this, "Contacto Guardado Correctamente!!", Toast.LENGTH_SHORT).show();
    }



    public void cmdLeer_onClick(View v) {
        // Obtener el ID del contacto a buscar

        String idBuscar = txtNumeroCargar.getText().toString();

        // Obtener la lista de contactos desde SharedPreferences
        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        Set<String> contactosSet = preferences.getStringSet("contactos", new HashSet<>());

        // Recorrer la lista de contactos para buscar el contacto con el ID especificado
        boolean encontrado = false;
        for (String contactoString : contactosSet) {
            String[] partesContacto = contactoString.split(",");
            String idContacto = partesContacto[1]; // Suponiendo que el ID está en la posición del teléfono

            // Si el ID coincide, mostrar los detalles del contacto
            if (idContacto.equals(idBuscar)) {
                String nombre = partesContacto[0];
                String telefono = partesContacto[1];
                String email = partesContacto[2];

                // Mostrar los detalles del contacto en los campos del formulario
                txtNombre.setText(nombre);
                txtTelefono.setText(telefono);
                txtEmail.setText(email);

                encontrado = true;
                break; // Salir del bucle una vez que se ha encontrado el contacto
            }
        }

        // Mostrar un mensaje si no se encontró ningún contacto con el ID especificado
        if (!encontrado) {
            Toast.makeText(this, "No se encontró ningún contacto con el ID especificado", Toast.LENGTH_LONG).show();
        }
    }


}

    // Clase Contacto para representar un contacto
    class Contacto {
        private String nombre;
        private String telefono;
        private String email;

        public Contacto(String nombre, String telefono, String email) {
            this.nombre = nombre;
            this.telefono = telefono;
            this.email = email;
        }

        public String getNombre() {
            return nombre;
        }

        public String getTelefono() {
            return telefono;
        }

        public String getEmail() {
            return email;
        }

        @Override
        public String toString() {
            return nombre + "," + telefono + "," + email;
        }
    }