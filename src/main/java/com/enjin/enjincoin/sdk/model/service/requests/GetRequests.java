package com.enjin.enjincoin.sdk.model.service.requests;

import com.enjin.enjincoin.sdk.graphql.GraphQLRequest;

import java.math.BigInteger;
import java.util.List;

/**
 * A builder for requests from the Trusted platform.
 *
 * @author Evan Lindsay
 * @see com.enjin.enjincoin.sdk.service.requests.RequestsService
 */
public class GetRequests extends GraphQLRequest<GetRequests> {

    /**
     * The request id.
     *
     * @param requestId the request id.
     *
     * @return the builder.
     */
    public GetRequests withRequestId(BigInteger requestId) {
        withParameter("id", requestId);
        return this;
    }

    /**
     * The blockchain transaction id.
     *
     * @param transactionId the transaction id.
     *
     * @return the builder.
     */
    public GetRequests withTransactionId(String transactionId) {
        withParameter("transaction_id", transactionId);
        return this;
    }

    /**
     * The identity id.
     *
     * @param identityId the identity id.
     *
     * @return the builder.
     */
    public GetRequests withIdentityId(BigInteger identityId) {
        withParameter("identity_id", identityId);
        return this;
    }

    /**
     * The transaction type.
     *
     * @param type the transaction type.
     *
     * @return the builder.
     */
    public GetRequests withType(TransactionType type) {
        withParameter("type", type);
        return this;
    }

    /**
     * The recipients identity id.
     *
     * @param recipientId the recipients identity id.
     *
     * @return the builder.
     */
    public GetRequests withRecipientId(BigInteger recipientId) {
        withParameter("recipient_id", recipientId);
        return this;
    }

    /**
     * The recipients ethereum address.
     *
     * @param recipientAddress the recipients ethereum address.
     *
     * @return the builder.
     */
    public GetRequests withRecipientAddress(String recipientAddress) {
        withParameter("recipient_address", recipientAddress);
        return this;
    }

    /**
     * The sender or recipients identity id.
     *
     * @param senderOrRecipientId the sender or recipients identity id.
     *
     * @return the builder.
     */
    public GetRequests withSenderOrRecipientId(BigInteger senderOrRecipientId) {
        withParameter("sender_or_recipient_id", senderOrRecipientId);
        return this;
    }

    /**
     * The token id.
     *
     * @param tokenId the token id.
     *
     * @return the builder.
     */
    public GetRequests withTokenId(String tokenId) {
        withParameter("token_id", tokenId);
        return this;
    }

    /**
     * The token value.
     *
     * @param value the token value.
     *
     * @return the builder.
     */
    public GetRequests withValue(String value) {
        withParameter("value", value);
        return this;
    }

    /**
     * The request state.
     *
     * @param state the request state.
     *
     * @return the builder.
     */
    public GetRequests withState(TransactionState state) {
        withParameter("state", state);
        return this;
    }

    /**
     * A list of states to match.
     *
     * @param stateIn a list of states to match.
     *
     * @return the builder.
     */
    public GetRequests withStateIn(List<TransactionState> stateIn) {
        withParameter("state_in", stateIn);
        return this;
    }

    /**
     * A list of states to match.
     *
     * @param stateIn a list of states to match.
     *
     * @return the builder.
     */
    public GetRequests withStateIn(TransactionState... stateIn) {
        withParameter("state_in", stateIn);
        return this;
    }

    /**
     * Whether the transactions can only broadcast.
     *
     * @param canBroadcastOnly whether the transactions can only broadcast.
     *
     * @return the builder.
     */
    public GetRequests withCanBroadcastOnly(Boolean canBroadcastOnly) {
        withParameter("can_broadcast_only", canBroadcastOnly);
        return this;
    }

}
