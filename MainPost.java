package com.example.android.sfwhf1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class MainPost extends AppCompatActivity {

    private RecyclerView mPostList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child("post");
        mPostList = (RecyclerView)findViewById(R.id.postList);
        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.row_view,
                PostViewHolder.class,
                mDatabase
        ){
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, final Post model, int position) {
                viewHolder.setUname(model.getUname());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setComments(model.getComments());
                viewHolder.setImage(model.getImage());

            }
        };
        mPostList.setAdapter(firebaseRecyclerAdapter);
    }
    public static class PostViewHolder extends RecyclerView.ViewHolder{
        TextView post_name,post_title, post_comments;
        ImageView image_View;
        public PostViewHolder(View itemView){
            super(itemView);
            post_name = (TextView)itemView.findViewById(R.id.nameView);
            post_title = (TextView)itemView.findViewById(R.id.titleView);
            post_comments = (TextView)itemView.findViewById(R.id.commentsView);
            image_View = (ImageView) itemView.findViewById(R.id.imageView);
        }
        public void setUname(String name){
            post_name.setText(name);
        }
        public void setTitle(String title){
            post_title.setText(title);
        }
        public void setComments(String comments){
            post_comments.setText(comments);
        }

        public void setImage(String image) {
            Picasso.with(itemView.getContext())
                    .load(image)
                    .centerCrop()
                    .into(image_View);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add){
            startActivity(new Intent(MainPost.this, PostViewActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
