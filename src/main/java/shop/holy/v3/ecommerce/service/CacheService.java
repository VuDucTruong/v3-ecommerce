package shop.holy.v3.ecommerce.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final Optional<CacheManager> optionalCacheManager;

    public void cache(String key, @NotNull Object value, int seconds) {
        if (optionalCacheManager.isEmpty())
            return;

        CacheManager cacheManager = optionalCacheManager.get();

        Cache cache = cacheManager.getCache("shop");
        if (cache == null) {
            return;
        }
        cache.put(key, value);
    }

    public <T> void get(String cacheName, String key) {

    }


}
