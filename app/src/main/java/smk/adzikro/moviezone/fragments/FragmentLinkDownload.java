package smk.adzikro.moviezone.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import smk.adzikro.moviezone.R;

/**
 * Created by server on 11/27/17.
 * create link download with google search
 */

public class FragmentLinkDownload extends Fragment {
    private static final String KEY ="linkdonlot" ;
    private String BASE_LINK = "https://www.google.com/search?q=";
    public static FragmentLinkDownload newInstance(String query){
        FragmentLinkDownload f = new FragmentLinkDownload();
        Bundle bundle = new Bundle();
        bundle.putString(KEY,query);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_link_download,parent,false);
        WebView webView = view.findViewById(R.id.webview);
        String query = getArguments().getString(KEY);
        webView.getSettings().setJavaScriptEnabled(true);
        query ="intitle:\"index of Movie\" -htm -html -tag \""+query+"\"";
        try {
            webView.loadUrl(BASE_LINK+URLEncoder.encode(query, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return view;
    }
}
