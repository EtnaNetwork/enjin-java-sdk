package io.enjincoin.sdk.client.service.tokens.impl;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import com.enjin.java_commons.ObjectUtils;
import com.enjin.java_commons.StringUtils;

import io.enjincoin.sdk.client.config.Config;
import io.enjincoin.sdk.client.service.BaseService;
import io.enjincoin.sdk.client.service.tokens.TokensService;
import io.enjincoin.sdk.client.util.GsonUtils;
import io.enjincoin.sdk.client.util.JsonUtils;
import io.enjincoin.sdk.client.vo.token.CreateTokenRequestVO;
import io.enjincoin.sdk.client.vo.token.CreateTokenResponseVO;
import io.enjincoin.sdk.client.vo.token.TokenResponseVO;

/**
 * <p>
 * Contains services related to tokens.
 * </p>
 */
public class TokensServiceImpl extends BaseService implements TokensService {

    /**
     * Logger used by this class.
     */
    private static final Logger LOGGER = Logger.getLogger(TokensServiceImpl.class.getName());

    /**
     * Class constructor.
     *
     * @param config - the config to use
     */
    public TokensServiceImpl(final Config config) {
        super(config);
    }

    /**
     * Method to get all tokens
     */
    @Override
    public final TokenResponseVO[] getTokensSync() {
        TokenResponseVO[] getTokensResponseVO = null;

        // Get the tokens url and append the filter
        String getTokensUrl = getTokensUrl();

        String responseJsonString = performGetCall(getTokensUrl);

        if (StringUtils.isEmpty(responseJsonString)) {
            LOGGER.warning("No response returned from the getTokens call");
            return getTokensResponseVO;
        }
        getTokensResponseVO = (TokenResponseVO[]) JsonUtils.convertJsonToObject(GsonUtils.GSON, responseJsonString, TokenResponseVO[].class);
        return getTokensResponseVO;
    }

    /**
     * Method to get a token by id
     * @param tokenId for the token to retrieve
     * @return
     */
    @Override
    public final TokenResponseVO getTokenSync(Integer tokenId) {
        TokenResponseVO getTokenResponseVO = null;

        if (ObjectUtils.isNull(tokenId)) {
            LOGGER.warning("Tokens.getToken tokenId cannot be null");
            return getTokenResponseVO;
        }

        // Get the tokens url and append the filter
        String getTokensUrl = String.format("%s/%d", getTokensUrl(), tokenId);
        System.out.println("getTokensUrl:"+getTokensUrl);
        String responseJsonString = performGetCall(getTokensUrl);

        if (StringUtils.isEmpty(responseJsonString)) {
            LOGGER.warning("No response returned from the getTokens call");
            return getTokenResponseVO;
        }
        getTokenResponseVO = (TokenResponseVO) JsonUtils.convertJsonToObject(GsonUtils.GSON, responseJsonString, TokenResponseVO.class);
        return getTokenResponseVO;
    }

    /**
     * Method to create a token
     * @param createTokenRequestVO
     * @return
     */
    @Override
    public CreateTokenResponseVO createTokenSync(CreateTokenRequestVO createTokenRequestVO) {
        CreateTokenResponseVO createTokenResponseVO = null;

        if (ObjectUtils.isNull(createTokenRequestVO)) {
            LOGGER.warning("Tokens.create createTokenRequestVO cannot be null");
            return createTokenResponseVO;
        }
        String requestJsonString = JsonUtils.convertObjectToJson(GsonUtils.GSON, createTokenRequestVO);
        if (StringUtils.isEmpty(requestJsonString)) {
            LOGGER.warning("Tokens.create failed to create requestJson");
            return createTokenResponseVO;
        }

        // Get the tokens url and append the filter
        String createTokensUrl = getTokensUrl();
        String responseJsonString = performPostCall(createTokensUrl, requestJsonString);

        if (StringUtils.isEmpty(responseJsonString)) {
            LOGGER.warning("No response returned from the createToken call");
            return createTokenResponseVO;
        }
        createTokenResponseVO = (CreateTokenResponseVO) JsonUtils.convertJsonToObject(GsonUtils.GSON, responseJsonString, CreateTokenResponseVO.class);
        return createTokenResponseVO;
    }

    /**
     * Method to get all tokens.
     *
     * @return - GetTokenResponseVO
     */
    @Override
    public CompletableFuture<TokenResponseVO[]> getTokensAsync() {
        return CompletableFuture.supplyAsync(() -> this.getTokensSync(), this.getExecutorService());
    }

    /**
     * Method to create a token
     * @param createTokenRequestVO
     * @return
     */
    @Override
    public CompletableFuture<CreateTokenResponseVO> createTokenAsync(CreateTokenRequestVO createTokenRequestVO) {
        return CompletableFuture.supplyAsync(() -> this.createTokenSync(createTokenRequestVO), this.getExecutorService());
    }

    /**
     * Method to get a token by id
     * @param tokenId for the token to retrieve
     * @return
     */
    @Override
    public CompletableFuture<TokenResponseVO> getTokenAsync(Integer tokenId) {
        return CompletableFuture.supplyAsync(() -> this.getTokenSync(tokenId), this.getExecutorService());
    }
}