package com.example.numad25sp_hongguo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;

public class ContactsActivity extends AppCompatActivity implements ContactsAdapter.ContactClickListener {
    private static final int CALL_PERMISSION_REQUEST_CODE = 123;
    private static final String KEY_CONTACTS = "contacts";
    private List<Contact> contacts = new ArrayList<>();
    private ContactsAdapter adapter;
    private Contact selectedContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        RecyclerView recyclerView = findViewById(R.id.contactsRecyclerView);
        adapter = new ContactsAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Restore contacts if there's saved state
        if (savedInstanceState != null) {
            ArrayList<Contact> savedContacts = savedInstanceState.getParcelableArrayList(KEY_CONTACTS);
            if (savedContacts != null) {
                contacts = new ArrayList<>(savedContacts);
                adapter.updateContacts(contacts);
            }
        }

        FloatingActionButton fab = findViewById(R.id.addContactFab);
        fab.setOnClickListener(v -> showContactDialog(null, -1));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_CONTACTS, new ArrayList<>(contacts));
    }

    private void showContactDialog(Contact contact, int position) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_contact, null);
        TextInputEditText nameInput = dialogView.findViewById(R.id.nameInput);
        TextInputEditText phoneInput = dialogView.findViewById(R.id.phoneInput);

        if (contact != null) {
            nameInput.setText(contact.getName());
            phoneInput.setText(contact.getPhoneNumber());
        }

        new AlertDialog.Builder(this)
                .setTitle(contact == null ? R.string.add_contact : R.string.edit_contact)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String name = nameInput.getText().toString();
                    String phone = phoneInput.getText().toString();

                    if (name.isEmpty() || phone.isEmpty()) {
                        showSnackbar(getString(R.string.empty_fields_error), false, null, null);
                        return;
                    }

                    if (contact == null) {
                        contacts.add(new Contact(name, phone));
                        showSnackbar(getString(R.string.contact_added), false, null, null);
                    } else {
                        contact.setName(name);
                        contact.setPhoneNumber(phone);
                        showSnackbar(getString(R.string.contact_updated), false, null, null);
                    }
                    adapter.updateContacts(contacts);
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @Override
    public void onContactClick(Contact contact) {
        selectedContact = contact;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PERMISSION_REQUEST_CODE);
        } else {
            makePhoneCall(contact);
        }
    }

    private void makePhoneCall(Contact contact) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
        startActivity(intent);
    }

    @Override
    public void onEditClick(Contact contact, int position) {
        showContactDialog(contact, position);
    }

    @Override
    public void onDeleteClick(Contact contact, int position) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_contact)
                .setMessage(R.string.delete_confirmation)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    Contact deletedContact = contacts.remove(position); // Store deleted contact
                    adapter.updateContacts(contacts);
                    showSnackbar(getString(R.string.contact_deleted), true, deletedContact, position);
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void showSnackbar(String message, boolean showUndo, Contact deletedContact, Integer position) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_LONG);

        // Only show the undo button if needed
        if (showUndo && deletedContact != null && position != null) {
            snackbar.setAction(R.string.undo, v -> {
                contacts.add(position, deletedContact); // Restore contact
                adapter.updateContacts(contacts);
            });
        }

        snackbar.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                         int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (selectedContact != null) {
                    makePhoneCall(selectedContact);
                }
            } else {
                showSnackbar(getString(R.string.permission_denied), false, null, null);
            }
        }
    }
} 