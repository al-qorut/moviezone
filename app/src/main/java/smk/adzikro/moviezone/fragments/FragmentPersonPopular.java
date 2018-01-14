package smk.adzikro.moviezone.fragments;

import android.graphics.drawable.Drawable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.activity.DetailPeopleActivity;
import smk.adzikro.moviezone.loader.GetPersonPopular;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Actor;
import smk.adzikro.moviezone.objek.OnLoadMoreListener;

/**
 * Created by server on 11/21/17.
 */

public class FragmentPersonPopular extends Fragment
    implements OnLoadMoreListener,
        LoaderManager.LoaderCallbacks<ArrayList<Actor>> {
    private static final String KEY = "kunci";
    private static final String TAG = "people";
    private int page = 1;
    RecyclerView recyclerView;
    ArrayList<Actor> listActor = new ArrayList<>();
    ListPeople adapter;
    private ProgressBar loading;

    public static FragmentPersonPopular newInstance(int page) {
        FragmentPersonPopular popular = new FragmentPersonPopular();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY, page);
        popular.setArguments(bundle);
        return popular;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.upcoming_layout, group, false);
        if(bundle!=null){
            page = bundle.getInt(KEY);
        }else{
            page = getArguments().getInt(KEY,0);
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.list_movie);
        loading = (ProgressBar)view.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
       // recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       // listActor.add(null);
        adapter = new ListPeople();
        adapter.setOnLoadMoreListener(this);
        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY, page);
    }

    @Override
    public void onResume() {

     //   Bundle bundle = new Bundle();
     //   bundle.putInt(KEY, page);
        super.onResume();
        getActivity().getSupportLoaderManager().initLoader(1004, null, this);
    }

    @Override
    public void onLoadMore() {
        Log.e(TAG, "onLoadMore dalam fragment");
        listActor.add(null);
        adapter.notifyItemInserted(listActor.size() - 1);
          page++;
        getActivity().getSupportLoaderManager().restartLoader(1004, null, this);
    }

    @Override
    public Loader<ArrayList<Actor>> onCreateLoader(int id, Bundle args) {
        Log.e(TAG,"onCreatelOADER");
//        page = args.getInt(KEY, 1);
        return new GetPersonPopular(getContext(), page);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Actor>> loader, ArrayList<Actor> data) {
        Log.e(TAG, "ONfISISH loader Banyak actor "+data.size());


        if(loadingPertama) {
            recyclerView.setAdapter(adapter);
            loadingPertama = false;
            loading.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            listActor.remove(listActor.size() - 1);
            adapter.notifyItemRemoved(listActor.size());
        }
        listActor.addAll(data);
        adapter.notifyDataSetChanged();
        adapter.setLoaded();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Actor>> loader) {

    }


    private boolean loadingPertama = true;

    public class HolderView extends RecyclerView.ViewHolder {
        CircleImageView photo;
        TextView nama, nomor;

        public HolderView(View itemView) {
            super(itemView);
            photo = (CircleImageView) itemView.findViewById(R.id.id_photo);
            nama = (TextView) itemView.findViewById(R.id.nama_actor);
            nomor = (TextView) itemView.findViewById(R.id.peran);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Actor actor = (Actor) v.getTag();
                    Intent intent = new Intent(getActivity(), DetailPeopleActivity.class);
                    intent.putExtra(DetailPeopleActivity.KEY_SAVE, actor);
                    startActivity(intent);
                }
            });
        }
    }

    public class LoadingHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.loadmore);
        }
    }

    private class ListPeople extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private boolean isLoading ;
        OnLoadMoreListener onLoadMoreListener;
        private final int  TYPE_VIEW = 0;
        private final int TYPE_LOADING = 1;

        public OnLoadMoreListener getOnLoadMoreListener() {
            return onLoadMoreListener;
        }

        public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
            this.onLoadMoreListener = onLoadMoreListener;
        }

        public ListPeople() {
            final LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                 //   Log.e(TAG,"Recycle onScoll");
                    int totalItem = lm.getItemCount();
                    int lastTampil = lm.findLastVisibleItemPosition();
                    if (!isLoading && totalItem <= (lastTampil + 1)) {
                    //    Log.e(TAG, "isLoading False totalItem <= (lastTampil + 2)");
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                            Log.e(TAG, "onLoadMore dalam Adapter");
                        }
                        isLoading = true;
                    }
                }
            });

        }
        @Override
        public int getItemViewType(int pos) {
            return listActor.get(pos) == null ? TYPE_LOADING : TYPE_VIEW;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_VIEW) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.detail_actor, parent, false);
                return new HolderView(view);
            } else if (viewType == TYPE_LOADING){
                View view = LayoutInflater.from(getContext()).inflate(R.layout.load_more, parent, false);
                return new LoadingHolder(view);
            }
            return null;
        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            Actor actor = listActor.get(position);
            if (holder instanceof HolderView) {
                Glide.with(getContext()).load(SearchClient.getImagePath(getContext()) + actor.getPhoto())
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                ((HolderView) holder).photo.setImageDrawable(resource);
                                ((HolderView) holder).photo.setDrawingCacheEnabled(true);
                            }
                        });
                ((HolderView) holder).nama.setText(actor.getmName());
                int pos = position+1;
                ((HolderView) holder).nomor.setText("#"+pos);
                ((HolderView) holder).itemView.setTag(actor);
            } else if (holder instanceof LoadingHolder) {
                ((LoadingHolder) holder).progressBar.setIndeterminate(true);
            }
        }

        public void setLoaded() {
            isLoading = false;
        }

        @Override
        public int getItemCount() {
            return listActor != null ? listActor.size() : 0;
        }


    }

}
