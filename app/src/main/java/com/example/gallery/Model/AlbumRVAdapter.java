package com.example.gallery.Model;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallery.R;

import java.util.ArrayList;
public class AlbumRVAdapter extends RecyclerView.Adapter<AlbumRVAdapter.ViewHolder> {

    private ArrayList<AlbumModel> albumModelArrayList;
    private Context context;

    // constructor
    public AlbumRVAdapter(ArrayList<AlbumModel> albumModelArrayList, Context context) {
        this.albumModelArrayList = albumModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        DBHandler dataBaseHelper= new DBHandler(context);
        AlbumModel modal = albumModelArrayList.get(position);
        holder.albumNameTV.setText(modal.getName());
        int count = dataBaseHelper.getAlbumImageCount(modal.getId());
        holder.albumCount.setText(Integer.toString(count));
        String noPass="";
        if (!modal.getPassword().equals(noPass))
        {
            holder.albumThumbnail.setImageResource(R.drawable.lock_album);
        }
        else
        {
            ImageModel firstImage = dataBaseHelper.getFirstAlbumImage(modal.getId());
            holder.albumThumbnail.setImageBitmap(firstImage.getImage());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noPass="";
                if (!modal.getPassword().equals(noPass))
                {
                    Intent passcode_intent = new Intent(context,AlbumPasscode.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("password",modal.getPassword());
                    bundle.putLong("albumId",modal.getId());
                    bundle.putString("albumName",modal.getName());
                    passcode_intent.putExtras(bundle);
                    context.startActivity(passcode_intent);
                }
                else
                {
                    Intent image_intent = new Intent(context,ViewAlbumImage.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("albumId",modal.getId());
                    bundle.putString("albumName",modal.getName());
                    bundle.putBoolean("hasPasscode",false);
                    image_intent.putExtras(bundle);
                    context.startActivity(image_intent);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            DBHandler dbHandler = new DBHandler(context);
            @Override
            public boolean onLongClick(View view) {
                if (position!=0&&position!=1)
                {
                    PopupMenu popup = new PopupMenu(context, view);
                    // Inflate the menu from xml
                    popup.inflate(R.menu.album_popup_filters);
                    // Setup menu item selection
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.deleteAlbum:
                                    dbHandler.deleteAlbum(albumModelArrayList.get(position).getId());
                                    albumModelArrayList.remove(position);
                                    notifyDataSetChanged();
                                    return true;
                                case R.id.renameAlbum:
                                    Dialog dialog = new Dialog(context);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.rename_album);
                                    EditText edtAlbumName = (EditText) dialog.findViewById(R.id.edtAlbumName);
                                    Button btnExitFind = (Button) dialog.findViewById(R.id.btnExitFind);
                                    Button btnSave = (Button) dialog.findViewById(R.id.btnSave);
                                    btnSave.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String albumName = edtAlbumName.getText().toString();
                                            dbHandler.updateAlbumName(albumModelArrayList.get(position).getId(),albumName);
                                            albumModelArrayList.get(position).setName(albumName);
                                            notifyItemChanged(position);
                                            dialog.dismiss();
                                        }
                                    });
                                    btnExitFind.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.show();
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView albumThumbnail;
        private TextView albumNameTV;
        private TextView albumCount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumNameTV = itemView.findViewById(R.id.idTVAlbumName);
            albumThumbnail = itemView.findViewById(R.id.idIVAlbumThumbnail);
            albumCount = itemView.findViewById(R.id.idTVAlbumCount);
        }
    }
}
