package spending_management_project.listener;


import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;
import spending_management_project.constant.CommonsConstant;
import spending_management_project.enums.ValidFlag;

import javax.persistence.PrePersist;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * Persist valid flag
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 10/10/2017
 * @since JDK1.8
 */
public class ValidFlagListener {

  @PrePersist
  public void onCreate(Object object) {
    final String ID = "id";
    final String VALID_FLAG = "validFlag";
    BeanUtilsBean beanUtilsBean = BeanUtilsBean2.getInstance();
    try {
      if (Objects.equals(beanUtilsBean.getProperty(object, ID), CommonsConstant.ZERO)) {
        beanUtilsBean.setProperty(object, VALID_FLAG, ValidFlag.VALID);
      }
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignore) {}
  }
}
