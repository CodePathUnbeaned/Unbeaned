package com.unbeaned.app.navigation;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.unbeaned.app.R;
import com.unbeaned.app.adapters.CommentFeedAdapter;
import com.unbeaned.app.databinding.FragmentReviewDetailBinding;
import com.unbeaned.app.models.Comment;
import com.unbeaned.app.models.Review;
import com.unbeaned.app.utils.EndlessScrollView;
import com.unbeaned.app.utils.Requests;

import java.util.ArrayList;
import java.util.List;


public class ReviewDetailFragment extends Fragment {

    public static final String TAG = "ReviewDetailFragment";
    FragmentReviewDetailBinding binding;
    private Review review;
    private String backPath;
    ScrollView nestedScrollViewDetails;
    ImageView ivReviewItemProfile;
    TextView tvReviewProfileName;
    TextView tvReviewRating;
    TextView tvReviewItemContent;
    TextView tvReviewPlaceName;
    CarouselView carouselViewItem;
    RecyclerView rvReviewDetailsComments;
    EditText etComment;
    Button btnComment;
    RelativeLayout reviewContainer;
    List<Comment> allComments;
    CommentFeedAdapter adapter;
    EndlessScrollView endlessNestedScrollView;
    

    public ReviewDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review_detail, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            review = getArguments().getParcelable("review");
            backPath = getArguments().getString("backPath");
        }
        nestedScrollViewDetails = binding.nestedScrollViewDetails;
        ivReviewItemProfile = binding.ivReviewItemProfile;
        tvReviewProfileName = binding.tvReviewProfileName;
        tvReviewRating = binding.tvReviewRating;
        tvReviewItemContent = binding.tvReviewItemContent;
        carouselViewItem = binding.carouselReviewItem;
        rvReviewDetailsComments = binding.rvReviewDetailsComments;
        tvReviewPlaceName = binding.tvReviewPlaceName;
        etComment = binding.etComment;
        btnComment = binding.btnComment;
        reviewContainer = binding.reviewContainer;
        allComments = new ArrayList<>();

        adapter = new CommentFeedAdapter(getContext(), allComments, ReviewDetailFragment.this);
        rvReviewDetailsComments.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        rvReviewDetailsComments.setLayoutManager(layoutManager);
        rvReviewDetailsComments.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        review.setImages();
        if(review.images.size()!=0){
            carouselViewItem.setImageListener(new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    Glide.with(getContext())
                            .load(review.images.get(position).getImage().getUrl())
                            .into(imageView);
                }
            });
            carouselViewItem.setPageCount(review.images.size());
        }
        else{
            //hide carousel view if no images
            reviewContainer.removeView(carouselViewItem);
        }

        loadMoreData(true, 0);
        endlessNestedScrollView = new EndlessScrollView(rvReviewDetailsComments, layoutManager){
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "Loading: " + page);
                loadMoreData(false, page);
            }
        };

        nestedScrollViewDetails.setOnScrollChangeListener(endlessNestedScrollView);

        ivReviewItemProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = review.getUser();
                ReviewDetailFragmentDirections.ActionReviewDetailFragmentToOtherUserFragment action = ReviewDetailFragmentDirections.actionReviewDetailFragmentToOtherUserFragment(user);
                action.setBackPath("placeDetails");
                NavHostFragment.findNavController(ReviewDetailFragment.this).navigate(action);
            }
        });
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = etComment.getText().toString();
                if(comment.isEmpty()){
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                saveComment(comment, currentUser, review);
                adapter.clear();
                Requests.getNextPageOfComments(allComments, adapter,TAG, review, 0);
                endlessNestedScrollView.resetState();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavHostFragment.findNavController(ReviewDetailFragment.this).navigateUp();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(callback);
        binding.setReview(review);

    }

    private void saveComment(String comment, ParseUser currentUser, Review review) {
        Comment commentItem = new Comment();
        commentItem.setComment(comment);
        commentItem.setReview(review);
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

    private void loadMoreData(boolean b, int page) {
        Requests.getNextPageOfComments(allComments, adapter, TAG, review, page);
    }
}