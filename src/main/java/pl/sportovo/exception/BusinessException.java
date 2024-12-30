package pl.sportovo.exception;

import lombok.Data;

import java.util.List;

@Data
public class BusinessException extends RuntimeException {
    private final List<String> sids;

    public BusinessException(String sid) {
        this.sids = List.of(sid);
    }
}
