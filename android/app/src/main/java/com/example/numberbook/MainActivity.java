package com.example.numberbook;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button btnLoadContacts, btnSyncContacts, btnSearch;
    private EditText etKeyword;
    private RecyclerView recyclerViewContacts;
    private ContactAdapter adapter;
    private List<Contact> contactList = new ArrayList<>();
    private ContactApi contactApi;

    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) loadContacts();
                else Toast.makeText(this, "Permission refusée", Toast.LENGTH_SHORT).show();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init vues
        btnLoadContacts = findViewById(R.id.btnLoadContacts);
        btnSyncContacts = findViewById(R.id.btnSyncContacts);
        btnSearch = findViewById(R.id.btnSearch);
        etKeyword = findViewById(R.id.etKeyword);
        recyclerViewContacts = findViewById(R.id.recyclerViewContacts);

        // Init RecyclerView
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactAdapter(contactList);
        recyclerViewContacts.setAdapter(adapter);

        // Init Retrofit
        contactApi = RetrofitClient.getClient().create(ContactApi.class);

        // Boutons
        btnLoadContacts.setOnClickListener(v -> checkAndLoadContacts());
        btnSyncContacts.setOnClickListener(v -> syncContacts());
        btnSearch.setOnClickListener(v -> searchContacts());
    }

    // ─── Charger contacts du téléphone ───────────────────────────────────────

    private void checkAndLoadContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            loadContacts();
        } else {
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS);
        }
    }

    private void loadContacts() {
        contactList.clear();
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                },
                null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                String phone = cursor.getString(1);
                contactList.add(new Contact(name, phone));
            }
            cursor.close();
        }

        adapter.updateData(contactList);
        Toast.makeText(this, contactList.size() + " contacts chargés", Toast.LENGTH_SHORT).show();
    }

    // ─── Synchroniser vers le serveur ────────────────────────────────────────

    private void syncContacts() {
        if (contactList.isEmpty()) {
            Toast.makeText(this, "Chargez d'abord les contacts", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Contact contact : contactList) {
            contactApi.insertContact(contact).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call,
                                       @NonNull Response<ApiResponse> response) {
                    // succès silencieux par contact
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this,
                            "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        Toast.makeText(this, "Synchronisation lancée", Toast.LENGTH_SHORT).show();
    }

    // ─── Rechercher dans le serveur ───────────────────────────────────────────

    private void searchContacts() {
        String keyword = etKeyword.getText().toString().trim();
        if (keyword.isEmpty()) {
            Toast.makeText(this, "Entrez un mot-clé", Toast.LENGTH_SHORT).show();
            return;
        }

        contactApi.searchContacts(keyword).enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(@NonNull Call<List<Contact>> call,
                                   @NonNull Response<List<Contact>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateData(response.body());
                    Toast.makeText(MainActivity.this,
                            response.body().size() + " résultat(s)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Contact>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this,
                        "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}