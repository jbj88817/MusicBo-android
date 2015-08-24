package com.bojie.musicbo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by bojiejiang on 8/23/15.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolderMusic> {

    private ArrayList<Music> mMusics = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;


    public SearchAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void setMusics(ArrayList<Music> listMusic) {
        mMusics = listMusic;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderMusic onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_search, viewGroup, false);
        ViewHolderMusic viewHolder = new ViewHolderMusic(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderMusic viewHolderMusic, int position) {
        Music currentMusic = mMusics.get(position);
        if (currentMusic != null) {
            viewHolderMusic.musicSearchResult.setText(currentMusic.getName());
            Picasso.with(mContext)
                    .load(currentMusic.getUrlThumbnail())
                    .placeholder(R.drawable.no_image_available)
                    .into(viewHolderMusic.musicThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return mMusics.size();
    }

    static class ViewHolderMusic extends RecyclerView.ViewHolder {
        ImageView musicThumbnail;
        TextView musicSearchResult;

        public ViewHolderMusic(View itemView) {
            super(itemView);
            musicThumbnail = (ImageView) itemView.findViewById(R.id.iv_thumbnail);
            musicSearchResult = (TextView) itemView.findViewById(R.id.tv_search_result);
        }
    }

}
