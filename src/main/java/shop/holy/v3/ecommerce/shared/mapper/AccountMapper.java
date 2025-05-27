package shop.holy.v3.ecommerce.shared.mapper;


import jakarta.persistence.criteria.Predicate;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.account.RequestAccountRegistration;
import shop.holy.v3.ecommerce.api.dto.account.RequestProfileUpdate;
import shop.holy.v3.ecommerce.api.dto.account.ResponseUser;
import shop.holy.v3.ecommerce.api.dto.user.RequestUserCreate;
import shop.holy.v3.ecommerce.api.dto.user.RequestUserSearch;
import shop.holy.v3.ecommerce.api.dto.user.profile.RequestProfileCreate;
import shop.holy.v3.ecommerce.api.dto.user.profile.ResponseProfile;
import shop.holy.v3.ecommerce.persistence.entity.Account;
import shop.holy.v3.ecommerce.persistence.entity.Profile;
import shop.holy.v3.ecommerce.shared.constant.MapFuncs;
import shop.holy.v3.ecommerce.shared.util.AppDateUtils;

@Mapper(componentModel = "spring",
uses = CommonMapper.class)
@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public abstract class AccountMapper  {

    @Mapping(source = "profile.id", target = "profileId")
    public abstract AuthAccount fromAccountToAuthAccount(Account account);

    @Mapping(source = "fullName", target = "profile.fullName")
    public abstract Account fromRegistrationRequestToEntity(RequestAccountRegistration accountRegistration);

    @Mapping(source = "profile", target = "profile", ignore = true)
    public abstract Account fromUserCreateRequestToAccountEntity(RequestUserCreate request);

//    @Mapping(source = "imageUrl", target = "imageUrlId", qualifiedByName = MapFuncs.EXTRACT_ACCOUNT_PUBLIC_ID)
    public abstract Profile fromProfileUpdateRequestToEntity(RequestProfileUpdate request);

    public abstract Profile fromProfileRequestToEntity(RequestProfileCreate request);

    @Mapping(source = "imageUrlId", target = "imageUrl", qualifiedByName = MapFuncs.GEN_URL)
    public abstract ResponseProfile fromEntityToResponseProfile(Profile profile);

    public abstract ResponseUser fromEntityToResponseAccountDetail(Account account);

    public Specification<Account> fromRequestToSpecification(RequestUserSearch searchReq) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (!CollectionUtils.isEmpty(searchReq.ids())) {
                predicate = criteriaBuilder.and(predicate, root.get("id").in(searchReq.ids()));
            }

            if (StringUtils.hasLength(searchReq.fullName())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("profile").get("fullName"), "%" + searchReq.fullName().toLowerCase() + "%"));
            }

            if (!searchReq.deleted()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(root.get("deletedAt")));
            }

            if(!CollectionUtils.isEmpty(searchReq.roles())) {
                predicate = criteriaBuilder.and(predicate, root.get("role").in(searchReq.roles()) );
            }

            if (searchReq.createdAtFrom() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), AppDateUtils.toStartOfDay(searchReq.createdAtFrom())));
            }
            if (searchReq.createdAtTo() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), AppDateUtils.toStartOfDay(searchReq.createdAtTo())));
            }

            if (StringUtils.hasLength(searchReq.email())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("email"), "%" + searchReq.email().toLowerCase() + "%"));
            }
            return predicate;
        };

    }


}
