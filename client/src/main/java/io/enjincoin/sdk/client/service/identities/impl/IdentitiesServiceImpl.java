package io.enjincoin.sdk.client.service.identities.impl;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import com.enjin.java_commons.MapUtils;
import com.enjin.java_commons.ObjectUtils;
import com.enjin.java_commons.OptionalUtils;
import com.enjin.java_commons.StringUtils;

import io.enjincoin.sdk.client.config.Config;
import io.enjincoin.sdk.client.service.BaseService;
import io.enjincoin.sdk.client.service.identities.IdentitiesService;
import io.enjincoin.sdk.client.util.GsonUtils;
import io.enjincoin.sdk.client.util.JsonUtils;
import io.enjincoin.sdk.client.vo.identity.CreateIdentityRequestVO;
import io.enjincoin.sdk.client.vo.identity.CreateIdentityResponseVO;
import io.enjincoin.sdk.client.vo.identity.GetIdentityResponseVO;
import io.enjincoin.sdk.client.vo.identity.LinkIdentityRequestVO;
import io.enjincoin.sdk.client.vo.identity.LinkIdentityResponseVO;
import io.enjincoin.sdk.client.vo.identity.UpdateIdentityRequestVO;
import io.enjincoin.sdk.client.vo.identity.UpdateIdentityResponseVO;

/**
 * <p>
 * Synchronous implementation of IdentitiesService.
 * </p>
 */
public class IdentitiesServiceImpl extends BaseService implements IdentitiesService {

    /**
     * Logger used by this class.
     */
    private static final Logger LOGGER = Logger.getLogger(IdentitiesServiceImpl.class.getName());

    /**
     * Class constructor.
     *
     * @param config - the config to use
     */
    public IdentitiesServiceImpl(final Config config) {
        super(config);
    }

    /**
     * Method to get all identities.
     *
     *
     * @return - GetIdentityResponseVO
     */
    @Override
    public final GetIdentityResponseVO[] getIdentitiesSync() {
        GetIdentityResponseVO[] getIdentitiesResponse = null;

        // Get the identities url
        String getIdentitiesUrl = getIdentitiesUrl();

        String responseJsonString = performGetCall(getIdentitiesUrl);
        if (StringUtils.isEmpty(responseJsonString)) {
            LOGGER.warning("No response returned from the getIdentities call");
            return getIdentitiesResponse;
        }
        getIdentitiesResponse = (GetIdentityResponseVO[]) JsonUtils.convertJsonToObject(GsonUtils.GSON, responseJsonString, GetIdentityResponseVO[].class);

        return getIdentitiesResponse;
    }

    /**
     * Method to get all identities - supplying a filter also.
     *  @param filterMap - the map with the data to use for filtering
     *
     * @return - GetIdentityResponseVO
     */
    @Override
    public GetIdentityResponseVO[] getIdentitiesSync(Map<String, Object> filterMap) {
        GetIdentityResponseVO[] getIdentitiesResponse = null;

        if (MapUtils.isEmpty(filterMap)) {
            LOGGER.warning("Identities.get filterMap is null or empty.");
            return getIdentitiesResponse;
        }

        String filterJsonString = JsonUtils.convertObjectToJson(GsonUtils.GSON, filterMap);

        // Get the identities url and append the filter
        String getIdentitiesUrl = String.format("%s?q=[%s]", getIdentitiesUrl(), filterJsonString);

        String responseJsonString = performGetCall(getIdentitiesUrl);
        if (StringUtils.isEmpty(responseJsonString)) {
            LOGGER.warning("No response returned from the getIdentities call");
            return getIdentitiesResponse;
        }
        getIdentitiesResponse = (GetIdentityResponseVO[]) JsonUtils.convertJsonToObject(GsonUtils.GSON, responseJsonString, GetIdentityResponseVO[].class);

        return getIdentitiesResponse;
    }

    /**
     * Method to get an entity by identityId
     * @param identityId
     * @return
     */
    @Override
    public GetIdentityResponseVO getIdentitySync(final Integer identityId) {
        GetIdentityResponseVO getIdentityResponse = null;

        if (identityId == null) {
            LOGGER.warning("Identities.get identityId is null.");
            return getIdentityResponse;
        }

        // Get the identities url and append the identityId
        String getIdentityByIdUrl = String.format("%s/%d", getIdentitiesUrl(), identityId);

        String responseJsonString = performGetCall(getIdentityByIdUrl);
        if (StringUtils.isEmpty(responseJsonString)) {
            LOGGER.warning("No response returned from the getIdentity call");
            return getIdentityResponse;
        }

        getIdentityResponse = (GetIdentityResponseVO) JsonUtils.convertJsonToObject(GsonUtils.GSON, responseJsonString, GetIdentityResponseVO.class);

        return getIdentityResponse;
    }

    @Override
    public final CreateIdentityResponseVO createIdentitySync(final CreateIdentityRequestVO createIdentityRequest) {
        CreateIdentityResponseVO createIdentityResponseVO = null;

        if (ObjectUtils.isNull(createIdentityRequest)) {
            LOGGER.warning("Identities.create createIdentityRequest is null.");
            return createIdentityResponseVO;
        }

        if (!OptionalUtils.isStringPresent(createIdentityRequest.getEthereumAddress())) {
            LOGGER.warning("Identities.create parameters may be empty or null.");
            return createIdentityResponseVO;
        }

        //Convert the request object to json
        String requestJsonString = JsonUtils.convertObjectToJson(GsonUtils.GSON, createIdentityRequest);
        if (StringUtils.isEmpty(requestJsonString)) {
            LOGGER.warning("Identities.create failed to convert request object to json.");
        }
         // Get the identities url
        String createIdentitiesUrl = getIdentitiesUrl();

        String responseJsonString = performPostCall(createIdentitiesUrl, requestJsonString);
        if (StringUtils.isEmpty(responseJsonString)) {
            LOGGER.warning("No response returned from the createIdentity call");
            return createIdentityResponseVO;
        }
        createIdentityResponseVO = (CreateIdentityResponseVO) JsonUtils.convertJsonToObject(GsonUtils.GSON, responseJsonString, CreateIdentityResponseVO.class);

        return createIdentityResponseVO;
    }

    /**
     * Method to delete an identity
     * @param identityId - the identity to delete
     * @return
     */
    @Override
    public final Boolean deleteIdentitySync(final Integer identityId) {
        Boolean deleteIdentityResponseVO = false;

        if (identityId == null) {
            LOGGER.warning("Identities.delete identityId is null.");
            return deleteIdentityResponseVO;
        }

        // Get the delete identities url and append the identityId
        String deleteIdentityByIdUrl = String.format("%s/%d", getIdentitiesUrl(), identityId);

        String responseJsonString = performDeleteCall(deleteIdentityByIdUrl);
        if (StringUtils.isEmpty(responseJsonString)) {
            LOGGER.warning("No response returned from the deleteIdentity call");
            return deleteIdentityResponseVO;
        }
        if (responseJsonString.equalsIgnoreCase("true")) {
            deleteIdentityResponseVO = true;
        }

        return deleteIdentityResponseVO;
    }

    @Override
    public final UpdateIdentityResponseVO updateIdentitySync(final UpdateIdentityRequestVO updateIdentityRequestVO, Integer identityId) {
        UpdateIdentityResponseVO updateIdentityResponseVO = null;

        if (ObjectUtils.isNull(updateIdentityRequestVO) || identityId == null) {
            LOGGER.warning("Identities.update updateIdentityRequestVO or identityId is null.");
            return updateIdentityResponseVO;
        }

        if (!OptionalUtils.isPresent(updateIdentityRequestVO.getFields())) {
            LOGGER.warning("Identities.update fields must be present.");
            return updateIdentityResponseVO;
        }

        //Convert the request object to json
        String requestJsonString = JsonUtils.convertObjectToJson(GsonUtils.GSON, updateIdentityRequestVO);
        if (StringUtils.isEmpty(requestJsonString)) {
            LOGGER.warning("Identities.update failed to convert request object to json.");
        }

        // Get the update identities url and append the identityId
        String updateIdentityByIdUrl = String.format("%s/%d", getIdentitiesUrl(), identityId);

        String responseJsonString = performPutCall(updateIdentityByIdUrl, requestJsonString);
        if (StringUtils.isEmpty(responseJsonString)) {
            LOGGER.warning("No response returned from the updateIdentity call");
            return updateIdentityResponseVO;
        }
        updateIdentityResponseVO = (UpdateIdentityResponseVO) JsonUtils.convertJsonToObject(GsonUtils.GSON, responseJsonString, UpdateIdentityResponseVO.class);

        return updateIdentityResponseVO;
    }

    @Override
    public final LinkIdentityResponseVO linkIdentitySync(final LinkIdentityRequestVO linkIdentityRequestVO, String linkingCode) {
        LinkIdentityResponseVO linkIdentityResponseVO = null;

        if (ObjectUtils.isNull(linkIdentityRequestVO) || StringUtils.isEmpty(linkingCode)) {
            LOGGER.warning("Identities.link linkIdentityRequestVO is null or linkingCode is null or empty.");
            return linkIdentityResponseVO;
        }

        //Convert the request object to json
        String requestJsonString = JsonUtils.convertObjectToJson(GsonUtils.GSON, linkIdentityRequestVO);
        if (StringUtils.isEmpty(requestJsonString)) {
            LOGGER.warning("Identities.link failed to convert request object to json.");
        }

        // Get the link wallet url and append the identityId
        String linkWalletUrl = String.format("%s/%s", getLinkWalletUrl(), linkingCode);

        String responseJsonString = performPutCall(linkWalletUrl, requestJsonString);
        if (StringUtils.isEmpty(responseJsonString)) {
            LOGGER.warning("No response returned from the linkIdentity call");
            return linkIdentityResponseVO;
        }
        linkIdentityResponseVO = (LinkIdentityResponseVO) JsonUtils.convertJsonToObject(GsonUtils.GSON, responseJsonString, LinkIdentityResponseVO.class);

        return linkIdentityResponseVO;
    }

    @Override
    public CompletableFuture<GetIdentityResponseVO[]> getIdentitiesAsync() {
        return CompletableFuture.supplyAsync(() -> this.getIdentitiesSync(), this.getExecutorService());
    }
    @Override
    public CompletableFuture<GetIdentityResponseVO[]> getIdentitiesAsync(Map<String, Object> filterMap) {
        return CompletableFuture.supplyAsync(() -> this.getIdentitiesSync(filterMap), this.getExecutorService());
    }

    @Override
    public CompletableFuture<GetIdentityResponseVO> getIdentityAsync(final Integer identityId) {
        return CompletableFuture.supplyAsync(() -> this.getIdentitySync(identityId), this.getExecutorService());
    }

    @Override
    public CompletableFuture<CreateIdentityResponseVO> createIdentityAsync(final CreateIdentityRequestVO request) {
        return CompletableFuture.supplyAsync(() -> this.createIdentitySync(request), this.getExecutorService());
    }

    @Override
    public CompletableFuture<UpdateIdentityResponseVO> updateIdentityAsync(final UpdateIdentityRequestVO request, final Integer identityId) {
        return CompletableFuture.supplyAsync(() -> this.updateIdentitySync(request, identityId), this.getExecutorService());
    }

    @Override
    public CompletableFuture<Boolean> deleteIdentityAsync(final Integer identityId) {
        return CompletableFuture.supplyAsync(() -> this.deleteIdentitySync(identityId), this.getExecutorService());
    }

    @Override
    public CompletableFuture<LinkIdentityResponseVO> linkIdentityAsync(final LinkIdentityRequestVO request, final String linkingCode) {
        return CompletableFuture.supplyAsync(() -> this.linkIdentitySync(request, linkingCode), this.getExecutorService());
    }

}