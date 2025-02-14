package com.senzhikong.web.controller;

import com.senzhikong.web.context.MyWebContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Shu.zhou
 */
@Slf4j
public class BaseController {
    @Resource
    protected MyWebContext webContext;

}
