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

import com.e.blood_bank.DataModela.Donor;
import com.e.blood_bank.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

//import com.bumptech.glide.Glide;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<Donor> dataSet;
    private Context context;

    public SearchAdapter(
            List<Donor> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

@NonNull
@Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donor_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, // This we get from View Holder
                                 final int position) {
        String str = "Name "+dataSet.get(position).getName();
        str+= "\n city: " +dataSet.get(position).getCity();

        holder.message.setText(str);

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
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView message;
        CircleImageView imageView;
        ImageView callbutton;

        ViewHolder(final View itemView) {

            super(itemView);

            message = itemView.findViewById(R.id.message);
            imageView = itemView.findViewById(R.id.image);
            callbutton = itemView.findViewById(R.id.call);



        }

    }

}

