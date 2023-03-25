package com.example.mediamarkt.Navigation;

import static android.os.Build.VERSION.SDK_INT;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimatedImageDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.mediamarkt.Auth.Auth;
import com.example.mediamarkt.Auth.User;
import com.example.mediamarkt.Svyaz.Svyaz11;
import com.example.mediamarkt.Svyaz.Svyaz12;
import com.example.mediamarkt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.w3c.dom.Text;

import java.io.IOException;


public class Profile extends Fragment {

    public final static int QRcodeWidth = 500;

    Bitmap bitmap;
    TextView maintexthome, numbercard, pricetextsheet22;
    ImageView videoPlayer;
    Button exit;
    TextView name1, nameprofilefirebase, telprofilefirebase;
    ImageView photoprofile;
    RelativeLayout svyaz1, svyaz2;
    ImageView qrimage;
    CardView qr;

    public static String PACKAGE_NAME;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        nameprofilefirebase = (TextView) view.findViewById(R.id.nameprofilefirebase);
        telprofilefirebase = (TextView) view.findViewById(R.id.telprofilefirebase);
        exit = (Button) view.findViewById(R.id.exit);
        svyaz1 = view.findViewById(R.id.svyaz1);
        svyaz2 = view.findViewById(R.id.svyaz2);
        qrimage = view.findViewById(R.id.qrimage);
        numbercard = view.findViewById(R.id.numbercard);
        qr = view.findViewById(R.id.qr);
        checkname();
        checktel();
        checkcard();
        checkcard2();
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(new Intent(getActivity(), Auth.class));
                startActivity(i);
            }
        });

        svyaz1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Svyaz11.class);
                startActivity(intent);
            }
        });

        svyaz2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Svyaz12.class);
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
                nameprofilefirebase.setText(String.valueOf(task.getResult().getValue()));
            }
        });
    }

    public void checktel() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usuariosRef = rootRef.child("Пользователи");
        usuariosRef.child(uid).child("phone").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                telprofilefirebase.setText(String.valueOf(task.getResult().getValue()));
            }
        });
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