package com.senzhikong.web.auth;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author shu
 */
public abstract class BaseFilter implements Filter {
    void reject(HttpServletResponse response, Object error) throws IOException {
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter()
                .write(error.toString());
        response.getWriter()
                .close();
    }
}
