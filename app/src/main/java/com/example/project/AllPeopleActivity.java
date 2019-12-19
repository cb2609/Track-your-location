package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.project.Interface.IFirebaseLoadDone;
import com.example.project.Interface.IRecyclerItemClickListener;
import com.example.project.Model.User;
import com.example.project.Utils.Common;
import com.example.project.ViewHolder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Query;

public class AllPeopleActivity extends AppCompatActivity implements IFirebaseLoadDone {

    FirebaseRecyclerAdapter<User, UserViewHolder> adapter,searchAdapter;
    RecyclerView recyclerAllUser;
    IFirebaseLoadDone firebaseLoadDone;

    MaterialSearchBar searchBar;
    List<String> suggestList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_people);

        //Init View
        searchBar = (MaterialSearchBar)findViewById(R.id.material_search_bar);
        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest = new ArrayList<>();
                for (String search:suggestList)
                {
                    if(search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                searchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled){
                    if(adapter!=null)
                    {
                        recyclerAllUser.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
        recyclerAllUser = (RecyclerView)findViewById(R.id.recycler_all_people);
        recyclerAllUser.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerAllUser.setLayoutManager(layoutManager);
        recyclerAllUser.addItemDecoration(new DividerItemDecoration(this, ((LinearLayoutManager) layoutManager).getOrientation()));

        firebaseLoadDone = this;
        loadUserList();
        loadSearchData();
    }

    private void loadSearchData() {
        final List<String> lstUserEmail = new ArrayList<>();
        DatabaseReference userList = FirebaseDatabase.getInstance()
                .getReference(Common.USER_INFORMATION);
        userList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapShot:dataSnapshot.getChildren())
                {
                    User user = userSnapShot.getValue(User.class);
                    lstUserEmail.add(user.getEmail());
                }
                firebaseLoadDone.onFirebaseLoadUserNameDone(lstUserEmail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                firebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
            }
        });
    }

    private void loadUserList() {
        DatabaseReference query = FirebaseDatabase.getInstance().getReference().child(Common.USER_INFORMATION);
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery((com.google.firebase.database.Query) query,User.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i, @NonNull User user) {
                if(user.getEmail().equals(Common.loggedUser.getEmail()))
                {
                    userViewHolder.txt_user_email.setText(new StringBuilder(user.getEmail()).append(" (me)"));
                    userViewHolder.txt_user_email.setTypeface(userViewHolder.txt_user_email.getTypeface(), Typeface.ITALIC);
                }
                else {
                    userViewHolder.txt_user_email.setText(new StringBuilder(user.getEmail()));
                }

                //event
                userViewHolder.setiRecyclerItemClickListener(new IRecyclerItemClickListener() {
                    @Override
                    public void onItemClickListener(View view, int position) {

                    }
                });
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_user,parent,false);
                return new UserViewHolder(itemView);

            }
        };

        adapter.startListening();
        recyclerAllUser.setAdapter(adapter);
    }

    @Override
    protected void onStop(){
        if(adapter!=null)
            adapter.stopListening();
        if(searchAdapter!=null)
            searchAdapter.stopListening();
        super.onStop();
    }

    @Override
    protected void onResume() {
        if (adapter!= null)
            adapter.startListening();
        if (searchAdapter!=null)
            searchAdapter.startListening();
        super.onResume();
    }

    private void startSearch(String txt_search) {
        com.google.firebase.database.Query query = FirebaseDatabase.getInstance()
                .getReference(Common.USER_INFORMATION)
                .orderByChild("name")
                .startAt(txt_search);

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery((com.google.firebase.database.Query) query,User.class)
                .build();

        searchAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i, @NonNull User user) {
                if(user.getEmail().equals(Common.loggedUser.getEmail()))
                {
                    userViewHolder.txt_user_email.setText(new StringBuilder(user.getEmail()).append(" (me)"));
                    userViewHolder.txt_user_email.setTypeface(userViewHolder.txt_user_email.getTypeface(), Typeface.ITALIC);
                }
                else {
                    userViewHolder.txt_user_email.setText(new StringBuilder(user.getEmail()));
                }

                //event
                userViewHolder.setiRecyclerItemClickListener(new IRecyclerItemClickListener() {
                    @Override
                    public void onItemClickListener(View view, int position) {

                    }
                });
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_user,parent,false);
                return new UserViewHolder(itemView);

            }
        };

        searchAdapter.startListening();
        recyclerAllUser.setAdapter(searchAdapter);
    }

    @Override
    public void onFirebaseLoadUserNameDone(List<String> lstemail) {
        searchBar.setLastSuggestions(lstemail);
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
