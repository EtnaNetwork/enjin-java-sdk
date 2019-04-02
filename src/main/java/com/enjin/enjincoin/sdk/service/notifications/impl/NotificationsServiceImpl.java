package com.enjin.enjincoin.sdk.service.notifications.impl;

import com.enjin.enjincoin.sdk.Response;
import com.enjin.enjincoin.sdk.enums.NotificationType;
import com.enjin.enjincoin.sdk.model.body.GraphQLResponse;
import com.enjin.enjincoin.sdk.service.notifications.EventMatcher;
import com.enjin.enjincoin.sdk.service.notifications.NotificationListener;
import com.enjin.enjincoin.sdk.service.notifications.NotificationListenerRegistration;
import com.enjin.enjincoin.sdk.service.notifications.NotificationsService;
import com.enjin.enjincoin.sdk.service.notifications.ThirdPartyNotificationService;
import com.enjin.enjincoin.sdk.service.platform.PlatformService;
import com.enjin.enjincoin.sdk.service.platform.vo.PlatformDetails;
import com.enjin.enjincoin.sdk.service.platform.vo.data.PlatformData;
import com.enjin.java_commons.BooleanUtils;
import com.enjin.java_commons.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * <p>
 * NotificationsService - Synchronous.
 * </p>
 */
public class NotificationsServiceImpl implements NotificationsService {

    /**
     * Logger used by this class.
     */
    private static final Logger LOGGER = Logger.getLogger(NotificationsServiceImpl.class.getName());

    /**
     * Local variable for the third party notification service.
     */
    private ThirdPartyNotificationService thirdPartyNotificationService;

    /**
     * Local variable holding all the notification listeners.
     */
    private List<NotificationListenerRegistration> notificationListeners = new ArrayList<>();

    /**
     * Local variable for the platformService.
     */
    private PlatformService service;

    private int clientId;

    /**
     * Class constructor.
     *
     * @param service  - the platform service to use
     * @param clientId - the app id to use
     */
    public NotificationsServiceImpl(final PlatformService service, final String clientId) {
        this.service = service;
        this.clientId = Integer.parseInt(clientId);
    }

    /**
     * Method to initialize the notifications service.
     *
     * @return boolean
     */
    @Override
    public boolean start() {

        //Call out to the reinitialize method in order to initialize the pusher notifications
        return this.restart();
    }


    /**
     * Method to re-initialize the notifications service.
     *
     * @return boolean
     */
    @Override
    public boolean restart() {
        boolean initResult = false;

        final Response<GraphQLResponse<PlatformData>> response;
        try {
            response = this.service.getPlatformSync();
            if (response == null || response.body() == null) {
                LOGGER.warning("Failed to get platform details");
                return initResult;
            }

            final GraphQLResponse<PlatformData> body = response.body();
            if (body == null || body.getData() == null) {
                LOGGER.warning("Failed to get platform details");
                return initResult;
            }

            final PlatformData    data    = body.getData();
            final PlatformDetails details = data.getPlatform();
            // Setup the thirdPartyNotificationService to use the pusher service.
            if (this.thirdPartyNotificationService == null) {
                this.thirdPartyNotificationService = new PusherNotificationServiceImpl(details, this.clientId);
            }

            //boolean initPusherResult = this.thirdPartyNotificationService.init(platformAuthDetailsResponseVO);
            final boolean initPusherResult = this.thirdPartyNotificationService.init();
            if (BooleanUtils.isFalse(initPusherResult)) {
                LOGGER.warning("A problem occured initializing the pusher library");
                return initResult;
            }
            initResult = initPusherResult;
        } catch (final IOException e) {
            LOGGER.warning(String.format("An IOException has occured. Exception: %s.", e.getMessage()));
        }


        return initResult;
    }

    @Override
    public void shutdown() {
        if (this.thirdPartyNotificationService != null) {
            this.thirdPartyNotificationService.shutdown();
        }
    }

    /**
     * Method to configure a listener.
     *
     * @param listener notificationListener to configer
     *
     * @return NotificationListenerRegistration
     */
    @SuppressWarnings("rawtypes")
    @Override
    public NotificationListenerRegistration.RegistrationListenerConfiguration configureListener(final NotificationListener listener) {
        return NotificationListenerRegistration.configure(this, listener);
    }

    /**
     * Method to add a notification listener.
     *
     * @param listener - listener to add
     *
     * @return NotificationListenerRegistration
     */
    @Override
    public synchronized NotificationListenerRegistration addNotificationListener(final NotificationListener listener) {
        NotificationListenerRegistration registration = null;
        if (ObjectUtils.isNull(listener)) {
            LOGGER.warning("Could not add a NotificationListener because it was null.");
            return null;
        } else {
            final long count = this.notificationListeners.stream().filter(r -> r.getListener() == listener).count();

            if (count == 0) {
                registration = NotificationListenerRegistration.configure(this, listener).register();
            } else {
                LOGGER.warning("Could not add a NotificationListener because it was already registered.");
            }
        }
        return registration;
    }

    /**
     * Method to add a notification listener.
     *
     * @param listener     the listener to add
     * @param eventMatcher to match against
     *
     * @return NotificationListenerRegistration
     */
    @Override
    public NotificationListenerRegistration addNotificationListener(final NotificationListener listener,
                                                                    final EventMatcher eventMatcher) {
        return this.configureListener(listener).withMatcher(eventMatcher).register();
    }

    /**
     * Method to configure a listener for allowed types.
     *
     * @param listener to configure
     * @param allowed  types
     *
     * @return NotificationListenerRegistration
     */
    @Override
    public NotificationListenerRegistration addAllowedTypesNotificationListener(final NotificationListener listener,
                                                                                final NotificationType... allowed) {
        return this.configureListener(listener).withAllowedEvents(allowed).register();
    }

    /**
     * Method to configure a listener for ignore types.
     *
     * @param listener to configure
     * @param ignored  types
     *
     * @return NotificationListenerRegistration
     */
    @Override
    public NotificationListenerRegistration addIgnoredTypesNotificationListener(final NotificationListener listener,
                                                                                final NotificationType... ignored) {
        return this.configureListener(listener).withIgnoredEvents(ignored).register();
    }

    /**
     * Method to remove a notification listener.
     *
     * @param listener - listener to remove
     */
    @Override
    public synchronized void removeNotificationListener(final NotificationListener listener) {
        if (ObjectUtils.isNull(listener)) {
            LOGGER.warning("Could not remove a NotificationListener because it was null.");
            return;
        }

        final List<NotificationListenerRegistration> matching = this.notificationListeners.stream()
                                                                                          .filter(registration -> registration
                                                                                                  .getListener() == listener)
                                                                                          .collect(Collectors.toList());

        if (matching.size() > 0) {
            matching.forEach(this::removeNotificationListenerRegistration);
            // thirdPartyNotificationService.setNotificationListeners(notificationListeners);
        } else {
            LOGGER.warning("Could not remove a NotificationListener because it wasn't already registered.");
        }
    }

    /**
     * Method to register a notificationListener.
     *
     * @param registration to add
     */
    @Override
    public void addNotificationListenerRegistration(final NotificationListenerRegistration registration) {
        if (registration != null) {
            this.notificationListeners.add(registration);
            this.thirdPartyNotificationService.setNotificationListeners(this.notificationListeners);
        } else {
            LOGGER.warning("Could not add a NotificationListenerRegistration because it was null.");
        }
    }

    /**
     * Method to remove a notificationListener.
     *
     * @param registration to remove
     */
    @Override
    public void removeNotificationListenerRegistration(final NotificationListenerRegistration registration) {
        if (registration != null) {
            this.notificationListeners.remove(registration);
            this.thirdPartyNotificationService.setNotificationListeners(this.notificationListeners);
        } else {
            LOGGER.warning("Could not add a NotificationListenerRegistration because it was null.");
        }
    }

    @Override
    public void startAsync(final CompletableFuture<Boolean> future) {
        this.restartAsync(future);
    }

    @Override
    public void restartAsync(final CompletableFuture<Boolean> future) {
        this.service.getPlatformAsync(response -> {
            boolean result = false;
            if (response.body() != null) {
                final GraphQLResponse<PlatformData> body = response.body();

                shutdown();

                final PlatformData    data    = body.getData();
                final PlatformDetails details = data.getPlatform();
                NotificationsServiceImpl.this.thirdPartyNotificationService =
                        new PusherNotificationServiceImpl(details,
                                                          NotificationsServiceImpl.this.clientId);
                result = NotificationsServiceImpl.this.thirdPartyNotificationService.init();
            } else {
                LOGGER.warning("An error occurred while retrieving platform details.");
            }
            future.complete(result);
        });
    }
}