package com.example.rohithreddyg.adminapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

import static android.R.id.list;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class SportsComplaints extends AppCompatActivity {
    List<String> rowItems = new ArrayList<>();
    List<String> rowItems1 = new ArrayList<>();
    List<String> rowItems2 = new ArrayList<>();
    List<String> Token = new ArrayList<>();
    List<String> Status = new ArrayList<>();
    List<String> rowItems3 = new ArrayList<>();
    List<String> rowItems4 = new ArrayList<>();
    List<String> rowItems5 = new ArrayList<>();
    List<String> Token1 = new ArrayList<>();
    List<String> Status1 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_complaints);
        final DatabaseReference sports = FirebaseDatabase.getInstance().getReference().child("/Sports Complaints").getRef();
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarComp);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

        sports.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long chilrenCount = dataSnapshot.getChildrenCount();
                if(chilrenCount!=0){
                    progressBar.setVisibility(View.VISIBLE);}
                else {
                    Toast.makeText(getApplicationContext(),"No Complaints yet!!!",Toast.LENGTH_SHORT).show();
                }
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String username = dsp.getKey();

                    for (DataSnapshot dsp1 : dsp.getChildren()) {
                        String token = dsp1.getKey();
                        String subject = dsp1.child("/Subject").getValue(String.class);
                        String complaint = dsp1.child("/Complaint").getValue(String.class);
                        String status = dsp1.child("/Status").getValue(String.class);
                        if (subject != null && !status.equals("Problem Solved")) {
                            rowItems.add(subject);
                            rowItems1.add(username);
                            rowItems2.add(complaint);
                            Token.add(token);
                            Status.add(status);

                        }
                        if (subject != null && status.equals("Problem Solved")) {
                            rowItems3.add(subject);
                            rowItems4.add(username);
                            rowItems5.add(complaint);
                            Token1.add(token);
                            Status1.add(status);

                        }
                    }
                }
                progressBar.setVisibility(View.GONE);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SportsComplaints.this, R.layout.listitem, rowItems);
                ListView lv = (ListView) findViewById(R.id.listsports);
                lv.setAdapter(adapter);
                final ListView list = (ListView) findViewById(R.id.listsports);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent1 = new Intent(SportsComplaints.this, Complaint.class);
                        intent1.putExtra("subject", rowItems.get(position));
                        intent1.putExtra("username", rowItems1.get(position));
                        intent1.putExtra("complaint", rowItems2.get(position));
                        intent1.putExtra("token", Token.get(position));
                        intent1.putExtra("status", Status.get(position));
                        intent1.putExtra("activity", "Sports Complaints");
                        startActivity(intent1);
                    }
                });
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(SportsComplaints.this, R.layout.listitem, rowItems3);
                ListView lv1 = (ListView) findViewById(R.id.listView2Sports);
                lv1.setAdapter(adapter1);
                final ListView list1 = (ListView) findViewById(R.id.listView2Sports);
                list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent1 = new Intent(SportsComplaints.this, Complaint.class);
                        intent1.putExtra("subject", rowItems3.get(position));
                        intent1.putExtra("username", rowItems4.get(position));
                        intent1.putExtra("complaint", rowItems5.get(position));
                        intent1.putExtra("token", Token1.get(position));
                        intent1.putExtra("status", Status1.get(position));
                        intent1.putExtra("activity", "Sports Complaints");
                        startActivity(intent1);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
