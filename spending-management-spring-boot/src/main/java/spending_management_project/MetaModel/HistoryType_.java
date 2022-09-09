package spending_management_project.MetaModel;

import spending_management_project.po.HistoryType;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(HistoryType.class)
public class HistoryType_ {
    public static volatile SingularAttribute<HistoryType, Long> id;
    public static volatile SingularAttribute<HistoryType, String> name;
    public static final String ID = "id";
    public static final String NAME = "name";

}
