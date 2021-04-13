package com.unbeaned.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseUser;
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.CommentItemBinding;
import com.unbeaned.app.models.Comment;
import com.unbeaned.app.navigation.ReviewDetailFragmentDirections;

import java.util.List;

public class CommentFeedAdapter extends RecyclerView.Adapter<CommentFeedAdapter.ViewHolder> {
    Context context;
    List<Comment> comments;
    Fragment fragmentContext;


    public CommentFeedAdapter(Context context, List<Comment> comments, Fragment fragmentContext) {
        this.context = context;
        this.comments = comments;
        this.fragmentContext = fragmentContext;
    }

    @NonNull
    @Override
    public CommentFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        CommentItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.comment_item, parent, false);

        return new CommentFeedAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentFeedAdapter.ViewHolder holder, int position) {
        //get the data at position
        Comment comment = comments.get(position);
        //bind the place with the viewholder
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
    //Clear all elements of the recycler
    public void clear(){
        comments.clear();
        notifyDataSetChanged();
    }

    //Add a List of items
    public void addAll(List<Comment> commentsList){
        comments.addAll(commentsList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final CommentItemBinding binding;
        ImageView ivProfileImage;
        TextView tvName;
        TextView tvComment;
        RelativeLayout commentItemLinearContainer;

        public ViewHolder(@NonNull CommentItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
            ivProfileImage = binding.ivProfileImage;
            tvName = binding.tvName;
            tvComment = binding.tvComment;
            commentItemLinearContainer = binding.commentItemLinearContainer;

        }

        public void bind(Comment comment) {
            binding.setComment(comment);
            binding.executePendingBindings();

            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseUser user = comment.getUser();
                    ReviewDetailFragmentDirections.ActionReviewDetailFragmentToOtherUserFragment action = ReviewDetailFragmentDirections.actionReviewDetailFragmentToOtherUserFragment(user);
                    action.setBackPath("placeDetails");
                    NavHostFragment.findNavController(fragmentContext).navigate(action);
                }
            });
        }
        }
    }
