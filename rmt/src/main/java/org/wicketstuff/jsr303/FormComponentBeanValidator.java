package org.wicketstuff.jsr303;

import de.flower.common.util.Check;
import de.flower.common.util.ReflectionUtil;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import java.io.Serializable;
import java.util.Set;


/**
 * Validator that can be bound to a input field but does a bean validation.
 * Requires that the form uses a CompoundPropertyModel containing the bean
 * to validate.
 * Must be added after the component is added to a form.
 *
 *
 * @author flowerrrr
 */
public class FormComponentBeanValidator<T> extends Behavior implements INullAcceptingValidator<String>, Serializable {

    private final static Logger log = LoggerFactory.getLogger(FormComponentBeanValidator.class);

    private Class<?>[] groups;

    private IModel<T> beanModel;

    private String propertyName;

    private Form form;

    public FormComponentBeanValidator(Class<?>[] groups) {
        this.groups = groups;
    }

    public FormComponentBeanValidator(Class<?> group) {
        this(new Class<?>[]{group});
    }

    @Override
    public void bind(Component component) {
        form = component.findParent(Form.class);
        Check.notNull(form);
        if (propertyName == null) {
            propertyName = component.getId();
        }
    }

    @Override
    public void validate(IValidatable<String> validatable) {
        T bean;
        if (beanModel == null) {
            bean = (T) form.getModelObject();
        } else {
            bean = beanModel.getObject();
        }

        // TODO (flowerrrr - 01.07.11) create copy of bean or write back original value after validating
        ReflectionUtil.setProperty(bean, propertyName, validatable.getValue());

        log.debug("Validating bean[{}]", bean);
        Set<ConstraintViolation<T>> violations = JSR303Validation.getValidator().validate(bean, groups);

        for (ConstraintViolation<T> v : violations) {
            log.debug("Constraint violation: " + v);
            validatable.error(new ViolationErrorBuilder.Property<T>(v).createError());
        }
    }

}