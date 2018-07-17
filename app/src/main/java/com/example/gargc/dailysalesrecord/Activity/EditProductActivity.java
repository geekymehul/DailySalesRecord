package com.example.gargc.dailysalesrecord.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gargc.dailysalesrecord.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProductActivity extends AppCompatActivity
{

    private FirebaseAuth mAuth;
    private DatabaseReference productDatabase,itemDatabase;
    private EditText edt_name,edt_currency,edt_qty,edt_stock,edt_sell,edt_actual,edt_notes,edt_sku;
    private ImageView btn_img;
    private Uri mImageUri = null;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private Button btn_add;
    private String productName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        productName = getIntent().getStringExtra("product_id");

        mAuth = FirebaseAuth.getInstance();
        productDatabase = FirebaseDatabase.getInstance().getReference().child("Products").child(mAuth.getCurrentUser().getUid());
        mStorage = FirebaseStorage.getInstance().getReference();
        itemDatabase = productDatabase.child(productName);

        itemDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                edt_actual.setText(dataSnapshot.child("actual_price").getValue().toString());
                edt_currency.setText(dataSnapshot.child("currency").getValue().toString());
                edt_name.setText(dataSnapshot.child("name").getValue().toString());
                edt_qty.setText(dataSnapshot.child("quantity").getValue().toString());
                edt_stock.setText(dataSnapshot.child("stock").getValue().toString());
                edt_sell.setText(dataSnapshot.child("selling_price").getValue().toString());
                edt_notes.setText(dataSnapshot.child("notes").getValue().toString());
                edt_sku.setText(dataSnapshot.child("sku").getValue().toString());

                if(!dataSnapshot.child("image").getValue().toString().equals("none"))
                    Picasso.with(getApplicationContext()).load(dataSnapshot.child("image").getValue().toString()).into(btn_img);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProgress = new ProgressDialog(EditProductActivity.this);

        edt_actual = (EditText)findViewById(R.id.edit_pro_edt_actual_price);
        edt_currency = (EditText)findViewById(R.id.edit_pro_edt_curr);
        edt_name = (EditText)findViewById(R.id.edit_pro_edt_name);
        edt_qty = (EditText)findViewById(R.id.edit_pro_edt_qty);
        edt_stock = (EditText)findViewById(R.id.edit_pro_edt_stock);
        edt_sell = (EditText)findViewById(R.id.edit_pro_edt_selling_price);
        edt_notes = (EditText)findViewById(R.id.edit_pro_edt_notes);
        edt_sku = (EditText)findViewById(R.id.edit_pro_edt_sku);
        btn_img = (ImageView)findViewById(R.id.edit_pro_btn_img);
        btn_add = (Button)findViewById(R.id.edit_pro_btn_add);

        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent , 1);

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final String actual = edt_actual.getText().toString();
                final String sell = edt_sell.getText().toString();
                final String name = edt_name.getText().toString();
                final String currency = edt_currency.getText().toString();
                final String sku = edt_sku.getText().toString();
                final String stock = edt_stock.getText().toString();
                final String qty = edt_qty.getText().toString();
                final String notes = edt_notes.getText().toString();

                if(TextUtils.isEmpty(actual) || TextUtils.isEmpty(sell) || TextUtils.isEmpty(name) || TextUtils.isEmpty(currency) ||
                        TextUtils.isEmpty(stock))
                {
                    Toast.makeText(EditProductActivity.this, "Enter these details", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mProgress.setTitle("Posting to Blog");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();

                    if(mImageUri!=null)
                    {
                        StorageReference filepath = mStorage.child("Products").child(mAuth.getCurrentUser().getUid())
                                .child(mImageUri.getLastPathSegment());
                        filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                            {
                                @SuppressWarnings("VisibleForTests") final Uri imgUrl = taskSnapshot.getDownloadUrl();

                                DatabaseReference info = productDatabase.child(name);
                                info.child("name").setValue(name);
                                info.child("selling_price").setValue(sell);
                                info.child("actual_price").setValue(actual);
                                info.child("quantity").setValue(qty);
                                info.child("image").setValue(imgUrl.toString());
                                info.child("currency").setValue(currency);
                                info.child("stock").setValue(stock);
                                info.child("sku").setValue(sku);
                                info.child("notes").setValue(notes);

                                Toast.makeText(EditProductActivity.this, "changes saved", Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
                            }
                        });
                    }
                    else
                    {
                        DatabaseReference info = productDatabase.child(name);
                        info.child("name").setValue(name);
                        info.child("selling_price").setValue(sell);
                        info.child("actual_price").setValue(actual);
                        info.child("quantity").setValue(qty);
                        info.child("image").setValue("none");
                        info.child("currency").setValue(currency);
                        info.child("stock").setValue(stock);
                        info.child("sku").setValue(sku);
                        info.child("notes").setValue(notes);

                        Toast.makeText(EditProductActivity.this, "changes saved", Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();
                    }

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK)
        {
            mImageUri = data.getData();
        }
    }

}
