package com.httpLike.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.httpLike.models.HttpLikeRequestModel;
import com.httpLike.models.HttpLikeResponseModel;

public interface HttpLikeRequestProcessor {
    HttpLikeResponseModel processRequest(HttpLikeRequestModel request) throws JsonProcessingException;
}
