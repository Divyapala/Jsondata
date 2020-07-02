package com.example.app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RetrofitAdapter extends RecyclerView.Adapter<RetrofitAdapter.MyViewHolder> implements Filterable {

    private ArrayList<Network> userList;
    private ArrayList<Network> filteredUserList;
    private Context context;
    private CustomItemClickListener customItemClickListener;

    public RetrofitAdapter(Context context,ArrayList<Network> userArrayList,CustomItemClickListener customItemClickListener) {
        this.context = context;
        this.userList = userArrayList;
        this.filteredUserList = userArrayList;
        this.customItemClickListener = customItemClickListener;
    }

    @Override
    public RetrofitAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_list, viewGroup, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customItemClickListener.onItemClick(filteredUserList.get(myViewHolder.getAdapterPosition()),myViewHolder.getAdapterPosition());
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RetrofitAdapter.MyViewHolder viewHolder, int position) {

        viewHolder.title.setText(filteredUserList.get(position).getTitle());
        Picasso.get().load(filteredUserList.get(position).getUrl()).fit().centerCrop().into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return filteredUserList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String searchString = charSequence.toString();

                if (searchString.isEmpty()) {

                    filteredUserList = userList;

                } else {

                    ArrayList<Network> tempFilteredList = new ArrayList<>();

                    for (Network user : userList) {

                        if (user.getTitle().toLowerCase().contains(searchString)||user.getUrl().toLowerCase().contains(searchString)) {

                            tempFilteredList.add(user);
                        }
                    }

                    filteredUserList = tempFilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredUserList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredUserList = (ArrayList<Network>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView image;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView)view.findViewById(R.id.title);
            image = (ImageView) view.findViewById(R.id.userThumbnail);

        }
    }
}