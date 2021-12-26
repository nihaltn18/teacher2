package com.example.teacher;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> name;
    ArrayList<String> code;
    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        name = new ArrayList<>();
        code = new ArrayList<>();
        adapter = new RecyclerViewAdapter(MainActivity2.this,name,code);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot = task.getResult();
                ClassObj obj;
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    obj = snapshot1.getValue(ClassObj.class);
                    name.add(obj.getClass_name());
                    code.add(obj.getClass_code());
                }
                ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        MenuItem menuItem = menu.findItem(R.id.searchclass);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void filter(String newText) {
        ArrayList<String> fname,fcode;
        fname = new ArrayList<>();
        fcode = new ArrayList<>();
        for(String s:code)
        {
            if(s.toLowerCase().contains(newText.toLowerCase()))
            {
                fcode.add(s);
                fname.add(name.get(code.indexOf(s)));
            }
        }
        adapter.filterList(fcode,fname);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.searchclass:
                //TODO (what to do when user clicks on search bar)

                break;
            case R.id.addClass:
                //what to do when user clicks on add bar
                startActivity(new Intent(MainActivity2.this,addclass.class));
                break;
            case R.id.signout:
                //signout
                FirebaseAuth.getInstance().signOut();
                GoogleSignIn.getClient(
                        MainActivity2.this,
                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                ).signOut();
                startActivity(new Intent(MainActivity2.this,MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}