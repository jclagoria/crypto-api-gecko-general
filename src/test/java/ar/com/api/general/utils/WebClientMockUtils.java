package ar.com.api.general.utils;

import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
public class WebClientMockUtils {

    public static WebClient.ResponseSpec mockResponseSpec() {
        WebClient.ResponseSpec responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);
        when(responseSpecMock.onStatus(any(), any())).thenReturn(responseSpecMock);
        return responseSpecMock;
    }

    public static WebClient mockWebClient(WebClient.ResponseSpec responseSpecMock) {
        WebClient webClientMock = Mockito.mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpecMock = Mockito.mock(WebClient.RequestHeadersSpec.class);

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);

        return webClientMock;
    }

}
