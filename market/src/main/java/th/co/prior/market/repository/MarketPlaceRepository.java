package th.co.prior.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.prior.market.entity.MarketPlaceEntity;

@Repository
public interface MarketPlaceRepository extends JpaRepository<MarketPlaceEntity, Integer> {
}
