package com.giovannymassuia.quarkus.twitterreader.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giovannymassuia.quarkus.twitterreader.services.SlackService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import twitter4j.Status;
import twitter4j.User;

import java.util.List;
import java.util.Map;

public class TweetProcessor implements Processor {

    private SlackService slackService;

    public TweetProcessor(SlackService slackService) {
        this.slackService = slackService;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Status status = exchange.getIn().getBody(Status.class);

        ObjectMapper mapper = new ObjectMapper();

        slackService.sendMessage(mapper.writeValueAsString(Map.of(
                "text", "Nova Postagem!! :tada:",
                "attachments", List.of(Map.of(
                        "color", "#f2c744",
                        "blocks", List.of(
                                Map.of(
                                        "type", "section",
                                        "text", Map.of(
                                                "type", "mrkdwn",
                                                "text", getUserLink(status.getUser()) +
                                                        (status.isRetweet() ?
                                                                " retweetou de " + getUserLink(status.getRetweetedStatus().getUser()) : "")
                                                        + " \n\n " +
                                                        status.getText()
                                        ),
                                        "accessory", Map.of(
                                                "type", "image",
                                                "image_url", status.getUser().getProfileImageURLHttps(),
                                                "alt_text", status.getUser().getName()
                                        )
                                ),
                                Map.of(
                                        "type", "section",
                                        "fields", List.of(Map.of(
                                                "type", "mrkdwn",
                                                "text", "*Data*\n" + status.getCreatedAt()
                                        ))
                                )
                        ))))));
    }

    private String getUserLink(User user) {
        return "<https://twitter.com/" + user.getScreenName() + "|@" + user.getScreenName() + "> ";
    }

}
