package com.enjin.sdk.schemas.project.queries;

import com.enjin.sdk.graphql.GraphQLRequest;

/**
 * TODO
 */
public class AuthPlayer extends GraphQLRequest<AuthPlayer> {

    /**
     * TODO
     */
    public AuthPlayer() {
        super("enjin.sdk.project.AuthPlayer");
    }

    /**
     * TODO
     * @param id
     * @return
     */
    public AuthPlayer id(String id) {
        return set("id", id);
    }

}