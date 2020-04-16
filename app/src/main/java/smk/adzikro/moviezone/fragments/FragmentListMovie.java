package smk.adzikro.moviezone.fragments;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.activity.MainActivity;
import smk.adzikro.moviezone.custom.GridAutofitLayoutManager;
import smk.adzikro.moviezone.loader.GetMovieTaskLoader;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Movie;
import smk.adzikro.moviezone.objek.OnItemClickListener;
import smk.adzikro.moviezone.objek.OnLoadMoreListener;

/**
 * Created by server on 11/15/17.
 */

public class FragmentListMovie extends Fragment implements
        LoaderManager.LoaderCallbacks<ArrayList<Movie>>,
        OnLoadMoreListener,
        OnItemClickListener {

    public static final String TAG = "FragmentListMovie" ;
    int page = 1, aksi, tampil;
    String query;
    private MovieListAdapter adapter;
    private RecyclerView listMovie;
    private ProgressBar loading;
    private ArrayList<Movie> mData =new ArrayList<>();
    private boolean loadingPertama=true;

    public static FragmentListMovie newInstance(int aksi, String query){
        FragmentListMovie fragmentListMovie = new FragmentListMovie();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG,aksi);
        fragmentListMovie.setArguments(bundle);
        return fragmentListMovie;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.upcoming_layout, container, false);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            aksi = savedInstanceState.getInt(TAG);
        } else {
            aksi = getArguments().getInt(TAG);
        }
        listMovie = view.findViewById(R.id.list_movie);
        tampil =SearchClient.getTampil(getContext());
        if(tampil==0) {
            listMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        }else{
            listMovie.setLayoutManager(new GridAutofitLayoutManager(getContext(), 0));
        }
        listMovie.setHasFixedSize(true);
        loading = view.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        listMovie.setVisibility(View.GONE);

        adapter = new MovieListAdapter(SearchClient.getTampil(getContext()));
        adapter.setOnLoadMoreListener(this);
        adapter.setOnItemClickListener(this);
        return view;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        listMovie.setLayoutManager(null);
        if(tampil==0) {
            listMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        }else{
            listMovie.setLayoutManager(new GridAutofitLayoutManager(getContext(), 300));
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TAG, aksi);
    }

    @Override
    public void onResume(){
        Log.e(TAG,"onResume dipanggil");
        super.onResume();
        getActivity().getSupportLoaderManager().initLoader(aksi,null,this);
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        return new GetMovieTaskLoader(getContext(),query, aksi, page,"0");
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        if(data.size()==0)return;
        if (!loadingPertama) {
            mData.remove(mData.size() - 1);
            adapter.notifyItemRemoved(mData.size());
        } else {
            loadingPertama = false;
            listMovie.setAdapter(adapter);
            loading.setVisibility(View.GONE);
            listMovie.setVisibility(View.VISIBLE);
        }
        //for (int i = 0; i < data.size(); i++) {
        //    mData.add(data.get(i));
       // }
        mData.addAll(data);
        adapter.notifyDataSetChanged();
        adapter.setLoaded();
        listMovie.setVisibility(View.VISIBLE);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }

    @Override
    public void onLoadMore() {
        mData.add(null);
        adapter.notifyItemInserted(mData.size()-1);
        page++;
        getActivity().getSupportLoaderManager().restartLoader(aksi,null,this);
    }

    @Override
    public void onItemClick(View view, Movie movie) {
        Toast.makeText(getContext(), movie.getTitle(),Toast.LENGTH_SHORT).show();
        ((MainActivity)getActivity()).showDetail(movie, view.findViewById(R.id.image));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.view_list_grid, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (tampil != 0) {
            tampil = 0;
            item.setIcon(R.drawable.view_grid);
            listMovie.setLayoutManager(new LinearLayoutManager(getContext()));
            SearchClient.setTampil(getContext(), MovieListAdapter.VIEW_TYPE_ITEM);
            adapter.setType(MovieListAdapter.VIEW_TYPE_ITEM);
        } else {
            tampil = 2;
            listMovie.setLayoutManager(new GridAutofitLayoutManager(getContext(), 0));
            SearchClient.setTampil(getContext(), MovieListAdapter.VIEW_TYPE_GRID);
            item.setIcon(R.drawable.view_list);
            adapter.setType(MovieListAdapter.VIEW_TYPE_GRID);
        }
        adapter.notifyDataSetChanged();
        return true;
    }

    public class ListDataHolder extends RecyclerView.ViewHolder
    {
        ImageView imgPhoto;
        TextView txTitle, txReleaseDate, txGenre, txRating;
        public ListDataHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.image);
            txTitle = itemView.findViewById(R.id.tx_title);
            txReleaseDate = itemView.findViewById(R.id.tx_release_date);
            txGenre = itemView.findViewById(R.id.tx_genre);
            txRating = itemView.findViewById(R.id.tx_rating);
        }


    }

    public class ListLoadingHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public ListLoadingHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.loadmore);
        }
    }

    public class GridDataHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public GridDataHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
            textView = view.findViewById(R.id.title);
        }
    }

    class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
            implements View.OnClickListener{
        public static final int VIEW_TYPE_ITEM = 0;
        public static final int VIEW_TYPE_GRID = 2;
        private final int VIEW_TYPE_LOADING = 1;
        private OnLoadMoreListener onLoadMoreListener;
        private OnItemClickListener onItemClickListener;
        private boolean isLoading;
        private int visibleThreshold =1;
        private int lastTampilItem, totalItem;
        private int type;

        public MovieListAdapter(int type){
            this.type=type;
            if(type==VIEW_TYPE_ITEM){
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)
                        listMovie.getLayoutManager();
                totalItem = linearLayoutManager.getItemCount();
                lastTampilItem = linearLayoutManager.findLastVisibleItemPosition();
            }else{
                final GridAutofitLayoutManager gridLayoutManager = (GridAutofitLayoutManager) listMovie.getLayoutManager();
                totalItem = gridLayoutManager.getItemCount();
                lastTampilItem = gridLayoutManager.findLastVisibleItemPosition();
            }

            listMovie.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if(!loadingPertama&&!isLoading && totalItem <= (lastTampilItem+visibleThreshold)){
                        if(onLoadMoreListener !=null){
                            onLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            });
        }
        public void setType(int type){
            this.type = type;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==VIEW_TYPE_ITEM){
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_movie,parent,false);
                view.setOnClickListener(this);
                return new ListDataHolder(view);
            }else if(viewType==VIEW_TYPE_GRID){
                View view = LayoutInflater.from(getContext()).inflate(R.layout.grid_layout,parent,false);
                view.setOnClickListener(this);
                return new GridDataHolder(view);
            }else if(viewType==VIEW_TYPE_LOADING){
                View view = LayoutInflater.from(getContext()).inflate(R.layout.load_more,parent,false);
                return new ListLoadingHolder(view);
            }
            return null;
        }

        public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
            this.onLoadMoreListener = onLoadMoreListener;
        }
        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
        @Override
        public int getItemViewType(int pos){
            int VIEW_NAON=type==VIEW_TYPE_ITEM?VIEW_TYPE_ITEM:VIEW_TYPE_GRID;
            return mData.get(pos)==null?VIEW_TYPE_LOADING:VIEW_NAON;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            final Movie p = mData.get(position);

            if(holder instanceof ListDataHolder) {
             //   Log.e(TAG,"ListDataHolder "+SearchClient.getImagePath(getContext())+p.getCover());
                Glide.with(getContext()).load(SearchClient.getImagePath(getContext())+p.getCover())
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                ((ListDataHolder)holder).imgPhoto.setImageDrawable(resource);
                                ((ListDataHolder)holder).imgPhoto.setDrawingCacheEnabled(true);
                             //   Log.e(TAG,"onResourceReady");
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
                ((ListDataHolder)holder).itemView.setTag(p);
                ((ListDataHolder)holder).txTitle.setText(p.getTitle());
                ((ListDataHolder)holder).txReleaseDate.setText(p.getReleasedate());
                ((ListDataHolder)holder).txGenre.setText(p.getGenres());
                ((ListDataHolder)holder).txRating.setText(p.getRating());
            }else if(holder instanceof GridDataHolder){
              //  Log.e(TAG,"GridDataHolder");
                ((GridDataHolder)holder).itemView.setTag(p);
                ((GridDataHolder)holder).textView.setText(p.getTitle());
                Glide.with(getContext()).load(SearchClient.getImagePath(getContext())+p.getCover())
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                ((GridDataHolder)holder).imageView.setImageDrawable(resource);
                                ((GridDataHolder)holder).imageView.setDrawingCacheEnabled(true);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }else if(holder instanceof ListLoadingHolder){
               // Log.e(TAG,"ListLoadingHolder");
                ((ListLoadingHolder)holder).progressBar.setIndeterminate(true);
            }
        }

        @Override
        public int getItemCount() {
            return mData==null?0:mData.size();
        }
        public void setLoaded(){
            isLoading = false;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, (Movie) v.getTag());
        }

    }

}
