package br.com.techmarket_identity_service.exception;

public class SenhaAtualIncorretaException extends RuntimeException {

    public SenhaAtualIncorretaException() {
        super("Senha atual incorreta");
    }

    public SenhaAtualIncorretaException(String message) {
        super(message);
    }
}
