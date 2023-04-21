package com.example.bookaholic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bookaholic.voucher.VoucherActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {


    private static final String TAG = "MainActivity";
    public static FirebaseAuth firebaseAuth;
    public static FirebaseUser firebaseUser;
    public static FirebaseDatabase firebaseDatabase;
    public static User currentUser;
    public static DatabaseReference currentUserDatabaseRef;

    private ImageView imgviewTakeCam, imgviewBack;
    private EditText edtPhone, edtName, edtEmail, edtAddress;
    private Button btnUpdateInfo;
    private TextView addBook, createVoucher, manageVoucher, manageUser, statistic;
    private ImageButton buttonSignOut;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void initCurrentUser() {
        currentUserDatabaseRef = firebaseDatabase
                .getReference("Users")
                .child(firebaseUser.getUid());
        currentUserDatabaseRef.addValueEventListener(onCurrentUserDataChanged);
    }

    private final ValueEventListener onCurrentUserDataChanged = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            currentUser = snapshot.getValue(User.class);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d(TAG, error.toString());
        }

        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {

                        imgviewTakeCam.setImageURI(uri);
                    }
                });
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentUser = MainActivity.currentSyncedUser;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();
        String firebaseUserID = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference cur_user = firebaseDatabase.getReference("Users").child(firebaseUserID);

        View view = inflater.inflate(R.layout.activity_profile_fragment, container, false);

//        edtAddress = view.findViewById(R.id.edt_address);
//        edtName = view.findViewById(R.id.edt_name);
        buttonSignOut = view.findViewById(R.id.button_profile_logout);


        addBook = view.findViewById(R.id.addBook);
        createVoucher = view.findViewById(R.id.createVoucher);
        manageVoucher = view.findViewById(R.id.manageVoucher);
        manageUser = view.findViewById(R.id.manageUser);
        statistic = view.findViewById(R.id.statistics);
        statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Statistic.class));
            }
        });
//        if(currentUser!=null){
//            edtAddress.setText(currentUser.getAddress());
//            edtPhone.setText(currentUser.getPhoneNumber());
//            edtEmail.setText(currentUser.getEmail());
//            edtName.setText(currentUser.getFullName());
//        }

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity() , AddBook.class));
            }
        });

        createVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreateVoucher.class));
            }
        });
        manageVoucher.setOnClickListener(new View.OnClickListener() {
            //VoucherActivity
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), VoucherActivity.class));
            }
        });


        buttonSignOut.setOnClickListener(v -> signOut());
        // Inflate the layout for this fragment
        return view;
    }

    private void signOut() {
        try {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this.getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}