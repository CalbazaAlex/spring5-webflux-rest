package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

public class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @Before
    public void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void list() {
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstname("Vendor1").lastname("vendoreskovics1").build(),
                        Vendor.builder().firstname("Vendor2").lastname("vendoreskovics2").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);

    }

    @Test
    public void getById() {
        BDDMockito.given(vendorRepository.findById("someid"))
                .willReturn(Mono.just(Vendor.builder().firstname("Vend").lastname("vendoreskovics").build()));

        webTestClient.get().uri("/api/v1/vendors/someid").exchange().expectBody(Vendor.class);

    }

    @Test
    public void testCreateVendor() {
        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class))).willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendToSave = Mono.just(Vendor.builder().firstname("Some Vend").lastname("Another vendor").build());

        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(vendToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();

    }

    @Test
    public void testUpdate() {
        BDDMockito.given(vendorRepository.save(any(Vendor.class))).willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendToUpdate = Mono.just(Vendor.builder().firstname("Some Vend").lastname("Another vendor").build());

        webTestClient.put()
                .uri("/api/v1/vendors/someId")
                .body(vendToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}