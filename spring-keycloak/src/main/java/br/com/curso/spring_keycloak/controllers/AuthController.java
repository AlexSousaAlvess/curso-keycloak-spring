package br.com.curso.spring_keycloak.controllers;

import br.com.curso.spring_keycloak.components.HttpComponent;
import br.com.curso.spring_keycloak.models.User;
import br.com.curso.spring_keycloak.utils.HttpParamsMapBuilder;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
public class AuthController {
    @Value("${keycloak.auth-server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${keycloak.user-login.grant-type}")
    private String grantType;

    @Autowired
    private HttpComponent httpComponent;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody User user){
        httpComponent.httpHeaders()
                .setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //Estrutura da requisição
        MultiValueMap<String, String> map = HttpParamsMapBuilder.builder()
                .withClient(clientId)
                .withClientSecret(clientSecret)
                .withGrantType(grantType)
                .withUsername(user.getUsername())
                .withPassword(user.getPassword())
                .build();
        //Requisição ao keycloak
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, httpComponent.httpHeaders());

        try {
            ResponseEntity<String> response = httpComponent.restTemplate().postForEntity(
                    keycloakServerUrl+"/protocol/openid-connect/token",
                    request,
                    String.class
            );
            //Resposta com token
            return ResponseEntity.ok(response.getBody());
        }catch (HttpClientErrorException e){
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }
}
