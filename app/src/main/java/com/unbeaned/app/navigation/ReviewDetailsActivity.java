package com.unbeaned.app.navigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.unbeaned.app.R;
import com.unbeaned.app.adapters.CommentFeedAdapter;
import com.unbeaned.app.adapters.ReviewFeedAdapter;
import com.unbeaned.app.databinding.ActivityReviewDetailsBinding;
import com.unbeaned.app.models.Comment;
import com.unbeaned.app.models.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewDetailsActivity extends AppCompatActivity {

    public static final String TAG = "ReviewDetailsActivity";
    ActivityReviewDetailsBinding binding;
    public Review currentReview;
    public String reviewId;
    private ImageView ivProfileImage;
    private TextView tvUsername;
    private TextView tvName;
    private TextView tvRating;
    //private TextView tvPlaceName;
    private TextView tvReviewTitle;
    private TextView tvReview;
    private CarouselView carouselView;
    private Button btnComment;
    private EditText etComment;
    private RecyclerView rvComments;
    private RelativeLayout reviewItemLinearContainer;
    private CommentFeedAdapter adapter;
    private List<Comment> allComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"Made it to Review Detail Activity");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_details);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                reviewId= null;
            } else {
                reviewId= extras.getString("review");
            }
        } else {
            reviewId= (String) savedInstanceState.getSerializable("review");
        }
        Log.i(TAG, "Review: "+reviewId);
        Log.i(TAG, "Querying Reviews");

        queryReview(reviewId);
        Log.i(TAG, "Review: "+currentReview.getObjectId());
        binding.setReview(currentReview);
        ivProfileImage = binding.ivProfileImage;
        tvUsername = binding.tvUsername;
        tvName = binding.tvName;
        //tvPlaceName = binding.tvPlaceName;
        tvReviewTitle = binding.tvReviewTitle;
        tvRating = binding.tvRating;
        tvReview = binding.tvReview;
        btnComment = binding.btnComment;
        carouselView = binding.carouselView;
        reviewItemLinearContainer = binding.reviewItemLinearContainer;
        rvComments = binding.rvComments;
        etComment = binding.etComment;
        allComments= new ArrayList<>();
        adapter = new CommentFeedAdapter(this, allComments);
        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        queryComments();
        //TODO: Populate the Carousel View with images
        currentReview.setImages(currentReview);
        if(currentReview.images.size()!=0){
            carouselView.setPageCount(currentReview.images.size());

            carouselView.setImageListener(new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    Glide.with(reviewItemLinearContainer.getContext())
                            .load(currentReview.images.get(position).getImage().getUrl())
                            .into(imageView);
                }
            });
        }
        else{
            //hide carousel view if no images
            reviewItemLinearContainer.removeView(carouselView);
        }
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = etComment.getText().toString();
                if(comment.isEmpty()){
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                saveComment(comment, currentUser, currentReview);
                queryComments();
            }
        });
    }

    private void saveComment(String comment, ParseUser currentUser, Review currentReview) {
        Comment commentItem = new Comment();
        commentItem.setComment(comment);
        commentItem.setReview(currentReview);
        commentItem.setUser(currentUser);
        commentItem.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Error while saving", e);
                    return;
                }
                Log.i(TAG, "Comment was saved successfully");
            }
        });
        etComment.setText("");

    }

    private void queryReview(String reviewId) {
        Log.i(TAG, "Querying Reviews");
        List<Review> reviews = new ArrayList<>();

        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);

        query.include(Review.KEY_USER);
        query.whereEqualTo(Review.KEY_REVIEW_ID, reviewId);
        try {
            reviews=query.find();
            Log.i(TAG, "Reviews" +reviews);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (reviews!=null){
            currentReview=reviews.get(0);
        }
        }

    private void queryComments() {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);

        query.include(Comment.KEY_USER);
        query.whereEqualTo(Comment.KEY_REVIEW, currentReview);
        query.addDescendingOrder(Comment.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issue with getting comments", e);
                    return;
                }

                adapter.clear();
                adapter.addAll(comments);
            }
        });
    }
}


