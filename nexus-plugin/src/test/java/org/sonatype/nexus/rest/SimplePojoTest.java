package org.sonatype.nexus.rest;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.openpojo.reflection.PojoClass;
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

public class SimplePojoTest {
    // Configured for expectation, so we know when a class gets added or
    // removed.
    private static final int EXPECTED_CLASS_COUNT = 17;

    // The package to test
    private static final String POJO_PACKAGE = "org.sonatype.nexus.rest.custom";

    private List<PojoClass> pojoClasses;
    private PojoValidator pojoValidator;

    @Before
    public void setup() {

        // this.pojoClasses = PojoClassFactory.getPojoClasses(POJO_PACKAGE, new
        // FilterBasedOnInheritance(AramisModel.class));
        this.pojoClasses = PojoClassFactory.getPojoClasses(POJO_PACKAGE);

        this.pojoValidator = new PojoValidator();

        // Create Rules to validate structure for POJO_PACKAGE
        this.pojoValidator.addRule(new NoPublicFieldsRule());
        // this.pojoValidator.addRule(new NoPrimitivesRule());
        this.pojoValidator.addRule(new NoStaticExceptFinalRule());
        this.pojoValidator.addRule(new GetterMustExistRule());
        this.pojoValidator.addRule(new SetterMustExistRule());
        // this.pojoValidator.addRule(new NoNestedClassRule());
        // this.pojoValidator.addRule(new NoFieldShadowingRule());
        this.pojoValidator.addRule(new SerializableMustHaveSerialVersionUIDRule());

        // Create Testers to validate behaviour for POJO_PACKAGE
        this.pojoValidator.addTester(new DefaultValuesNullTester());
        // this.pojoValidator.addTester(new SetterTester());
        this.pojoValidator.addTester(new GetterTester());
    }

    @Test
    public void ensureExpectedPojoCount() {
        // for (final PojoClass pjc : this.pojoClasses) {
        // System.out.println(pjc.getName());
        // }

        Affirm.affirmEquals("Classes added / removed?", EXPECTED_CLASS_COUNT, this.pojoClasses.size());
    }

    @Test
    public void testPojoStructureAndBehavior() {
        for (final PojoClass pojoClass : this.pojoClasses) {
            this.pojoValidator.runValidation(pojoClass);
        }
    }

}
