package com.example.hta;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

public class LinksAdapter extends RecyclerView.Adapter<LinksAdapter.LinkViewHolder> {

    private List<LinkItem> linkItemList;
    private Context context;

    public LinksAdapter(Context context, List<LinkItem> linkItemList) {
        this.context = context;
        this.linkItemList = linkItemList;
    }

    @NonNull
    @Override
    public LinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_link, parent, false);
        return new LinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LinkViewHolder holder, int position) {
        LinkItem linkItem = linkItemList.get(position);
        holder.textViewLink.setText(linkItem.getTitle());

        holder.textViewLink.setOnClickListener(v -> {
            // Abrir PDF local
            openPdfFromRaw(linkItem.getResourceId());
        });
    }

    @Override
    public int getItemCount() {
        return linkItemList.size();
    }

    static class LinkViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLink;

        public LinkViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLink = itemView.findViewById(R.id.textViewLink);
        }
    }

    private void openPdfFromRaw(int resourceId) {
        try {
            // Guardar o PDF em armazenamento externo
            File file = new File(context.getExternalFilesDir(null), "temp.pdf");
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            // Abrir o PDF com um Intent
            Uri pdfUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
