package org.maven.search.rest.model;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoClassFilter;
import com.openpojo.reflection.filters.FilterClassName;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.PojoValidator;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.NoPublicFieldsRule;
import com.openpojo.validation.rule.impl.NoStaticExceptFinalRule;
import com.openpojo.validation.rule.impl.SerializableMustHaveSerialVersionUIDRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.DefaultValuesNullTester;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

public class SimplePojoTest {

    private static final int EXPECTED_CLASS_COUNT = 5;

    private List<PojoClass> pojoClasses;

    private PojoValidator pojoValidator;

    @Before
    public void setup() {
        // The package to test
        final String packageName = Answer.class.getPackage().getName();
        final PojoClassFilter filter = new FilterClassName("^((?!Test$).)*$");
        this.pojoClasses = PojoClassFactory.getPojoClasses(packageName, filter);

        this.pojoValidator = new PojoValidator();

        // Create Rules to validate structure for POJO_PACKAGE
        this.pojoValidator.addRule(new NoPublicFieldsRule());
        this.pojoValidator.addRule(new NoStaticExceptFinalRule());
        this.pojoValidator.addRule(new GetterMustExistRule());
        this.pojoValidator.addRule(new SetterMustExistRule());
        this.pojoValidator.addRule(new SerializableMustHaveSerialVersionUIDRule());

        // Create Testers to validate behaviour for POJO_PACKAGE
        this.pojoValidator.addTester(new DefaultValuesNullTester());
        this.pojoValidator.addTester(new SetterTester());
        this.pojoValidator.addTester(new GetterTester());
    }

    @Test
    public void ensureExpectedPojoCount() {
        Affirm.affirmEquals("Classes added / removed?", EXPECTED_CLASS_COUNT, this.pojoClasses.size());
    }

    @Test
    public void testPojoStructureAndBehavior() {
        for (final PojoClass pojoClass : this.pojoClasses) {
            this.pojoValidator.runValidation(pojoClass);
        }
    }

}
