package spending_management_project.domain;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import spending_management_project.component.Transformer;
import spending_management_project.constant.CommonsConstant;
import spending_management_project.enums.ErrorType;
import spending_management_project.enums.ValidFlag;
import spending_management_project.exception.CommonsException;
import spending_management_project.po.User;
import spending_management_project.repo.CustomRepository;
import spending_management_project.tools.Assert;
import spending_management_project.tools.BeanUtils;
import spending_management_project.tools.ErrorMsgHelper;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class BaseDomain<T, ID extends Serializable> {

    @Transactional
    public <VO> VO creat(Class<VO> voType, Object inputParam, User currentUser) throws Exception {
        T po = transformer.param2PO(getClassT(), inputParam, getClassT().getDeclaredConstructor().newInstance(), currentUser);
        return createByPO(voType, po);
    }

    @Transactional
    public <VO> VO createByPO(Class<VO> voType, T inputPO) throws Exception {
        return transformer.po2VO(voType, inputPO);
    }

    @Transactional
    public T createByPO(T inputPO) {
        return repository.save(inputPO);
    }

    public <VO> List<VO> getAll(Specification<T> specification, Sort sort, Class<VO> voType) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List pos = repository.findAll(specification, sort);
        if (pos.isEmpty()) {
            return null;
        }
        return transformer.pos2VOs(voType, pos);
    }

    public Page<?> getPage(Specification<T> specification, Pageable pageable, Class<?> voType)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Page<T> poPage = repository.findAll(specification, pageable);
        if (poPage.getSize() == 0) {
            return null;
        }
        return transformer.poPage2VO(transformer.pos2VOs(voType, poPage.getContent()),
                pageable, poPage.getTotalElements());
    }

    public List<T> getAllByIds(List<ID> ids) {
        return ids.stream()
                .map(id -> repository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public <VO> VO getById(Long id, Class<VO> voType) throws Exception {
        return findById(id) == null ? null : transformer.po2VO(voType, findById(id));
    }

    @Transactional
    public <VO> VO update(Class<VO> voType, Object inputParam) throws Exception {
        T po = findByIdParam(inputParam);
        if (po == null) {
            throw new CommonsException(ErrorType.SYS0122,
                    ErrorMsgHelper.getReturnMsg(ErrorType.SYS0122, voType.getName(), CommonsConstant.ID));
        }
        BeanUtils.copyPropertiesIgnoreNull(inputParam, po);
        return updateByPO(voType, po);
    }

    @Transactional
    public <VO> VO updateByPO(Class<VO> voType, T inputPO) throws Exception {
        return transformer.po2VO(voType, updateByPO(inputPO));
    }

    @Transactional
    public T updateByPO(T po) {
        return repository.save(po);
    }

    @Transactional
    public void delete(Object inputParam) throws Exception {
        T po = findByIdParam(inputParam);
        repository.save(setInvalid(po));
    }

    @Transactional
    public void deleteById(Long id) throws Exception {
        T po = findById(id);
        repository.save(setInvalid(po));
    }

    @Transactional public void deepDelete(Long id) throws Exception {
        repository.deleteById((ID) id);
    }

    @Transactional
    public void deleteByIds(String ids) throws RuntimeException {
        transformer.idsStr2List(ids).forEach(id -> {
            try {
                this.deleteById(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public T findByIdParam(Object inputParam) throws Exception {
        Field idField = inputParam.getClass().getDeclaredField(CommonsConstant.ID);
        idField.setAccessible(true);
        return repository.findById((ID) idField.get(inputParam)).orElse(null);
    }

    public T findById(Long id) {
        return repository.findById((ID) id).orElse(null);
    }

    protected Class<T> getClassT() {
        Type type = getClass().getGenericSuperclass();
        return (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
    }


    private final CustomRepository<T, ID> repository;
    protected final Transformer transformer;

    public BaseDomain(CustomRepository<T, ID> repository, Transformer transformer) {
        Assert.defaultNotNull(repository);
        Assert.defaultNotNull(transformer);
        this.repository = repository;
        this.transformer = transformer;
    }

    protected T setInvalid(T po) throws Exception {
        Field validFlagField = po.getClass().getDeclaredField(CommonsConstant.VALID_FLAG);
        validFlagField.setAccessible(true);
        validFlagField.set(po, ValidFlag.INVALID);
        return po;
    }
}
