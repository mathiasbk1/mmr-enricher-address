package dk.convergens.mmr.enricher;

import dk.convergens.mmr.message.Message;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author Mathias
 */
@ApplicationScoped
public class AddressEnricher {

    private Jsonb jsonb = JsonbBuilder.create();

    @Inject
    @RestClient
    AddressService service;

    @Incoming("address-enrichment")
    public void enrich(String content) throws Exception {
        Message message = jsonb.fromJson(content, Message.class);
        message.startLog("address-enrichment");

        String address = message.getMetaData().getAddress();
        int zip = message.getMetaData().getZip();

        String addressString = address + ", " + zip;
        Response response = service.getAddresInformation(addressString, "flad");
        String responseContent = response.readEntity(String.class);
        JsonArray value = jsonb.fromJson(responseContent, JsonArray.class);
        JsonObject jsonObj = (JsonObject) value.get(0);
        message.getData().put("addressData", jsonObj);
       
        message.sendToKafkaQue();
    }
}
