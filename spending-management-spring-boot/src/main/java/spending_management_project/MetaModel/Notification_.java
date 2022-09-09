package spending_management_project.MetaModel;

import spending_management_project.enums.IncomeType;
import spending_management_project.po.History;
import spending_management_project.po.Notification;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Notification.class)
public class Notification_ {
    public static volatile SingularAttribute<History, Long> id;
    public static volatile SingularAttribute<History, Long> user_id;
    public static volatile SingularAttribute<History, IncomeType> type;
    public static final String ID = "id";
    public static final String TYPE = "type";
    public static final String USER_ID = "user_id";
}
