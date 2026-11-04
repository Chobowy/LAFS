package zut.lafs;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import zut.lafs.exception.InvalidInputException;
import zut.lafs.exception.ItemNotFoundException;
import zut.lafs.exception.NotAuthorizedException;
import zut.lafs.service.ItemService;
import zut.lafs.storage.FileStorage;

import java.util.Map;

public class Main {

    public record LostRequest(String name, String category, String location, String date, String ownerContact) {}
    public record FoundRequest(String name, String category, String location, String date, String storedAt) {}
    public record ClaimRequest(int itemId, String studentId, String proof) {}

    public static void main(String[] args) {
        ItemService service = new ItemService(new FileStorage());

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(7070);

        app.get("/api/items", ctx -> {
            String q = ctx.queryParam("q");
            ctx.json(q == null || q.isBlank() ? service.getAllItems() : service.search(q));
        });

        app.post("/api/lost", ctx -> {
            LostRequest r = ctx.bodyAsClass(LostRequest.class);
            ctx.status(201).json(service.addLostItem(r.name(), r.category(), r.location(), r.date(), r.ownerContact()));
        });

        app.post("/api/found", ctx -> {
            FoundRequest r = ctx.bodyAsClass(FoundRequest.class);
            ctx.status(201).json(service.addFoundItem(r.name(), r.category(), r.location(), r.date(), r.storedAt()));
        });

        app.get("/api/items/{id}/matches", ctx ->
                ctx.json(service.findMatches(Integer.parseInt(ctx.pathParam("id")))));

        app.delete("/api/items/{id}", ctx -> {
            service.deleteItem(Integer.parseInt(ctx.pathParam("id")), ctx.queryParam("adminId"));
            ctx.status(204);
        });

        app.post("/api/claims", ctx -> {
            ClaimRequest r = ctx.bodyAsClass(ClaimRequest.class);
            ctx.status(201).json(service.submitClaim(r.itemId(), r.studentId(), r.proof()));
        });

        app.get("/api/claims", ctx -> ctx.json(service.getAllClaims()));

        app.put("/api/claims/{id}/approve", ctx ->
                ctx.json(service.approveClaim(Integer.parseInt(ctx.pathParam("id")), ctx.queryParam("adminId"))));

        app.exception(ItemNotFoundException.class, (e, ctx) -> ctx.status(404).json(Map.of("error", e.getMessage())));
        app.exception(InvalidInputException.class, (e, ctx) -> ctx.status(400).json(Map.of("error", e.getMessage())));
        app.exception(NotAuthorizedException.class, (e, ctx) -> ctx.status(403).json(Map.of("error", e.getMessage())));
    }
}