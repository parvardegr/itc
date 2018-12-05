package ir.rostami.itc.api.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ir.rostami.itc.api.service.JsonService;

@Path("test")
public class TestService extends JsonService {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String testPlainText() {
		return "test service from itc project!!!";
	}
}
