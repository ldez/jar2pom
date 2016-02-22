package org.sonatype.nexus.rest;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.NoPublicFieldsRule;
import com.openpojo.validation.rule.impl.NoStaticExceptFinalRule;
import com.openpojo.validation.rule.impl.SerializableMustHaveSerialVersionUIDRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.DefaultValuesNullTester;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.Before;
import org.junit.Test;
import org.sonatype.nexus.rest.custom.ObjectFactory;

import java.util.List;

public class SimplePojoTest {

    private static final int EXPECTED_CLASS_COUNT = 17;

    private List<PojoClass> pojoClasses;

    private Validator pojoValidator;

    @Before
    public void setup() {
        // The package to test
        final String packageName = ObjectFactory.class.getPackage().getName();
        this.pojoClasses = PojoClassFactory.getPojoClasses(packageName);

        this.pojoValidator = ValidatorBuilder.create()
                // Create Rules to validate structure for POJO_PACKAGE
                .with(new NoPublicFieldsRule())
                .with(new NoStaticExceptFinalRule())
                .with(new GetterMustExistRule())
                .with(new SetterMustExistRule())
                .with(new SerializableMustHaveSerialVersionUIDRule())
                // Create Testers to validate behaviour for POJO_PACKAGE
                .with(new DefaultValuesNullTester())
                .with(new SetterTester())
                .with(new GetterTester())
                .build();
    }

    @Test
    public void ensureExpectedPojoCount() {
        Affirm.affirmEquals("Classes added / removed?", EXPECTED_CLASS_COUNT, this.pojoClasses.size());
    }

    @Test
    public void testPojoStructureAndBehavior() {
        for (final PojoClass pojoClass : this.pojoClasses) {
            this.pojoValidator.validate(pojoClass);
        }
    }

}
