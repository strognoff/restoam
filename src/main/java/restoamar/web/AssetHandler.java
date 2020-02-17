package restoamar.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import restoamar.domain.Asset;
import restoamar.service.AssetService;

import java.util.UUID;

@Service
public class AssetHandler {

    private final AssetService assetService;

    @Autowired
    public AssetHandler(AssetService assetService) {
        this.assetService = assetService;
    }

    public Mono<ServerResponse> get(ServerRequest request) {
        UUID uuid = UUID.fromString(request.pathVariable("id"));
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return this.assetService.findOne(uuid)
                .flatMap(asset -> ServerResponse.ok().body(Mono.just(asset), Asset.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        Mono<Asset> assetToBeCreated = serverRequest.bodyToMono(Asset.class);
        return assetToBeCreated.flatMap(asset ->
                ServerResponse.status(HttpStatus.CREATED).body(assetService.save(asset), Asset.class)
        );
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        Mono<Asset> assetToBeUpdated = serverRequest.bodyToMono(Asset.class);

        return assetToBeUpdated.flatMap(asset ->
                ServerResponse.status(HttpStatus.CREATED).body(assetService.update(asset), Asset.class));
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        UUID uuid = UUID.fromString(serverRequest.pathVariable("id"));

        return this.assetService.delete(uuid).flatMap(result -> ServerResponse.accepted().build());
    }


}
