package com.giovannymassuia.quarkus.twitterreader.services;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RegisterRestClient
public interface SlackService {
   
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    void sendMessage(String payload);
}
