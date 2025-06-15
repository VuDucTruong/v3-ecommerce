package shop.holy.v3.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final Optional<CacheManager> optionalCacheManager;

//    public void cache(String key, @NotNull Object value, int seconds) {
//        if (optionalCacheManager.isEmpty())
//            return;
//
//        CacheManager cacheManager = optionalCacheManager.get();
//
//        Cache cache = cacheManager.getCache("shop");
//        if (cache == null) {
//            return;
//        }
//        cache.put(key, value);
//    }

    public <T> T getSafe(String cacheName, Object key, Class<T> tClass) {
        try {
            if (optionalCacheManager.isEmpty())
                return null;
            Cache cache = optionalCacheManager.get().getCache(cacheName);
            if (cache != null)
                return cache.get(key, tClass);
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public <T> void cacheSafe(String cacheName, Object key, Object value) {
        try {
            optionalCacheManager.ifPresent(cacheManager -> {
                Cache cache = cacheManager.getCache(cacheName);
                if (cache != null) {
                    cache.put(key, value);
                }
            });
        } catch (Exception _) {

        }
    }

    public <T> void partialUpdate(String cacheName, Object key, Class<T> clazz, Function<T, T> updater) {
        try {
            optionalCacheManager.ifPresent(cacheManager -> {
                Cache cache = cacheManager.getCache(cacheName);
                if (cache == null) {
                    T value = cache.get(key, clazz);
                    if (value != null) {
                        T newValue = updater.apply(value);
                        cache.put(key, newValue);
                    }
                }
            });
        } catch (Exception _) {

        }
    }

    public <T> void evictIn(String cacheName, Collection<T> keys) {
        try {
            optionalCacheManager.ifPresent(cacheManager -> {
                Cache cache = cacheManager.getCache(cacheName);
                if (cache != null) {
                    for (Object key : keys) {
                        cache.evict(key);
                    }
                }
            });
        } catch (Exception _) {
        }
    }
}
