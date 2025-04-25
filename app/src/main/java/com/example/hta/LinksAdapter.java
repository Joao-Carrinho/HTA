package com.example.hta;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
            if (resourceId == 0) {
                Toast.makeText(context, "PDF não encontrado (ID = 0)", Toast.LENGTH_SHORT).show();
                return;
            }

            InputStream inputStream = context.getResources().openRawResource(resourceId);
            File file = new File(context.getCacheDir(), "temp.pdf");
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            Uri uri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".provider",
                    file
            );

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Nenhuma app encontrada para abrir PDF. Por favor, instale um leitor de PDFs.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(context, "Erro ao abrir PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}