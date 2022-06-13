package io.sapl.demo.axon.intrface.rest;

import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
@Profile("client")
public class ExtendedErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        var errorAttributes = super.getErrorAttributes(webRequest, options);
        var error = getError(webRequest);
        // sanitize string from leading class names, like org.package.SomeClass:

        var msg = error != null ? error.getMessage().replaceAll("^[a-zA-Z\\.]*:\\s*", "") : "";
        errorAttributes.put("message", msg);
        return errorAttributes;
    }
}