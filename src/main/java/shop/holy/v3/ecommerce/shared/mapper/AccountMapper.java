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
import shop.holy.v3.ecommerce.api.dto.user.profile.ResponseProfile;
import shop.holy.v3.ecommerce.persistence.entity.Account;
import shop.holy.v3.ecommerce.persistence.entity.Profile;
import shop.holy.v3.ecommerce.shared.constant.MappingFunctions;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public abstract class AccountMapper extends IBaseMapper {

    public abstract AuthAccount fromAccountToAuthAccount(Account account);

    public abstract Account fromRegistrationRequestToEntity(RequestAccountRegistration accountRegistration);

    public abstract Account fromUserCreateRequestToAccountEntity(RequestUserCreate request);

    public abstract Profile fromProfileUpdateRequestToEntity(RequestProfileUpdate request);

    @Mapping(source = "imageUrlId", target = "imageUrl", qualifiedByName = MappingFunctions.GEN_URL)
    public abstract ResponseProfile fromEntityToResponseProfile(Profile profile);

    public abstract ResponseUser fromEntityToResponseAccountDetail(Account account);

    public Specification<Account> fromRequestToSpecification(RequestUserSearch searchReq) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (CollectionUtils.isEmpty(searchReq.ids())) {
                predicate = criteriaBuilder.and(predicate, root.get("id").in(searchReq.ids()));
            }
            if (StringUtils.hasLength(searchReq.citizenId())) {
                predicate = criteriaBuilder.and(predicate, root.get("profile").get("citizenId").in(searchReq.citizenId()));
            }
            if (StringUtils.hasLength(searchReq.fullName())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("profile").get("fullName"), searchReq.fullName()));
            }

            if (searchReq.deleted()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNotNull(root.get("deletedAt")));
            }
            if (searchReq.createdAtFrom() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), searchReq.createdAtFrom()));
            }
            if (searchReq.createdAtTo() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), searchReq.createdAtTo()));
            }

            if (StringUtils.hasLength(searchReq.phone())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("phone"), searchReq.phone()));
            }
            if (StringUtils.hasLength(searchReq.email())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("email"), searchReq.email()));
            }
            return predicate;
        };

    }


}
