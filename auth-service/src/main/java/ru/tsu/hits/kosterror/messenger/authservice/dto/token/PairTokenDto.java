package ru.tsu.hits.kosterror.messenger.authservice.dto.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PairTokenDto {

    private String accessToken;
    private String refreshToken;

}
