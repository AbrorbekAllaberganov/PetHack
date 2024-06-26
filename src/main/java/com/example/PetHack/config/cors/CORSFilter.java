package com.example.PetHack.config.cors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CORSFilter extends GenericFilterBean implements Filter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
//        httpResponse.setHeader("Access-Control-Allow-Methods", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");

//        httpResponse.setHeader("Access-Control-Allow-Headers", "*");
        httpResponse.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, X-Auth-Token, X-Csrf-Token, WWW-Authenticate, Authorization");
        httpResponse.setHeader("Access-Control-Expose-Headers", "custom-token1, custom-token2");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "false");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");

        StringBuilder sb = new StringBuilder();
        sb.append("\nCORS HEADERS:\n");
        sb.append("---------------\n");
        httpResponse.getHeaderNames()
                .forEach(name -> {
                            sb.append(name).append(": ").append(httpResponse.getHeader(name)).append("\n");
                        }
                );
        logger.debug("********** CORS Configuration Completed **********");
        logger.debug(sb.toString());

        chain.doFilter(request, response);
    }


}
