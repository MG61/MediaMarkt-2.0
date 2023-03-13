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

public class Home1 extends Fragment{

    public final static int QRcodeWidth = 500;
    Bitmap bitmap;
    TextView maintexthome, numbercard, pricetextsheet22;
    CardView qr, intsearch;
    ImageView qrimage, imagesheet22;
    String code1 = "";


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
        checkcard();
        checkcard2();

        intsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllSearch.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
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

    public void checkcard() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usuariosRef = rootRef.child("Пользователи");
        usuariosRef.child(uid).child("card").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                numbercard.setText(String.valueOf(task.getResult().getValue()));
            }
        });
    }

    public void checkcard2() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usuariosRef = rootRef.child("Пользователи");
        usuariosRef.child(uid).child("card").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                try {
                    bitmap = TextToImageEncode(String.valueOf(task.getResult().getValue()));
                    qrimage.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}