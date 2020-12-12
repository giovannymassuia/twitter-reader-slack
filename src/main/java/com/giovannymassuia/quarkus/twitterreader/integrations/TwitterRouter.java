package com.giovannymassuia.quarkus.twitterreader.integrations;

import com.giovannymassuia.quarkus.twitterreader.processors.TweetProcessor;
import com.giovannymassuia.quarkus.twitterreader.services.SlackService;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.builder.endpoint.dsl.TwitterTimelineEndpointBuilderFactory;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;

@ApplicationScoped
public class TwitterRouter extends EndpointRouteBuilder {

    @ConfigProperty(name = "twitter.access-token")
    String twitterAccessToken;

    @ConfigProperty(name = "twitter.access-token-secret")
    String twitterAccessTokenSecret;

    @ConfigProperty(name = "twitter.consumer-key")
    String twitterConsumerKey;

    @ConfigProperty(name = "twitter.consumer-secret")
    String twitterConsumerSecret;

    @ConfigProperty(name = "twitter.users")
    String twitterUsers;

    @Inject
    @RestClient
    SlackService slackSenderService;

    @Override
    public void configure() {
        Arrays.stream(twitterUsers.split(",")).forEach(user -> 
                from(getFrom(user))
                .process(new TweetProcessor(slackSenderService))
                .to(log("search")));
    }

    private TwitterTimelineEndpointBuilderFactory.TwitterTimelineEndpointConsumerBuilder getFrom(String userId) {
        return twitterTimeline("user")
                .user(userId)
                .accessToken(twitterAccessToken)
                .accessTokenSecret(twitterAccessTokenSecret)
                .consumerKey(twitterConsumerKey)
                .consumerSecret(twitterConsumerSecret)
                .delay(5000);
    }

}
