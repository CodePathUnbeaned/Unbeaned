package com.unbeaned.app.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.unbeaned.app.BR;

public class RatingViewModel extends BaseObservable {
    private String averageRating;

    public RatingViewModel(double rating) {
        averageRating = String.valueOf(rating);
    }

    @Bindable
    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = String.valueOf(averageRating);
        notifyPropertyChanged(BR.averageRating);
    }

}
