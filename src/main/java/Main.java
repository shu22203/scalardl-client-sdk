import com.google.inject.Guice;
import com.google.inject.Injector;
import com.scalar.client.config.ClientConfig;
import com.scalar.client.service.ClientModule;
import com.scalar.client.service.ClientService;
import com.scalar.rpc.ledger.ContractExecutionResponse;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Injector injector = Guice.createInjector(new ClientModule(new ClientConfig(new File("client.properties"))));
        JsonObject json = Json.createObjectBuilder().add("asset_id", "shu22203-myasset").add("state", 2).build();

        try (ClientService service = injector.getInstance(ClientService.class)) {
            ContractExecutionResponse response = service.executeContract("shu22203-StateUpdater", json);
            System.out.println("status: " + response.getStatus());
            System.out.println("result: " + response.getResult());
        }
    }
}
