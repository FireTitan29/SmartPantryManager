package com.example.smartpantrymanager.adapters;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.models.PantryItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<PantryItem> pantryItems;
    private OnPantryItemClickListener listener;

    public PantryAdapter(List<PantryItem> pantryItems, OnPantryItemClickListener listener) {
        this.pantryItems = pantryItems;
        this.listener = listener;
    }

    // Gets the XML and turns it into a view
    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);

        return new PantryViewHolder(view);
    }

    // Binds the attributes to the holder
    @Override
    public void onBindViewHolder(PantryViewHolder holder, int position) {

        PantryItem item = pantryItems.get(position);

        holder.textViewName.setText(item.getName());

        holder.textViewQuantity.setText(String.format("%s%s", String.valueOf(item.getQuantity()), item.getMeasurementName()));

        if (!item.getExpiryDate().isEmpty()) {

            holder.textViewExpiry.setText(String.format("Expires %s", item.getExpiryDate()));
        } else {

            holder.textViewExpiry.setText("No Expiry Set");
        }

        if (item.isExpiringSoon()) {

            holder.imageExpiryNotification.setVisibility(View.VISIBLE);

        } else {

            holder.imageExpiryNotification.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {listener.onPantryItemClick(item);});
    }

    @Override
    public int getItemCount() {

        return pantryItems.size();
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewQuantity;
        TextView textViewExpiry;
        ImageView imageViewEdit;

        ImageView  imageExpiryNotification;

        public PantryViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewPantryItemName);
            textViewQuantity = itemView.findViewById(R.id.textViewPantryItemQuantity);
            textViewExpiry = itemView.findViewById(R.id.textViewPantryItemExpiry);
            imageViewEdit = itemView.findViewById(R.id.imageEditPantryItem);

            imageExpiryNotification = itemView.findViewById(R.id.imageExpiryNotification);
        }
    }

    public interface OnPantryItemClickListener {
        void onPantryItemClick(PantryItem item);
    }
}