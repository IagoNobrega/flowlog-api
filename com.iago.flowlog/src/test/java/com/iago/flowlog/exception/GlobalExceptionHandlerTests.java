package com.iago.flowlog.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.core.MethodParameter;

import com.iago.flowlog.controller.FocusController;
import com.iago.flowlog.dto.FocusRecordRequest;

class GlobalExceptionHandlerTests {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldReturnInternalServerErrorForUnexpectedException() {
        RuntimeException exception = new RuntimeException("unexpected error");

        ResponseEntity<ApiError> response = handler.handleAll(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Erro interno");
        assertThat(response.getBody().getDetails()).contains("unexpected error");
    }

    @Test
    void shouldReturnBadRequestForMalformedJson() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(
                "Malformed JSON request", new IllegalArgumentException("Unexpected character"), null);

        ResponseEntity<Object> response = handler.handleHttpMessageNotReadable(exception, new HttpHeaders(), HttpStatus.BAD_REQUEST,
                mock(WebRequest.class));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isInstanceOf(ApiError.class);

        ApiError error = (ApiError) response.getBody();
        assertThat(error.getError()).isEqualTo("Mensagem JSON inválida");
        assertThat(error.getDetails()).contains("Unexpected character");
    }

    @Test
    void shouldReturnValidationErrorDetailsForInvalidRequestBody() throws NoSuchMethodException {
        FocusRecordRequest request = new FocusRecordRequest();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(request, "focusRecordRequest");
        bindingResult.addError(new FieldError("focusRecordRequest", "nivelFoco", "nivel_foco é obrigatório"));
        MethodParameter methodParameter = new MethodParameter(FocusController.class.getMethod("registrarFoco", FocusRecordRequest.class), 0);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(exception, new HttpHeaders(), HttpStatus.BAD_REQUEST,
                mock(WebRequest.class));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isInstanceOf(ApiError.class);

        ApiError error = (ApiError) response.getBody();
        assertThat(error.getError()).isEqualTo("Validation failed");
        assertThat(error.getDetails()).contains("nivelFoco: nivel_foco é obrigatório");
    }
}
