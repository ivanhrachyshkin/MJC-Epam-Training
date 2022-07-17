package by.hrachyshkin.idFinance.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "prop")
@NoArgsConstructor
@Getter
@Setter
public class Properties {

    private String catalog;
    private String notFound;
    private String conflict;
    private String negative;
    private String user;
    private String userInvalid;
    private String url;
}
