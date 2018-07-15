package com.example.gargc.dailysalesrecord.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gargc.dailysalesrecord.CustomerActivity;
import com.example.gargc.dailysalesrecord.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddCustomer extends AppCompatActivity {

    EditText name,phoneNumber,customerEmail,amountPaid,notes;

    Button button;
    private ProgressBar progressBar;

    Uri mImageUri=null;

    private String customerName,customerEmailId,customerAmountPaid,customerNotes,customerPhoneNumber;

    CircleImageView circleImageView;

    private DatabaseReference mDatabase;
    StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        name=(EditText) findViewById(R.id.customerName);
        phoneNumber=(EditText) findViewById(R.id.customerphoneNumber);
        customerEmail =(EditText) findViewById(R.id.customerEmailId);
        amountPaid=(EditText) findViewById(R.id.customerAmountPaid);
        notes=(EditText) findViewById(R.id.customerNotes);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        circleImageView=(CircleImageView) findViewById(R.id.addImage);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Customer");
        mStorage = FirebaseStorage.getInstance().getReference();

        button=(Button) findViewById(R.id.btSignIn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerName=name.getText().toString().trim();
                customerEmailId=customerEmail.getText().toString().trim().toLowerCase();
                customerAmountPaid=amountPaid.getText().toString().trim();
                customerNotes=notes.getText().toString().trim();
                customerPhoneNumber=phoneNumber.getText().toString();

                if (customerName.length() < 2) {
                    Toast.makeText(AddCustomer.this, "Name must be at least 2 characters", Toast.LENGTH_LONG).show();
                } else if (!customerName.matches("^[^<>'\"/;`%&!@#$*()+=:?.,~|{}]*$")) {
                    Toast.makeText(AddCustomer.this, "Name cannot contain special characters", Toast.LENGTH_LONG).show();
                    name.requestFocus();
                } else if (!customerEmailId.matches("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")) {
                    Toast.makeText(AddCustomer.this, "Please enter a valid email id", Toast.LENGTH_LONG).show();
                    customerEmail.requestFocus();
                } else if (!(customerPhoneNumber.length() == 10 && (customerPhoneNumber.startsWith("9") || customerPhoneNumber.startsWith("8") || customerPhoneNumber.startsWith("7")))) {
                    Toast.makeText(AddCustomer.this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();
                    phoneNumber.requestFocus();
                }
                else if(mImageUri==null)
                {
                    Toast.makeText(AddCustomer.this, "Add Customer Image", Toast.LENGTH_SHORT).show();
                    circleImageView.requestFocus();
                }
                else {
                    toggleViews(false);
                    button.setEnabled(false);
                    addCustomer();
                    return;
                }
                toggleViews(true);
                button.setEnabled(true);

            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent , 1);

            }
        });


    }

    private void addCustomer() {


        StorageReference filepath = mStorage.child(mImageUri.getLastPathSegment());
        Log.i("check","1");

        filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("check",mImageUri+"2");


                try {
                    @SuppressWarnings("VisibleForTests")
                    Uri imgUri = taskSnapshot.getDownloadUrl();

                    DatabaseReference userReq = mDatabase.child(customerName);
                    Log.i("check","3");



                    HashMap userReqMap = new HashMap();
                    userReqMap.put("CustomerName", customerName);
                    userReqMap.put("EmailId", customerEmailId);
                    userReqMap.put("PhoneNumber", customerPhoneNumber);
                    userReqMap.put("AmountPaid", customerAmountPaid);
                    userReqMap.put("Notes", customerNotes);
                    userReqMap.put("Image", imgUri.toString());

                    userReq.setValue(userReqMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddCustomer.this, "Customer Added successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddCustomer.this, CustomerActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(AddCustomer.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
                catch (Exception e)
                {
                    Log.i("error","true");
                }
            }
        });




    }

    private void toggleViews(boolean enabled) {
        progressBar.setVisibility((enabled) ? View.GONE : View.VISIBLE);
        button.setVisibility((enabled) ? View.VISIBLE : View.INVISIBLE);

        name.setEnabled(enabled);
        customerEmail.setEnabled(enabled);
        phoneNumber.setEnabled(enabled);
        amountPaid.setEnabled(enabled);
        notes.setEnabled(enabled);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK)
        {
            mImageUri = data.getData();
            circleImageView.setImageURI(mImageUri);
        }
    }
}
