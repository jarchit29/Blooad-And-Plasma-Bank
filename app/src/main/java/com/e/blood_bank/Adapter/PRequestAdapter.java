package com.e.blood_bank.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.RecyclerView;

import com.e.blood_bank.DataModela.PRequestDataModel;
import com.e.blood_bank.DataModela.RequestDataModel;
import com.e.blood_bank.PlasmaActivity;
import com.e.blood_bank.R;

import java.util.List;

import static android.Manifest.permission.CALL_PHONE;

public class PRequestAdapter extends RecyclerView.Adapter<PRequestAdapter.ViewHolder> {

    private List<PRequestDataModel> dataSet;
    private Context context;

    public PRequestAdapter(
            List<PRequestDataModel> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    public PRequestAdapter(List<RequestDataModel> requestDataModels, PlasmaActivity context) {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plasma_layout, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,
                                 final int position) {

        holder.message.setText(dataSet.get(position).getMessage());

        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (PermissionChecker.checkSelfPermission(context,CALL_PHONE)
                        ==PermissionChecker.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + dataSet.get(position).getNumber()));
                    context.startActivity(intent);
                }
                else
                {
                    ((Activity)context).requestPermissions(new String[]{CALL_PHONE},401);
                }

            }
        });

        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        holder.message.getText().toString() + "\n\nContact:" +dataSet.get(position)
                                .getNumber());
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Hey could you help here");
                context.startActivity(Intent.createChooser(shareIntent,"share...."));



            }


        });



    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView message;
        ImageView callButton, shareButton;

        ViewHolder(final View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.pmessage);
            callButton = itemView.findViewById(R.id.pcall);
            shareButton = itemView.findViewById(R.id.pshare);
        }

    }

}