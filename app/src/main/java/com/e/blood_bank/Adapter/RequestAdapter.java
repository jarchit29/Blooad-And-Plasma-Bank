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

import com.e.blood_bank.DataModela.RequestDataModel;
import com.e.blood_bank.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CALL_PHONE;

//import com.bumptech.glide.Glide;


public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<RequestDataModel> dataSet;
    private Context context;

    public RequestAdapter(
            List<RequestDataModel> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

@NonNull
@Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item_lay, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, // This we get from View Holder
                                 final int position) {

        holder.message.setText(dataSet.get(position).getMessage());

       // Glide.with(context).load(dataSet.get(position).getImageurl()).into(holder.imageView);

        holder.callbutton.setOnClickListener(new View.OnClickListener() {
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

        holder.sharebutton.setOnClickListener(new View.OnClickListener() {
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
        CircleImageView imageView;
        ImageView callbutton, sharebutton;

        ViewHolder(final View itemView) {

            super(itemView);

            message = itemView.findViewById(R.id.message);
            imageView = itemView.findViewById(R.id.image);
            callbutton = itemView.findViewById(R.id.call);
            sharebutton = itemView.findViewById(R.id.share);


        }

    }

}

