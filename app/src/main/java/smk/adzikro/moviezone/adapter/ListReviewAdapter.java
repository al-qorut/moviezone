package smk.adzikro.moviezone.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.objek.Review;

/**
 * Created by server on 11/17/17.
 */

public class ListReviewAdapter extends RecyclerView.Adapter<ListReviewAdapter.Holder>{
    private static final String TAG ="ListActorAdapter" ;
    private List<Review> actors;
    private Context context;

    public ListReviewAdapter(Context context, List<Review> actors){
        this.context=context;
        this.actors = actors;
        //  Log.e(TAG,"on Create "+actors.get(0).getName());
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.detail_review,parent,false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Review review = actors.get(position);
        holder.title.setText("Review By. "+review.getAuthor());
        holder.author.setText("By "+review.getAuthor());
        holder.url.setText(review.getUrl());
        holder.content.setText(review.getContent());
        holder.itemView.setTag(review);
    }

    @Override
    public int getItemCount() {
        return (actors!=null?actors.size():0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView title, author, url, content;
        public Holder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            url = itemView.findViewById(R.id.url);
            content = itemView.findViewById(R.id.content);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = ((Review)v.getTag()).getUrl();
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent();
                    intent.setData(uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    //Toast.makeText(context,nama,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
