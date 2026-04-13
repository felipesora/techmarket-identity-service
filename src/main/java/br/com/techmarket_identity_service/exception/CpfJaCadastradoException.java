package br.com.techmarket_identity_service.exception;

public class CpfJaCadastradoException extends RuntimeException {
    public CpfJaCadastradoException() {
        super("CPF já cadastrado para este perfil");
    }
}
