package com.example.mediamarkt.Navigation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.mediamarkt.CatFire.SoftCatFire;
import com.example.mediamarkt.CatFire.ActiveCatFire;
import com.example.mediamarkt.CatFire.CatFire1;
import com.example.mediamarkt.CatFire.EducationalCatFire;
import com.example.mediamarkt.CatFire.FunkoCatFire;
import com.example.mediamarkt.CatFire.HolidayCatFire;
import com.example.mediamarkt.CatFire.LegoCatFire;
import com.example.mediamarkt.CatFire.PuzzlesCatFire;
import com.example.mediamarkt.CatFire.RadioCatFire;
import com.example.mediamarkt.R;
import com.example.mediamarkt.Search.AllSearch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class Home1 extends Fragment {

    public final static int QRcodeWidth = 500;
    Bitmap bitmap;
    TextView maintexthome, numbercard;
    CardView qr, intsearch;
    ImageView qrimage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home1, container, false);

        maintexthome = view.findViewById(R.id.maintexthome);
        qrimage = view.findViewById(R.id.qrimage);
        numbercard = view.findViewById(R.id.numbercard);
        qr = view.findViewById(R.id.qr);
        intsearch = view.findViewById(R.id.intsearch);
        checkname();

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