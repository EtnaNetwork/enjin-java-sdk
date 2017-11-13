package com.enjin.coin.sdk.service;

import com.enjin.coin.sdk.config.EnjinConfig;
import com.enjin.coin.sdk.util.ArrayUtils;
import com.enjin.coin.sdk.util.Constants;
import com.enjin.coin.sdk.util.ObjectUtils;
import com.enjin.coin.sdk.util.StringUtils;
import com.enjin.coin.sdk.vo.token.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TokensService extends BaseService {

    private static final Logger LOGGER = Logger.getLogger(TokensService.class.getName());

    /**
     * Class constructor
     *
     * @param enjinConfig
     */
    protected TokensService(EnjinConfig enjinConfig) {
        super(enjinConfig);
    }

    /**
     * Method to get a token
     *
     * @param getTokenRequestVO
     * @return
     */
    public GetTokenResponseVO getToken(GetTokenRequestVO getTokenRequestVO) {
        GetTokenResponseVO getTokenResponseVO = null;

        if (ObjectUtils.isNull(getTokenRequestVO) || StringUtils.isEmpty(getTokenRequestVO.getTokenId())) {
            LOGGER.warning("getTokenRequestVO is null or tokenId passed in are null or empty");
            return getTokenResponseVO;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("token_id", getTokenRequestVO.getTokenId());

        // Construct new request
        String method = Constants.METHOD_TOKENS_GET;

        getTokenResponseVO = (GetTokenResponseVO) jsonRpcUtils.sendJsonRpcRequest(getTokensUrl(), GetTokenResponseVO.class, method, params);

        return getTokenResponseVO;
    }

    /**
     * Method to list the tokens
     *
     * @param listTokensRequestVO
     * @return
     */
    public ListTokensResponseVO listTokens(ListTokensRequestVO listTokensRequestVO) {
        ListTokensResponseVO listTokensResponseVO = null;

        if (ObjectUtils.isNull(listTokensRequestVO) || StringUtils.isEmpty(listTokensRequestVO.getAppId())
                || StringUtils.isEmpty(listTokensRequestVO.getAfterTokenId())
                || StringUtils.isEmpty(listTokensRequestVO.getLimit())) {
            LOGGER.warning("listTokensRequestVO is null, appId, afterTokenId or limit passed in are null or empty");
            return listTokensResponseVO;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("app_id", listTokensRequestVO.getAppId());
        params.put("after_token_id", listTokensRequestVO.getAfterTokenId());
        params.put("limit", listTokensRequestVO.getLimit());

        // Construct new request
        String method = Constants.METHOD_TOKENS_LIST;

        GetTokenResponseVO[] getTokenResponseVOArray = (GetTokenResponseVO[]) jsonRpcUtils.sendJsonRpcRequest(getTokensUrl(), GetTokenResponseVO[].class, method, params);
        if (ArrayUtils.isEmpty(getTokenResponseVOArray)) {
            LOGGER.warning("No tokens returned");
            return listTokensResponseVO;
        }
        listTokensResponseVO = ImmutableListTokensResponseVO.builder()
                .setGetTokensResponseVOArray(getTokenResponseVOArray)
                .build();

        return listTokensResponseVO;
    }
}
