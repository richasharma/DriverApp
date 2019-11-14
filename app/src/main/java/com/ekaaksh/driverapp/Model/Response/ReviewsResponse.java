package com.ekaaksh.driverapp.Model.Response;

import com.ekaaksh.driverapp.Model.Beans.ReviewBean;

import java.util.List;

public class ReviewsResponse {


    /**
     * success : 1
     * review : [{"id":"42","username":"mukesh","image":"paaport_photo.jpg","review_text":"good food","ratting":"4","created_at":"5092019","login_with":"ios"}]
     */

    private String success;
    private List<ReviewBean> review;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ReviewBean> getReview() {
        return review;
    }

    public void setReview(List<ReviewBean> review) {
        this.review = review;
    }


}
