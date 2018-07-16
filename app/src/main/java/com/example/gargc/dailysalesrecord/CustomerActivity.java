package com.example.gargc.dailysalesrecord;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gargc.dailysalesrecord.Activity.AddCustomer;
import com.example.gargc.dailysalesrecord.Activity.EditCustomer;
import com.example.gargc.dailysalesrecord.Model.CustomerContent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CustomerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CustomerActivity.this, AddCustomer.class);
                startActivity(intent);
            }
        });

        recyclerView=(RecyclerView) findViewById(R.id.customer_content);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Customer");

        GridLayoutManager gridLayoutManager=new GridLayoutManager(CustomerActivity.this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(CustomerActivity.this,DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);


    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<CustomerContent,MyViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<CustomerContent, MyViewHolder>
                (
                CustomerContent.class,
                R.layout.single_customer_item,
                MyViewHolder.class,
                mDatabase
                )
             {
                  @Override
                   protected void populateViewHolder(MyViewHolder viewHolder, final CustomerContent model, int position) {
                      try {
                          Log.i("check", "1");

                          viewHolder.email.setText("Email :" + model.getEmailId());
                          viewHolder.phoneNumber.setText("Phone :" + model.getPhoneNumber());
                          viewHolder.name.setText(model.getCustomerName());
                          Picasso.with(CustomerActivity.this).load(model.getImage()).placeholder(R.mipmap.image_not_available).into(viewHolder.imageView);

                          viewHolder.view.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {

                                  Intent intent=new Intent(CustomerActivity.this, EditCustomer.class);
                                  intent.putExtra("object",model);
                                  startActivity(intent);

                              }
                          });

                          viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  Log.i("delete","true");
                                  AlertDialog.Builder alert = new AlertDialog.Builder(
                                          CustomerActivity.this);
                                  alert.setTitle("Alert!!");
                                  alert.setMessage("Are you sure to delete Customer");
                                  alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                      @Override
                                      public void onClick(DialogInterface dialogInterface, int i) {

                                          mDatabase.child(model.getCustomerName()).removeValue(new DatabaseReference.CompletionListener() {
                                              @Override
                                              public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                  Toast.makeText(CustomerActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                  Intent intent=new Intent(CustomerActivity.this,CustomerActivity.class);
                                                  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                  startActivity(intent);
                                          }
                                          });
                                      }
                                  });
                                  alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                      @Override
                                      public void onClick(DialogInterface dialogInterface, int i) {
                                          dialogInterface.dismiss();

                                      }
                                  });

                                  alert.show();

                              }
                          });
                      }catch (Exception e)
                      {

                      }



                  }
            };
            firebaseRecyclerAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView name,phoneNumber,email;
        View view;
        ImageButton imageButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            imageView=(ImageView) itemView.findViewById(R.id.customerImage);
            name=(TextView) itemView.findViewById(R.id.customernameItem);
            phoneNumber=(TextView) itemView.findViewById(R.id.customerPhoneNumberItem);
            email=(TextView) itemView.findViewById(R.id.customerEmailIdItem);
            imageButton=(ImageButton) itemView.findViewById(R.id.deleteCustomerButton);
        }
    }
}
