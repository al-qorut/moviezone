package smk.adzikro.moviezone.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.activity.DetailPeopleActivity;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.CastMovie;

/**
 * Created by server on 11/16/17.
 */

public class ListActorAdapter extends RecyclerView.Adapter<ListActorAdapter.Holder> {
    private static final String TAG ="ListActorAdapter" ;
    private List<CastMovie> actors;
    private Context context;

    public ListActorAdapter(Context context, List<CastMovie> actors){
        this.context=context;
        this.actors = actors;
      //  Log.e(TAG,"on Create "+actors.get(0).getName());
    }
    @Override
    public ListActorAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.detail_actor,parent,false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        CastMovie actor = actors.get(position);
      //  Log.e(TAG,"onBindView "+actor.getName());

        Glide.with(context).load(SearchClient.getImagePath(context)+actor.getPhoto())
                .thumbnail(0.7f)
                .into(holder.photo);
        holder.nama.setText(actor.getName());
        holder.pemeran.setText(actor.getCharackter());
        holder.itemView.setTag(actor);
    }

    @Override
    public int getItemCount() {
        return (actors!=null?actors.size():0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        CircleImageView photo;
        TextView nama, pemeran;
        public Holder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.id_photo);
            nama = itemView.findViewById(R.id.nama_actor);
            pemeran = itemView.findViewById(R.id.peran);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CastMovie actor = (CastMovie)v.getTag();
                   // ((DetailMovieActivity)context).loadDetailActor(actor.getActor());
                    Intent intent = new Intent(context, DetailPeopleActivity.class);
                    intent.putExtra(DetailPeopleActivity.KEY_SAVE, actor.getActor());
                    context.startActivity(intent);
                }
            });
        }
    }
}
