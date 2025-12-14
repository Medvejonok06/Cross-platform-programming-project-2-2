package org.acme;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated; // 1. Імпорт для захисту
import io.quarkus.security.identity.SecurityIdentity; // 2. Імпорт для даних юзера
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import java.util.List;

@Path("/")
@Authenticated // <--- 3. Ця анотація закриває вхід для анонімів
public class HomeResource {

    @Inject
    Template home;

    @Inject
    @RestClient
    ProductClient productClient;

    @Inject
    SecurityIdentity identity; // <--- 4. Тут живе інформація про залогіненого юзера

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        List<Product> products = productClient.getAll();

        // 5. Передаємо і продукти, і юзера в шаблон
        return home.data("products", products)
                .data("user", identity);
    }
}