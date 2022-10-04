package noctem.purchaseService.global.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeignErrorDecoder implements ErrorDecoder {
    private final ObjectMapper objectMapper;

    @Override
    public CommonException decode(String methodKey, Response response) {
        try {
            CommonRequest commonRequest = objectMapper.readValue(
                    CharStreams.toString(response.body().asReader(Charset.defaultCharset())),
                    CommonRequest.class);
            return CommonException.builder()
                    .errorCode(commonRequest.getErrorCode())
                    .httpStatus(HttpStatus.valueOf(response.status()))
                    .build();
        } catch (Exception e) {
            return CommonException.builder()
                    .errorCode(6001)
                    .httpStatus(HttpStatus.valueOf(response.status()))
                    .build();
        }
    }
}
