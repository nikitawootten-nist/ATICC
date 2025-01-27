package gov.nist.csd.pm.admintool.services;

import gov.nist.csd.pm.admintool.app.MainView;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;

@Service
public class RestService {
    private final  RestTemplate restTemplate;

    public RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public static CoordinatorScenarioResponse sendRequest(String url, HttpMethod method, Map<String, Object> params) {
        RestTemplate rt = new RestTemplate();
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, null);
        ResponseEntity<CoordinatorScenarioResponse> response;

        if (method == HttpMethod.GET) {
            try {
                response = rt.getForEntity(url, CoordinatorScenarioResponse.class, request);
            } catch (RestClientException e) {
                MainView.notify(e.getMessage(), MainView.NotificationType.ERROR);
                e.printStackTrace();
                return new CoordinatorScenarioResponse(false, e.getMessage());
            }
        } else /* if (method == HttpMethod.POST) */ {
            try {
                response = rt.postForEntity(url, request, CoordinatorScenarioResponse.class);
            } catch (RestClientException e) {
                MainView.notify(e.getMessage(), MainView.NotificationType.ERROR);
                e.printStackTrace();
                return new CoordinatorScenarioResponse(false, e.getMessage());
            }
        }

        return response.getBody();
    }

    public static Boolean checkCoordinatorStatus(String url) {
        RestTemplate rt = new RestTemplate();
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, null);
        ResponseEntity<CoordinatorStatusResponse> response;

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        converter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON));

        rt.getMessageConverters().add(converter);

        try {
            response = rt.getForEntity(url, CoordinatorStatusResponse.class, request);
        } catch (RestClientException e) {
            MainView.notify(e.getMessage(), MainView.NotificationType.ERROR);
            e.printStackTrace();
            return false;
        }

        if (response.getStatusCode().isError()) {
            MainView.notify("Coordinator status returned error", MainView.NotificationType.ERROR);
            return false;
        } else {
            MainView.notify("Coordinator status returned " + response.getStatusCode() + " with message: \"" +
                    response.getBody().status + "\"", MainView.NotificationType.SUCCESS);
            return true;
        }


    }


}