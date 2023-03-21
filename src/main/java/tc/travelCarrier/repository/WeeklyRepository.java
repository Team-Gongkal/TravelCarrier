package tc.travelCarrier.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tc.travelCarrier.domain.Weekly;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WeeklyRepository {

    private final EntityManager em;

    public void save(Weekly weekly){
        em.persist(weekly);
    }

    public Weekly findOne(String id){
        Weekly weekly =  em.find(Weekly.class, id);
        return weekly;
    }

    public List<Weekly> findAll(){
        return em.createQuery("select w from Weekly w", Weekly.class)
                .getResultList();
    }
}
