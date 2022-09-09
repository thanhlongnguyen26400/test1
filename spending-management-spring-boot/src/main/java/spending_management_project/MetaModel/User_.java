package spending_management_project.MetaModel;

import spending_management_project.po.History;
import spending_management_project.po.User;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Set;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public class User_ {
    public static volatile SingularAttribute<User, Long> id;
    public static volatile SingularAttribute<User, Set<History>> histories;
    public static final String ID = "id";
    public static final String HISTORIES = "histories";
}
