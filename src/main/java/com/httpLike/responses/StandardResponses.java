package com.httpLike.responses;

public class StandardResponses {
    static public HttpLikeStandardResponse invalidHttpLikeFormat = new HttpLikeStandardResponse(
            "Invalid HttpLike format.", 406
    );
    static public HttpLikeStandardResponse badRequest = new HttpLikeStandardResponse(
            "Bad request.", 400
    );
    static public HttpLikeStandardResponse noSuchMethod = new HttpLikeStandardResponse(
            "No such method.", 405
    );

    static public HttpLikeStandardResponse notFound = new HttpLikeStandardResponse(
            "Not found.", 404
    );
}
