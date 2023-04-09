package com.example.mediamarkt.Navigation;

import static com.example.mediamarkt.R.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.mediamarkt.CatFire.CartActivity.CartActivity;
import com.example.mediamarkt.CatFire.Model.Model;
import com.example.mediamarkt.CatFire.adapter.MyToyAdapter;
import com.example.mediamarkt.CatFire.adapter.ViewPagerArticleAdapter;
import com.example.mediamarkt.CatFire.listener.LoadListener;
import com.example.mediamarkt.CatFire.utils.SpaceItemDecoration;
import com.example.mediamarkt.R;
import com.example.mediamarkt.Search.AllSearch;
import com.example.mediamarkt.ViewPager.MostArticle1Fragment;
import com.example.mediamarkt.ViewPager.MostArticle2Fragment;
import com.example.mediamarkt.ViewPager.MostArticle3Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class Home1 extends Fragment {

    TextView maintexthome, obratchet;
    CardView intsearch;
    ViewPagerArticleAdapter viewPagerArticleAdapter;
    ViewPager viewpager;




    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(layout.fragment_home1, container, false);

        maintexthome = view.findViewById(id.maintexthome);
        intsearch = view.findViewById(id.intsearch);
        obratchet = view.findViewById(R.id.obratchet);
        checkname();


        viewPagerArticleAdapter = new ViewPagerArticleAdapter(((FragmentActivity)getContext()).getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerArticleAdapter.addFragment(new MostArticle1Fragment());
        viewPagerArticleAdapter.addFragment(new MostArticle2Fragment());
        viewPagerArticleAdapter.addFragment(new MostArticle3Fragment());

        viewpager = (ViewPager) view.findViewById(id.viewpagerArticle);
        viewpager.setAdapter(viewPagerArticleAdapter);

        CircleIndicator indicator = (CircleIndicator) view.findViewById(id.indicator);
        indicator.setViewPager(viewpager);

        new CountDownTimer(50000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                Calendar calendar = new GregorianCalendar();
                Date trialTime = new Date();
                calendar.setTime(trialTime);
                Integer secu = calendar.get(Calendar.SECOND);
                Integer minu = calendar.get(Calendar.MINUTE);
                Integer hou = calendar.get(Calendar.HOUR_OF_DAY);

                NumberFormat f = new DecimalFormat("00");
                long sec = 60 - secu;
                long min = 60 - minu;
                long hour = 23 - hou;

                obratchet.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                obratchet.setText("00:00:00");
            }
        }.start();

        intsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllSearch.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void checkname() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usuariosRef = rootRef.child("Пользователи");
        usuariosRef.child(uid).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                maintexthome.setText("Привет, " + String.valueOf(task.getResult().getValue()) + "!");
            }
        });
    }

}