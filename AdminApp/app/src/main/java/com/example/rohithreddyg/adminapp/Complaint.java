package com.example.rohithreddyg.adminapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class Complaint extends AppCompatActivity {
    String username, subject, complaint;
    static boolean calledAlready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        username = getIntent().getExtras().getString("username");
        subject = getIntent().getExtras().getString("subject");
        complaint = getIntent().getExtras().getString("complaint");
        String actname = getIntent().getExtras().getString("activity");
        final String token = getIntent().getExtras().getString("token");
        Button solved = (Button) findViewById(R.id.solved);
        final DatabaseReference random = FirebaseDatabase.getInstance().getReference().child(actname).getRef();
        final EditText message = (EditText) findViewById(R.id.message_edit);
        random.child(username).child(token).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String replyfromuser = dataSnapshot.child("Subject").getValue(String.class);
                TextView reply = (TextView) findViewById(R.id.message_user_act_text);
                reply.setText(replyfromuser);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        if (!calledAlready) {
            random.child(username).child(token).child("Flag").setValue("Pending");
            random.child(username).child(token).child("Status").setValue("Problem Seen.");
            calledAlready = true;

        }
        TextView rollNum = (TextView) findViewById(R.id.rollno);
        rollNum.setText(username);
        TextView sub = (TextView) findViewById(R.id.subject);
        sub.setText(subject);
        TextView comp = (TextView) findViewById(R.id.complaint);
        comp.setText(complaint);
        solved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Problem Solved", Toast.LENGTH_SHORT).show();
                random.child(username).child(token).child("Status").setValue("Problem Solved");
                random.child(username).child(token).child("Flag").setValue("Finished");

                if (!message.getText().toString().isEmpty())
                    random.child(username).child(token).child("Message").setValue(message.getText().toString());
                finish();
            }
        });
        Button sent = (Button) findViewById(R.id.button2);
        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!message.getText().toString().isEmpty()) {
                    random.child(username).child(token).child("Message").setValue(message.getText().toString());
                    Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
                    finish();

                } else
                    Toast.makeText(getApplicationContext(), "Please enter the message", Toast.LENGTH_SHORT).show();

            }
        });

    }


}
