package com.example.omar.cnghiring.dashboardpack;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.omar.cnghiring.R;

import java.util.ArrayList;

public class dashboard_item_view_adapter extends RecyclerView.Adapter<dashboard_item_view_adapter.Search_book_holder> {
    Context context;
    private ArrayList<reqDetails> fairRequestList;

    class Search_book_holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView rqfair, rqphone, rqdistance, rqinserttime;
        private ItemClickListener itemClickListener;


        public Search_book_holder(@NonNull View itemView) {
            super(itemView);

            rqfair = itemView.findViewById(R.id.fair);
            rqphone = itemView.findViewById(R.id.phoneNumber);
            rqdistance = itemView.findViewById(R.id.distance);
            rqinserttime = itemView.findViewById(R.id.requestTime);


            itemView.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onCLick(v, getAdapterPosition(), false);

        }
    }

    public dashboard_item_view_adapter(Context context, ArrayList<reqDetails> fairRequestList) {

        this.context = context;
        this.fairRequestList = fairRequestList;

    }

    @NonNull
    @Override
    public Search_book_holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.activity_dashboard_item_view, viewGroup, false);
        return new Search_book_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Search_book_holder search_book_holder, int i) {

        search_book_holder.rqfair.setText(fairRequestList.get(i).getFair()+"");
        search_book_holder.rqphone.setText("+"+fairRequestList.get(i).getPhoneNumber());
        search_book_holder.rqdistance.setText(fairRequestList.get(i).getDistance()+"");
        search_book_holder.rqinserttime.setText(fairRequestList.get(i).getInsertTime());

        search_book_holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onCLick(View view, int position, boolean isClicked) {

                //viewBookDetails(fairRequestList.get(position));

                Intent intent= new Intent(context, RequestDetailsClick_activity.class);
                intent.putExtra("request", fairRequestList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fairRequestList.size();
    }


}
