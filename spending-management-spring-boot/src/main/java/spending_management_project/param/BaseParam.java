package spending_management_project.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;
import spending_management_project.annotation.SignField;
import spending_management_project.constant.SignatureConstant;
import spending_management_project.enums.ErrorType;
import spending_management_project.exception.CommonsException;
import spending_management_project.tools.SignatureUtils;

import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Data
@EqualsAndHashCode(exclude = "currentUser")
@ToString(exclude = "currentUser")
public class
BaseParam implements Serializable {
    private static final Set<String> baseFields = new HashSet<>();


    private static final String EQUAL = "=";

    private static final long serialVersionUID = -103658650614029839L;


    private Integer pageNo;


    private Integer pageSize = 20;


    private String sortBy;


    private String sign;


    private UserDetails currentUser;

    public boolean isSignValid(String publicKey) throws CommonsException {
        String content = getSignContent();
        return SignatureUtils
                .rsaCheckContent(content, this.getSign(), publicKey, SignatureConstant.CHARSET_UTF8);
    }

    /**
     * Signature.
     *
     * @param privateKey Local private key.
     */
    public void sign(String privateKey) throws CommonsException {
        String content = getSignContent();//JsonConverter.convertToJSON(this).toString();
        this.sign = SignatureUtils.rsaSign(content, privateKey, SignatureConstant.CHARSET_UTF8);
    }

    /**
     * Get the signature content.
     *
     * @return signature content
     */

    public String getSignContent() throws CommonsException {
        StringBuilder buffer = new StringBuilder();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            Arrays.sort(pds, Comparator.comparing(FeatureDescriptor::getName));
            for (PropertyDescriptor pd : pds) {
                Method method = pd.getReadMethod();
                if (method == null) { // Ignore read-only field
                    continue;
                }
                Field field = null;
                String itemName = pd.getName();
                try {
                    field = baseFields.contains(itemName) ? BaseParam.class.getDeclaredField(itemName) :
                            this.getClass().getDeclaredField(itemName);
                } catch (Exception ignored) {
                    // Ignore
                }

                if (field == null || !field.isAnnotationPresent(SignField.class)) {
                    continue; // Ignore field without ParamField annotation.
                }
                field.setAccessible(true);
                Object itemValue = field.get(this);
                if (itemValue == null) {
                    continue;
                }
                buffer.append(itemName).append(EQUAL);
                if (itemValue.getClass().isAssignableFrom(List.class)) {
                    List<?> list = (List<?>) itemValue;
                    list.forEach(buffer::append);
                } else {
                    buffer.append(itemValue);
                }
            }
            return buffer.toString();
        } catch (Exception e) {
            throw new CommonsException(ErrorType.UNKNOWN);
        }
    }

//    public Pageable getPageable() {
//        PageRequest pageable = PageRequest.of(getPageNo() - 1, getPageSize());
//        return pageable;
//    }
}
