package ru.tsu.hits.kosterror.messenger.authservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Класс со значениями приватного и публичного ключей.
 */
@ConfigurationProperties(prefix = "rsa")
@Data
public class RsaKeyProperties {

    private RSAPublicKey publicKey;

    private RSAPrivateKey privateKey;

}
