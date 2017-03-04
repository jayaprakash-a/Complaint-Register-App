package com.example.rohithreddyg.adminapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HComplaints extends AppCompatActivity {
    String username, subject, complaint, room, hostel;
    static boolean calledAlready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hcomplaints);
        username = getIntent().getExtras().get("username").toString();
        subject = getIntent().getExtras().get("subject").toString();
        complaint = getIntent().getExtras().get("complaint").toString();
        room = getIntent().getExtras().getString("roomNo");
        hostel = getIntent().getExtras().getString("Hostel");
        String actname = getIntent().getExtras().getString("activity");
        final String token = getIntent().getExtras().getString("token");
        Button solved = (Button) findViewById(R.id.solvedhc);
        final DatabaseReference random = FirebaseDatabase.getInstance().getReference().child(actname).getRef();
        final EditText message = (EditText) findViewById(R.id.message_edit_hc);
        if (!calledAlready) {
            random.child(username).child(token).child("Flag").setValue("Pending");
            random.child(username).child(token).child("Status").setValue("Problem Seen.");
            calledAlready = true;

        }
        random.child(username).child(token).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String replyfromuser = dataSnapshot.child("UserReply").getValue(String.class);
                TextView reply = (TextView) findViewById(R.id.message_user_text);
                reply.setText(replyfromuser);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        TextView tv = (TextView) findViewById(R.id.rollno);
        tv.setText(username);
        TextView tv1 = (TextView) findViewById(R.id.subject);
        tv1.setText(subject);
        TextView tv2 = (TextView) findViewById(R.id.hccomplaint);
        tv2.setText(complaint);
        TextView tv3 = (TextView) findViewById(R.id.roomNo);
        tv3.setText(room);
        TextView tv4 = (TextView) findViewById(R.id.hostel1);
        tv4.setText(hostel);
        solved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                random.child(username).child(token).child("Status").setValue("Problem Solved");
                random.child(username).child(token).child("Flag").setValue("Finished");

                if (!message.getText().toString().isEmpty())
                    random.child(username).child(token).child("Message").setValue(message.getText().toString());
                finish();
            }
        });
        Button sent = (Button) findViewById(R.id.button3);

        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!message.getText().toString().isEmpty()){
                    random.child(username).child(token).child("Message").setValue(message.getText().toString());
                    Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(), "Please enter the message", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
