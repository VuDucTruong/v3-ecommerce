package shop.holy.v3.ecommerce.api.dto.mail;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MailProductKeys {
    String email;
    String fullName;
    @EqualsAndHashCode.Include long orderId;
    //    String bannerUrl;
    Map<Long, ProductMeta> metas;

    @Getter @Setter
    public static class ProductMeta {
        String productName;
        int quantity;
        List<ProductKey> keys = new ArrayList<>();
        public void addQty(int qty) {
            this.quantity += qty;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ProductKey {
        long id;
        String productKey;
        String duration;
    }

    public static MailProductKeys sample() {
        var m = new MailProductKeys();
        m.setEmail("paosdjfasd");
        m.setFullName("phong");
        m.setOrderId(1L);

        Map<Long, ProductMeta> metas = IntStream.range(1, 10)
                .boxed()
                .collect(Collectors.toMap(
                        Integer::longValue,
                        i -> sampleMeta()
                ));
        m.setMetas(metas);
        return m;
    }

    public static ProductMeta sampleMeta(){
        var pm = new ProductMeta();
        int rand = (int) (Math.random() * 100);
        pm.setProductName("Paosdjfasd"+rand);
        pm.setQuantity(1);
        List<ProductKey> keys = IntStream.range(0, 10).mapToObj(
                i-> sampleKey()
        ).toList();
        pm.setKeys(keys);
        return pm;
    }
    public static ProductKey sampleKey(){
        return new ProductKey((long) (Math.random() * 1000), UUID.randomUUID().toString(), "2 years");
    }


}
