package spending_management_project.MetaModel;

import spending_management_project.enums.IncomeType;
import spending_management_project.po.History;
import spending_management_project.po.HistoryType;
import spending_management_project.po.User;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(History.class)
public abstract class History_ {
    public static volatile SingularAttribute<History, Date> date;
    public static volatile SingularAttribute<History, Long> money;
    public static volatile SingularAttribute<History, HistoryType> historyType;
    public static volatile SingularAttribute<History, IncomeType> type;
    public static volatile SingularAttribute<History, User> user;
    public static final String HISTORY_TYPE = "historyType";
    public static final String MONEY = "money";
    public static final String DATE = "date";
    public static final String TYPE = "type";
    public static final String USER = "user";
}
