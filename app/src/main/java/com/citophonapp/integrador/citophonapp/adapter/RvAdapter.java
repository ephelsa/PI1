package com.citophonapp.integrador.citophonapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.citophonapp.integrador.citophonapp.R;
import com.citophonapp.integrador.citophonapp.entity.User;
import com.citophonapp.integrador.citophonapp.fragment.InformationFragment;

import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {
    List<User> users;
    private Context context;
    private InformationFragment informationFragment;

    public RvAdapter(List<User> users) {
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_information, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final AppCompatActivity activity = (AppCompatActivity) context;


        holder.owner.setText(users.get(position).getName());
        holder.localNumber.setText(users.get(position).getLocalNumber());
        holder.showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("callUser", users.get(position));
                informationFragment = new InformationFragment();
                informationFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, informationFragment).addToBackStack(null).commit();


            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView localNumber;
        private TextView owner;
        private ImageButton showMore;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardViewInfo);
            localNumber = (TextView) itemView.findViewById(R.id.localNumber);
            owner = (TextView) itemView.findViewById(R.id.owner);
            showMore = (ImageButton) itemView.findViewById(R.id.showMore);
        }
    }
}
