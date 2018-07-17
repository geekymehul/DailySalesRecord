package com.example.gargc.dailysalesrecord.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gargc.dailysalesrecord.Model.ProductContent;
import com.example.gargc.dailysalesrecord.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProductActivity extends AppCompatActivity
{
    private RecyclerView productList;
    private DatabaseReference productDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.product_btn_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductActivity.this,AddProductActivity.class));
            }
        });

        productList = (RecyclerView)findViewById(R.id.product_list);
        productList.setLayoutManager(new LinearLayoutManager(this));
        productList.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();
        productDatabase = FirebaseDatabase.getInstance().getReference().child("Products").child(mAuth.getCurrentUser().getUid());

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<ProductContent,ProductViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ProductContent, ProductViewHolder>
                (
                        ProductContent.class,
                        R.layout.single_product_item,
                        ProductViewHolder.class,
                        productDatabase
                ) {
            @Override
            protected void populateViewHolder(final ProductViewHolder viewHolder, ProductContent model, int position)
            {
                final String product_key = getRef(position).getKey();

                viewHolder.name.setText(model.getName());
                viewHolder.sp.setText("Selling Price  "+model.getSelling_price()+" "+model.getCurrency());
                viewHolder.ap.setText("Actual Price  "+model.getActual_price()+" "+model.getCurrency());
                viewHolder.sku.setText("SKU : "+model.getSku());
                viewHolder.stock.setText("Stock left "+model.getStock()+" "+model.getQuantity());
                viewHolder.notes.setText(model.getNotes());

                if(!model.getImage().equals("none"))
                    Picasso.with(getApplicationContext()).load(model.getImage()).into(viewHolder.img);

                final DatabaseReference itemDatabase = productDatabase.child(model.getName());

                viewHolder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(viewHolder.notes.getVisibility()==View.GONE)
                        {
                            viewHolder.notes.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            viewHolder.notes.setVisibility(View.GONE);
                        }
                    }
                });

                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemDatabase.removeValue();
                    }
                });

                viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Intent edit = new Intent(ProductActivity.this,EditProductActivity.class);
                        edit.putExtra("product_id",product_key);
                        startActivity(edit);
                    }
                });

            }
        };

        productList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder
    {
        TextView sku,stock,sp,ap,name,notes;
        ImageView img,edit,delete;
        LinearLayout container;

        public ProductViewHolder(View itemView)
        {
            super(itemView);

            container = (LinearLayout)itemView.findViewById(R.id.single_product_container);
            sku = (TextView)itemView.findViewById(R.id.single_product_sku);
            stock = (TextView)itemView.findViewById(R.id.single_product_stock);
            sp = (TextView)itemView.findViewById(R.id.single_product_sp);
            ap = (TextView)itemView.findViewById(R.id.single_product_ap);
            name = (TextView)itemView.findViewById(R.id.single_product_name);
            notes = (TextView)itemView.findViewById(R.id.single_product_notes);
            img = (ImageView)itemView.findViewById(R.id.single_product_img);
            edit = (ImageView)itemView.findViewById(R.id.single_product_edit);
            delete = (ImageView)itemView.findViewById(R.id.single_product_delete);
        }

    }

}
