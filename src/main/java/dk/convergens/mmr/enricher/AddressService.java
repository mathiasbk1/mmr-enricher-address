/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.convergens.mmr.enricher;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author Magnus
 */
@RegisterRestClient(baseUri = "http://dawa.aws.dk/adresser")
public interface AddressService {

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    public Response getAddresInformation(@QueryParam("q") String address, @QueryParam("struktur") String structure);

}
