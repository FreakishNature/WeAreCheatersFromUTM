package com.httpLike;

import com.model.HttpLikeRequestModel;
import com.model.HttpLikeResponseModel;

public interface HttpLikeRequestProcessor {
    HttpLikeResponseModel processRequest(HttpLikeRequestModel request);
}
