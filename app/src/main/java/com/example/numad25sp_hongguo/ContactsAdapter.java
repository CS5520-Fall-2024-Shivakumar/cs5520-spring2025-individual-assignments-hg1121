package com.example.numad25sp_hongguo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
    private List<Contact> contacts = new ArrayList<>();
    private final ContactClickListener listener;

    public interface ContactClickListener {
        void onContactClick(Contact contact);
        void onEditClick(Contact contact, int position);
        void onDeleteClick(Contact contact, int position);
    }

    public ContactsAdapter(ContactClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.bind(contact, position);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void updateContacts(List<Contact> newContacts) {
        contacts = new ArrayList<>(newContacts);
        notifyDataSetChanged();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView phoneText;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.contactName);
            phoneText = itemView.findViewById(R.id.contactPhone);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        void bind(final Contact contact, final int position) {
            nameText.setText(contact.getName());
            phoneText.setText(contact.getPhoneNumber());

            itemView.setOnClickListener(v -> listener.onContactClick(contact));
            editButton.setOnClickListener(v -> listener.onEditClick(contact, position));
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(contact, position));
        }
    }
} 