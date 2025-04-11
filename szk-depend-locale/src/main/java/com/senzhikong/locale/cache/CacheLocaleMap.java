package com.senzhikong.locale.cache;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * @author shu.zhou
 */
@Getter
@Setter
public class CacheLocaleMap extends HashMap<String, String> {
    private Long expireTime;
}
