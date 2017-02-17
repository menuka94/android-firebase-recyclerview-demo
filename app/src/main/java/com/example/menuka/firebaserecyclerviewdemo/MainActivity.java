package com.example.menuka.firebaserecyclerviewdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.menuka.firebaserecyclerviewdemo.models.Post;
import com.example.menuka.firebaserecyclerviewdemo.utils.Constants;
import com.example.menuka.firebaserecyclerviewdemo.utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mPostRV;
    private FirebaseRecyclerAdapter<Post, PostViewHolder> mPostAdapter;
    private DatabaseReference mPostRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeScreen();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPostToFirebase();
            }
        });
    }

    private void sendPostToFirebase() {
        Post post = new Post();
        String UID = Utils.getUID();

        post.setUID(UID);
        post.setNumLikes(0);
        post.setImageUrl();

    }

    private void initializeScreen() {
        mPostRV = (RecyclerView) findViewById(R.id.post_recycler_view);
        mPostRV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mPostRef = FirebaseDatabase.getInstance().getReference(Constants.POSTS);
        setupAdapter();
    }

    private void setupAdapter() {
        mPostAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.item_layout_post,
                PostViewHolder.class,
                mPostRef
        ){
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, final Post model, int position) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.getImageUrl());
                Glide.with(MainActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .into(viewHolder.postImageView);

                viewHolder.setPostImage(model.getImageUrl());
                viewHolder.setNumLikes(model.getNumLikes());
                viewHolder.postLikeImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateNumLikes(model.getUID());
                    }
                });
            }
        };
    }

    private void updateNumLikes(String uid) {
        mPostRef.child(uid).child(Constants.NUM_LIKES)
                .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        long num = (long) mutableData.getValue();
                        num++;
                        mutableData.setValue(num);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    }
                });
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        public ImageView postImageView;
        public ImageView postLikeImageView;
        public TextView numLikesTextView;

        public PostViewHolder(View itemView) {
            super(itemView);

            postImageView = (ImageView) itemView.findViewById(R.id.post_image_view);
            postLikeImageView = (ImageView) itemView.findViewById(R.id.like_image_view);
            numLikesTextView = (TextView) itemView.findViewById(R.id.num_likes_text_view);
        }

        public void setPostImage(String url){
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        }

        public void setNumLikes(long num){
            numLikesTextView.setText(String.valueOf(num));
        }
    }
}
