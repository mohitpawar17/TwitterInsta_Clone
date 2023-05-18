package com.example.twitterinstafbclone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ProfileObject> mData;
    private LayoutInflater mLayoutInflator;
    private ItemClickListener mItemClickedListener;
    private ArrayList<String> mCurrentFollowers;


    MyRecyclerViewAdapter(Context context, ArrayList<ProfileObject> data, ItemClickListener itemClickListener, ArrayList<String> currentFollowers){
        this.mLayoutInflator=LayoutInflater.from(context);
        this.mData =  data;
        this.mItemClickedListener= itemClickListener;
        this.mCurrentFollowers= currentFollowers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= mLayoutInflator.inflate(R.layout.recycler_view_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProfileObject data= mData.get(position);
        String text = data.getName();
        holder.mTextView.setText(text);

        if(mCurrentFollowers != null){
            for(int i=0; i< mCurrentFollowers.size();i++ ){
                if (mCurrentFollowers.get(i).equalsIgnoreCase(data.getName())){
                    holder.mTextView.setChecked(true);
                    holder.mTextView.setCheckMarkDrawable(android.R.drawable.checkbox_on_background);
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public String getName(int position){
        return mData.get(position).getName();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CheckedTextView mTextView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            mTextView=itemView.findViewById(R.id.textViewUsers);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (mTextView.isChecked()){
                mTextView.setChecked(false);
                mTextView.setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
                if(mItemClickedListener!=null){
                    mItemClickedListener.onItemClicked(false, getAdapterPosition());
                }
            }else{
                mTextView.setChecked(true);
                mTextView.setCheckMarkDrawable(android.R.drawable.checkbox_on_background);
                if(mItemClickedListener!=null){
                    mItemClickedListener.onItemClicked(false, getAdapterPosition());
                }

            }


        }
    }

    public interface ItemClickListener{
        void onItemClicked(boolean isChecked,int position);

    }
}

