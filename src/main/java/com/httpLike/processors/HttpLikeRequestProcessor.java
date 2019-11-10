package com.httpLike.processors;

import com.httpLike.models.HttpLikeRequestModel;
import com.httpLike.models.HttpLikeResponseModel;

public interface HttpLikeRequestProcessor {
    HttpLikeResponseModel processRequest(HttpLikeRequestModel request);
}
