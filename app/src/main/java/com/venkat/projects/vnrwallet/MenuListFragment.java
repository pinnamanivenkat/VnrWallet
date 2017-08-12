package com.venkat.projects.vnrwallet;

/**
 * Created by venkat on 30/7/17.
 */

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by mxn on 2016/12/13.
 * MenuListFragment
 */

public class MenuListFragment extends Fragment {
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container,
                false);
        NavigationView vNavigation = (NavigationView) view.findViewById(R.id.vNavigation);
        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_id_card:
                        startActivity(new Intent(getContext(),IdCard.class));
                        break;
                    case R.id.menu_lirc_card:
                        break;
                    case R.id.lirc_qr_code:
                        startActivity(new Intent(getContext(),qrcode.class));
                        break;
                    case R.id.menu_exit:
                        android.os.Process.killProcess(android.os.Process.myPid());
                        break;
                    case R.id.menu_logout:
                        mAuth.signOut();
                        removeFiles();
                        startActivity(new Intent(getContext(),LoginActivity.class));
                        getActivity().finish();
                        break;
                }
                return false;
            }
        }) ;
        return  view ;
    }

    private void removeFiles() {
        Context c = getContext();
        String path = c.getFilesDir().getPath();
        if((new File(path+"/id_front.jpg")).delete()) {}
        if((new File(path+"/id_back.jpg")).delete()){}

    }
}