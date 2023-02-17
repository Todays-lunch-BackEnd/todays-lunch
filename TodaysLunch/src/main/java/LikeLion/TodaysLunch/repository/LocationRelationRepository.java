package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.relation.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRelationRepository extends JpaRepository<Location, Long> {

}
